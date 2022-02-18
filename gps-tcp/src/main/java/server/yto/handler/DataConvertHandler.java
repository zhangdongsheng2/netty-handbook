package server.yto.handler;

import com.google.common.primitives.Bytes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import server.yto.utils.DataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DataConvertHandler
 * @Description 将转义的数据还原
 * @Aurhor yto 吴建伟
 * @Date 2018/11/21 9:28
 */
public class DataConvertHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        if (buf.readableBytes() < 1) {//排除掉开始的标识符
            return;
        }
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        List<Byte> byteList = new ArrayList<Byte>(req.length);
        byte replace = 0x7e;//转义替换
        for (int i = 0; i < req.length; i++) {
            if (req[i] == 0x7d) {
                if (req[i + 1] == 0x02) {
                    byteList.add(replace);
                    i++;
                } else {
                    if (req[i + 1] == 0x01) {
                        i++;
                    }
                    byteList.add(req[i]);
                }
            } else {
                byteList.add(req[i]);
            }
        }
        ctx.fireChannelRead(Unpooled.copiedBuffer(Bytes.toArray(byteList)));
    }
}

