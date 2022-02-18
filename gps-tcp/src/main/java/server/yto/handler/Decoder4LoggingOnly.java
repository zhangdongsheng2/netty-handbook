package server.yto.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import server.yto.utils.BitOperator;
import server.yto.utils.HexStringUtils;

import java.util.List;

/**
 * @author 86199
 */
@Slf4j
public class Decoder4LoggingOnly extends ByteToMessageDecoder {

    private BitOperator bitOperator;


    public Decoder4LoggingOnly() {
        this.bitOperator = new BitOperator();
    }


    /**
     * 仅作日志打印 无其它操作
     *
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String hex = buf2Str(in);
        log.info("ip={},hex = {}", ctx.channel().remoteAddress(), hex);
        ByteBuf buf = Unpooled.buffer();
        while (in.isReadable()) {
            buf.writeByte(in.readByte());
        }
        out.add(buf);
    }


    private String buf2Str(ByteBuf in) {
        byte[] dst = new byte[in.readableBytes()];
        in.getBytes(0, dst);
        return HexStringUtils.toHexString(dst);
    }


    private byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (byte[] value : values) {
            length_byte += value.length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (byte[] b : values) {
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

}

