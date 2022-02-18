package server.yto.listener;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import server.yto.config.NettyServerConfig;
import server.yto.handler.Decoder4LoggingOnly;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * Netty服务器监听器
 */
@Component
@Slf4j
public class NettyServerListener implements ApplicationContextAware {
    private ApplicationContext springContext;
    /**
     * 创建bootstrap
     */
    private ServerBootstrap serverBootstrap = new ServerBootstrap();
    /**
     * BOSS
     */
    private EventLoopGroup boss = new NioEventLoopGroup();
    /**
     * Worker
     */
    private EventLoopGroup work = new NioEventLoopGroup();
    /**
     * NETT服务器配置类
     */
    @Resource
    private NettyServerConfig nettyConfig;

    /**
     * 关闭服务器方法
     */
    @PreDestroy
    public void close() {
        log.info("关闭服务器....");
        //优雅退出
        boss.shutdownGracefully();
        work.shutdownGracefully();
    }

    /**
     * 开启及服务线程
     */
    public void start() {
        // 从配置文件中(application.yml)获取服务端监听端口号
        String host = nettyConfig.getHost();
        int port = nettyConfig.getPort();
        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO));

        try {
            //设置事件处理
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(nettyConfig.getThreshold(), 0, 0, TimeUnit.SECONDS));
//                    ch.pipeline().addLast(new MessageDecodeLength());
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 1, 2, -3, 0));
                    ch.pipeline().addLast(new Decoder4LoggingOnly());
                    ch.pipeline().addLast((ChannelHandler) springContext.getBean("processDataHandler"));
                }
            });
            log.info("netty服务器在[{}]端口启动监听", port);
            ChannelFuture f = serverBootstrap.bind(host, port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.info("[出现异常] 释放资源");
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springContext = applicationContext;
    }
}
