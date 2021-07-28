package com.shth.das;

import com.shth.das.common.DataInitialization;
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
@EnableTransactionManagement // 开启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
public class DasApplication implements CommandLineRunner {

    @Autowired
    private EmqMqttClient emqMqttClient;
    @Autowired
    private NettyServer nettyServer;
    @Autowired
    private RtDataJob rtDataJob;

    //启动时执行任务
    @Override
    public void run(String... args) {
        //emq客户端启动，连接服务端
        emqMqttClient.start();
        //判断OTC的ip和松下ip是否相同（true：同一个服务器启动）
        if (DataInitialization.getOtcIp().equals(DataInitialization.getSxIp())) {
            //true:同一个端口启动一次
            if (DataInitialization.getOtcPort() == DataInitialization.getSxPort()) {
                //OTC（松下）服务端启动
                nettyServer.start(DataInitialization.getOtcPort());
            }
            //false:端口不同则启动两个端口
            else {
                //OTC服务端启动
                nettyServer.start(DataInitialization.getOtcPort());
                //松下服务端启动
                nettyServer.start(DataInitialization.getSxPort());
            }
        } else {
            //OTC服务端启动
            nettyServer.start(DataInitialization.getOtcPort());
            //松下服务端启动
            nettyServer.start(DataInitialization.getSxPort());
        }
        //创建OTC实时数据表
        rtDataJob.startJnOtcJob();
        //创建松下实时数据表
        rtDataJob.startSxJob();
    }

    public static void main(String[] args) {
        SpringApplication.run(DasApplication.class, args);
    }

}
