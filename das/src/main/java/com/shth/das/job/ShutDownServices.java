package com.shth.das.job;

import com.shth.das.common.CommonThreadPool;
import com.shth.das.mqtt.EmqMqttClient;
import com.shth.das.netty.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Slf4j
@Component
public class ShutDownServices {

    @PreDestroy
    public void shutdownNettyServer() {
        //netty关闭
        NettyServer.closeNettyServer();
    }

    @PreDestroy
    public void shutdownMq() {
        log.info("mqtt客户端已关闭");
        //mq关闭
        EmqMqttClient.shutdownMqtt();
    }

    @PreDestroy
    public void shutdownPool() {
        int poolSize = CommonThreadPool.threadPoolExecutor().getPoolSize();
        int activeCount = CommonThreadPool.threadPoolExecutor().getActiveCount();
        log.info("线程池已关闭,当前线程数：{},活跃线程数：{}", poolSize, activeCount);
        //线程池关闭
        CommonThreadPool.threadPoolExecutor().shutdown();
    }

}
