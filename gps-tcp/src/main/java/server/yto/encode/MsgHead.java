package server.yto.encode;

import lombok.Data;
import server.yto.utils.ByteUtil;
import server.yto.utils.EndianUtil;

import java.io.UnsupportedEncodingException;

/**
 * <pre>
 * 名称: GpsMsgHead
 * 描述: GpsMsgHead
 * </pre>
 *
 * @author lcyuan(01490324 @ yto.net.cn)
 * @version 2018/8/21 16:45
 * @since 1.0.0
 */
@Data
public class MsgHead extends BaseMsg {
    private MsgAttr attr;      //消息体属性
    private BCDArray mobile;   //终端手机号
    private Integer serial;    //消息流水号
    private MsgOption option;   //消息包封装项

    public MsgHead() {
        super();
        this.attr = new MsgAttr();
        this.mobile = new BCDArray(6);
        this.serial = 0;
        this.option = new MsgOption();
    }

    public MsgHead(MsgType msgType) {
        super(msgType);
        this.attr = new MsgAttr();
        this.mobile = new BCDArray(6);
        this.serial = 0;
        this.option = new MsgOption();
    }

    @Override
    protected byte[] toBytes() throws UnsupportedEncodingException {
        int headLen = 12;
        boolean isSub = this.getAttr().isSub();
        if (isSub) headLen = 14;
        byte[] bytes = new byte[headLen];
        byte[] msgId = this.getMsgType().bytes();
        byte[] attr = this.attr.getBytes();
        byte[] mobile = this.mobile.getBytes();
        byte[] serial = ByteUtil.getBytes(EndianUtil.convert(this.serial.shortValue()));
        System.arraycopy(msgId, 0, bytes, 0, msgId.length);
        System.arraycopy(attr, 0, bytes, 2, attr.length);
        System.arraycopy(mobile, 0, bytes, 4, mobile.length);
        System.arraycopy(serial, 0, bytes, 10, serial.length);
        if (isSub) {
            byte[] option = this.option.getBytes();
            System.arraycopy(option, 0, bytes, 12, option.length);
        }
        return bytes;
    }

    public class MsgAttr {
        private Short value = 0x0000;

        /**
         * 判断是否分包
         *
         * @return
         */
        public boolean isSub() {
            return (this.value >>> 13 & 0x0001) > 0;
        }

        /**
         * 设置是否分包
         *
         * @param isSub
         */
        public void setSub(boolean isSub) {
            if (isSub) {
                this.value = (short) (this.value | 0x2000);
            } else {
                this.value = (short) (this.value & 0xdfff);
            }
        }

        /**
         * 获取加密方式
         *
         * @return
         */
        public CryptType cryptType() {
            return CryptType.parseOf(this.value >>> 10 & 0x0007);
        }

        /**
         * 设置加密方式
         *
         * @param type
         */
        public void setCryptType(CryptType type) {
            this.value = (short) ((this.value & 0xe1ff) | (type.code() << 10));
        }

        /**
         * 获取消息体长度
         *
         * @return
         */
        public int getBodyLen() {
            return this.value & 0x000003ff;
        }

        /**
         * 设置消息体长度
         *
         * @param len
         */
        public void setBodyLen(int len) {
            this.value = (short) ((this.value & 0xfc00) | (len & 0x000003ff));
        }

        /**
         * 返回大端字节码
         *
         * @return
         */
        public byte[] getBytes() {
            return ByteUtil.getBytes(EndianUtil.convert(this.value));
        }
    }

    @Data
    public class MsgOption {
        private Integer total; // 消息总包数
        private Integer serial; // 包序号

        /**
         * 返回大端字节码
         *
         * @return
         */
        public byte[] getBytes() {
            Integer option = total << 16 | (serial & 0x0000ffff);
            return ByteUtil.getBytes(EndianUtil.convert(option));
        }
    }
}
