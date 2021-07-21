package com.shth.das;

import com.shth.das.job.RtDataJob;
import com.shth.das.mqtt.EmqMqttClient;
import com.shth.das.netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 系统启动类
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling //开启定时任务
@EnableTransactionManagement // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
public class DasApplication implements CommandLineRunner {

    @Autowired
    private EmqMqttClient emqMqttClient;
    @Autowired
    private NettyServer nettyServer;
    @Autowired
    private RtDataJob rtDataJob;

    @Value("${nettyServer.port}")
    private int otcPort;
    @Value("${sxNettyServer.port}")
    private int sxPort;

    //启动时执行任务
    @Override
    public void run(String... args) {
        //emq客户端启动，连接服务端
        emqMqttClient.start();
        //OTC服务端启动
        nettyServer.start(otcPort);
        //松下服务端启动
        nettyServer.start(sxPort);
        //创建OTC实时数据表
        rtDataJob.startJnOtcJob();
        //创建松下实时数据表
        rtDataJob.startSxJob();
    }

    public static void main(String[] args) {
        SpringApplication.run(DasApplication.class, args);
    }

}
