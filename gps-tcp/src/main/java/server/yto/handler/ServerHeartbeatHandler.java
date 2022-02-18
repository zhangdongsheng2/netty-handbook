package server.yto.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;


/**
 * @ClassName ServerHeartbeatHandler
 * @Description T0DO
 * @Aurhor yto 吴建伟
 * @Date 2018/9/4 17:52
 */
public class ServerHeartbeatHandler extends ChannelDuplexHandler {
    // 心跳丢失计数器
    private int counter;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            if (counter >= 3) {
                // 连续丢失3个心跳包 (断开连接)
                ctx.channel().close().sync();
            } else {
                counter++;
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        counter = 0;
        ctx.fireChannelRead(msg);
    }
}
