package com.shth.das;

import com.shth.das.job.RtDataJob;
import com.shth.das.mqtt.EmqMqttClient;
import com.shth.das.netty.NettyServer;
import com.shth.das.sys.rtdata.service.RtDataService;
import com.shth.das.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void run(String... args) {
        emqMqttClient.start();
        nettyServer.start();
        rtDataJob.start();
    }

    public static void main(String[] args) {
        SpringApplication.run(DasApplication.class, args);
    }

}
