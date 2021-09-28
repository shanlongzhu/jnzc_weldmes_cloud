package com.shth.das;

import com.shth.das.common.CommonFunction;
import com.shth.das.job.PowerBootJob;
import com.shth.das.mqtt.EmqMqttClient;
import com.shth.das.netty.NettyServer;
import com.shth.das.processdb.DBCreateConnect;
import com.shth.das.processdb.DBCreateMethod;
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
    @Autowired
    private DBCreateConnect dbCreateConnect;
    @Autowired
    private DBCreateMethod dbCreateMethod;

    //启动时执行任务
    @Override
    public void run(String... args) {
        //emq客户端启动，连接服务端
        emqMqttClient.start();
        //启动所有任务
        powerBootJob.startAllJob();
        //判断是否启用OTC业务功能
        if (CommonFunction.isEnableOtcFunction()) {
            //OTC（松下）服务端启动
            nettyServer.start(CommonFunction.getOtcPort());
        }
        //判断是否启用ProcessDB实时数据库功能
        if (CommonFunction.isEnableProcessDB()) {
            //创建Processdb连接
            dbCreateConnect.start();
            //创建Processdb的库，表
            dbCreateMethod.addDbaseTablePoint();
        }
        //判断是否启用松下业务功能
        if (CommonFunction.isEnableSxFunction()) {
            if (CommonFunction.getOtcPort() != CommonFunction.getSxPort()) {
                //松下服务端启动
                nettyServer.start(CommonFunction.getSxPort());
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(DasApplication.class, args);
    }

}
