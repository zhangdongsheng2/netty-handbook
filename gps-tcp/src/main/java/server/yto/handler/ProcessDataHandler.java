package server.yto.handler;


import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import server.yto.constant.TianMaiBodyType;
import server.yto.constant.TianMaiHeaderType;
import server.yto.entity.DataGPSFormat;
import server.yto.entity.EntityGPS;
import server.yto.entity.TianMaiBody;
import server.yto.entity.TianMaiHeader;
import server.yto.kafka.TianMaiKafkaProducer;
import server.yto.utils.BitOperator;
import server.yto.utils.DateUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @ClassName RestoreDataHandler
 * @Description 校验数据及校验码、消息头以及消息体中转义还原
 * @Aurhor yto 吴建伟
 * @Date 2018/8/31 11:06
 */
@Component("processDataHandler")
@Scope("prototype")
@Slf4j
public class ProcessDataHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private BitOperator bitOperator;


    @Autowired
    private TianMaiKafkaProducer tianMaiKafkaProducer;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //解析数组下标
        int index = 0;
        //解析报文
        ByteBuf buf = (ByteBuf) msg;
        if (buf.readableBytes() <= 0) {
            return;
        }
        //获取报文字节数组
        byte[] bytes = buf.array();
        DataGPSFormat dataGPSFormat = new DataGPSFormat();
        index += 1;

        int msgLength = parseIntFromBytes(bytes, index, 2, 0);
        dataGPSFormat.setMsgLength(msgLength);
        index += 2;

        dataGPSFormat.setMsgVersion(parseIntFromBytes(bytes, index, 1, 0));
        index += 1;

        int msgType = parseIntFromBytes(bytes, index, 1, 0);
        if (msgType != 3) {
            log.info("包类型排除:{}", msgType);
            if (msgType == 8) {
                log.info("握手包回应:{}", msgType);
                ctx.writeAndFlush(Unpooled.copiedBuffer(bytes)).sync();
            }
            release(msg);
            return;
        }
        dataGPSFormat.setMsgType(msgType);
        index += 1;

        int headerLength = parseIntFromBytes(bytes, index, 2, 0);
        index += 2;
        dataGPSFormat.setMsgHeaderLength(headerLength);

        byte[] msgHeader = new byte[headerLength];
        System.arraycopy(bytes, index, msgHeader, 0, headerLength);
        index += headerLength;
        dataGPSFormat.setMsgHeader(msgHeader);

        //报文体多出报文尾处理
        byte[] tempMsgBody = new byte[msgLength - index];
        System.arraycopy(bytes, index, tempMsgBody, 0, msgLength - index);
        byte[] msgBody = new byte[tempMsgBody.length - 1];
        System.arraycopy(tempMsgBody, 0, msgBody, 0, msgBody.length);
        dataGPSFormat.setMsgBody(msgBody);
        //获取报文信息
        getEntityGps(dataGPSFormat);
        release(msg);
    }


    private int parseIntFromBytes(byte[] data, int startIndex, int length, int defaultVal) {
        try {
            // 字节数大于4,从起始索引开始向后处理4个字节,其余超出部分丢弃
            final int len = Math.min(length, 4);
            byte[] tmp = new byte[len];
            System.arraycopy(data, startIndex, tmp, 0, len);
            return bitOperator.byteToInteger(tmp);
        } catch (Exception e) {
            log.error("解析整数出错:{}", e.getMessage());
            e.printStackTrace();
            return defaultVal;
        }
    }

    /**
     * 解析数据
     *
     * @param dataGPSFormat 消息体
     */
    public void getEntityGps(DataGPSFormat dataGPSFormat) {
        EntityGPS entityGPS = new EntityGPS();
        TianMaiHeader tianMaiHeader = getTiamMaiHeaderMessage(dataGPSFormat.getMsgHeader(), dataGPSFormat.getMsgHeaderLength() - 1);
        TianMaiBody tianMaiBody = getTiamMaiBodyMessage(dataGPSFormat.getMsgBody());
        //发送kafka
        if (null != tianMaiHeader && null != tianMaiBody) {
            entityGPS.setROUTEID(String.valueOf(tianMaiHeader.getSourceAddressOrganization()));
            entityGPS.setPRODUCTID(String.valueOf(tianMaiHeader.getSourceAddress()));
            entityGPS.setLONGITUDE(tianMaiBody.getLng());
            entityGPS.setLATITUDE(tianMaiBody.getLat());
            entityGPS.setALTITUDE(tianMaiBody.getHeight());
            entityGPS.setGPSSPEED(tianMaiBody.getSpeed());
            entityGPS.setROTATIONANGLE(tianMaiBody.getDirectionAngle());

            //业务时间 从包体里取
            Calendar actDateTime = Calendar.getInstance();
            actDateTime.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
            actDateTime.add(Calendar.SECOND, tianMaiBody.getDateTime());
            //时区问题
            actDateTime.add(Calendar.HOUR, 8);
            entityGPS.setACTDATETIME(DateUtil.formatYMDHMS(actDateTime.getTime()));

            //时间戳 从包头里取
            Calendar recDateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            recDateTime.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
            recDateTime.add(Calendar.SECOND, tianMaiHeader.getTimestamp());
            //时区问题
            recDateTime.add(Calendar.HOUR, 8);
            entityGPS.setRECDATETIME(DateUtil.formatYMDHMS(recDateTime.getTime()));

            entityGPS.setACTDATETIMEType((int) tianMaiBody.getDateTimeType());
            entityGPS.setISAPPEND((int) tianMaiBody.getReissueMark());
            entityGPS.setAveragepeed(tianMaiBody.getAvgSpeed());
            entityGPS.setBUSSID((int) tianMaiBody.getTrainNumberType());
            entityGPS.setDATATYPE("");
            entityGPS.setSUBROUTEID(String.valueOf(tianMaiBody.getSubLineNumber()));
            entityGPS.setSiteNo(String.valueOf(tianMaiBody.getStationUnifiedNumber()));
            entityGPS.setSENSORMILEAGE(tianMaiBody.getAccumulatedMileage());
            //entityGPS.setSENSORMILEAGE(tianMaiBody.getAccumulatedMileageBySensor());
        }

        tianMaiKafkaProducer.send(entityGPS);
    }

    /**
     * 获取消息头
     *
     * @param bytes  数组
     * @param length 数组长度
     * @return
     */
    private TianMaiHeader getTiamMaiHeaderMessage(byte[] bytes, int length) {
        Map<String, Object> map = new HashMap<>();
        int byteIndex = 0;
        do {
            byte tempType = bytes[byteIndex];
            byteIndex += 1;
            String tempKey = TianMaiHeaderType.getValue(tempType);
            byte tempLength = bytes[byteIndex];
            byteIndex += 1;
            byte[] tempData;
            if (tempLength == 1) {
                tempData = new byte[4];
                System.arraycopy(bytes, byteIndex, tempData, 0, 4);
                byteIndex += 4;
            } else if (tempLength == 2) {
                tempData = new byte[2];
                System.arraycopy(bytes, byteIndex, tempData, 0, 2);
                byteIndex += 2;
            } else if (tempLength == 3) {
                tempData = new byte[1];
                System.arraycopy(bytes, byteIndex, tempData, 0, 1);
                byteIndex += 1;
            } else if (tempLength == 4) {
                return null;
            } else if (tempLength == 5) {
                return null;
            } else {
                log.info("报文头字段长度解析异常,长度{}", tempLength);
                return null;
            }
            int value = parseIntFromBytes(tempData, 0, tempData.length, 0);
            map.put(tempKey, value);

        } while (byteIndex < length);
        if (!(map.size() > 0)) {
            log.info("报文头字段解析为空");
            return null;
        }
        //map转成实体
        return JSON.parseObject(JSON.toJSONString(map), TianMaiHeader.class);
    }

    /**
     * 解析报文体
     *
     * @param bytes
     * @return
     */
    private TianMaiBody getTiamMaiBodyMessage(byte[] bytes) {
        Map<String, Object> map = new HashMap<>();
        int byteIndex = 0;
        int maxIndex = bytes.length - 1;
        //解析报文体
        do {
            byte tempType = bytes[byteIndex];
            byteIndex += 1;
            String tempKey = TianMaiBodyType.getValue(tempType);
            byte tempLength = bytes[byteIndex];
            byteIndex += 1;
            byte[] tempData;
            if (tempLength == 1) {
                tempData = new byte[4];
                System.arraycopy(bytes, byteIndex, tempData, 0, 4);
                byteIndex += 4;
            } else if (tempLength == 2) {
                tempData = new byte[2];
                System.arraycopy(bytes, byteIndex, tempData, 0, 2);
                byteIndex += 2;
            } else if (tempLength == 3) {
                tempData = new byte[1];
                System.arraycopy(bytes, byteIndex, tempData, 0, 1);
                byteIndex += 1;
            } else if (tempLength == 4) {
                return null;
            } else if (tempLength == 5) {
                return null;
            } else {
                log.info("报文体字段长度解析异常,长度{}", tempLength);
                return null;
            }
            int value = parseIntFromBytes(tempData, 0, tempData.length, 0);
            map.put(tempKey, value);
        } while (byteIndex < maxIndex);

        //经纬度合并转换
        if (map.size() > 0) {
            if (map.get("lngHeight") != null && map.get("lngLow") != null) {
                double lngHeight = Double.parseDouble(map.get("lngHeight").toString());
                double lngLow = Double.parseDouble(map.get("lngLow").toString());
                map.put("lng", lngHeight + (lngLow / 1000000));
            }
            if (map.get("latHeight") != null && map.get("latLow") != null) {
                double latHeight = Double.parseDouble(map.get("latHeight").toString());
                double latLow = Double.parseDouble(map.get("latLow").toString());
                map.put("lat", latHeight + (latLow / 1000000));
            }
        } else {
            log.info("报文体字段长度解析异常");
            return null;
        }
        //map转成实体
        return JSON.parseObject(JSON.toJSONString(map), TianMaiBody.class);
    }


    /**
     * 功能：服务端发生异常的操作
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        ctx.close();
        log.error("发生异常:{}", cause.getMessage());
        cause.printStackTrace();
    }

    private void release(Object msg) {
        try {
            ReferenceCountUtil.release(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
