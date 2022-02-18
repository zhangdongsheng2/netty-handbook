package server.yto.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Netty服务器配置信息
 */
@Component
@ConfigurationProperties(prefix = "netty")
@Data
public class NettyServerConfig {
    /**
     * ip
     */
    private String host;
    /**
     * 端口
     */
    private int port;
    /**
     * 断开阀值（单位秒）在阀值时间内没有收到任何信息（包含心跳）则断开与客户端的连接
     */
    private int threshold;
    /**
     * 最大数据包长度
     */
    private int maxFrameLength;
}
