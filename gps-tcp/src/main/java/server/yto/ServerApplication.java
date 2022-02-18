package server.yto;

import io.netty.util.ResourceLeakDetector;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import server.yto.listener.NettyServerListener;

import javax.annotation.Resource;


@SpringBootApplication
public class ServerApplication implements CommandLineRunner {
    @Resource
    private NettyServerListener nettyServerListener;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        /********************************内存泄漏定位配置的参数 勿删!!!!!****************************************/
//        System.setProperty("io.netty.leakDetection.maxRecords", "200");
//        System.setProperty("io.netty.leakDetection.acquireAndReleaseOnly", "true");
//        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
        nettyServerListener.start();
    }


}
