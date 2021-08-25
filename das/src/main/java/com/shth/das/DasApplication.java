package com.shth.das;

import com.shth.das.common.CommonFunction;
import com.shth.das.job.PowerBootJob;
import com.shth.das.mqtt.EmqMqttClient;
import com.shth.das.netty.NettyServer;
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
@EnableTransactionManagement // 开启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
public class DasApplication implements CommandLineRunner {

    @Autowired
    private EmqMqttClient emqMqttClient;
    @Autowired
    private NettyServer nettyServer;
    @Autowired
    private PowerBootJob powerBootJob;

    //启动时执行任务
    @Override
    public void run(String... args) {
        //emq客户端启动，连接服务端
        emqMqttClient.start();
        //判断OTC的ip和松下ip是否相同（true：同一个服务器启动）
        if (CommonFunction.getOtcIp().equals(CommonFunction.getSxIp())) {
            //true:同一个端口启动一次
            if (CommonFunction.getOtcPort() == CommonFunction.getSxPort()) {
                //OTC（松下）服务端启动
                nettyServer.start(CommonFunction.getOtcPort());
            }
            //false:端口不同则启动两个端口
            else {
                //OTC服务端启动
                nettyServer.start(CommonFunction.getOtcPort());
                //松下服务端启动
                nettyServer.start(CommonFunction.getSxPort());
            }
        } else {
            //OTC服务端启动
            nettyServer.start(CommonFunction.getOtcPort());
            //松下服务端启动
            nettyServer.start(CommonFunction.getSxPort());
        }
        //启动所有任务
        powerBootJob.startAllJob();
    }

    public static void main(String[] args) {
        SpringApplication.run(DasApplication.class, args);
    }

}
