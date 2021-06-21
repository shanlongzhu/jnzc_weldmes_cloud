package com.shth.das.mqtt;

import com.shth.das.common.TopicEnum;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * mq客户端，连接emq服务器，订阅和发送消息
 */
@Component
@Configuration
@Slf4j
public class EmqMqttClient {

    private static MqttClient mqttClient;
    private static MqttConnectOptions mqttConnectOptions;

    @Value("${mqtt.host}")
    private String host;
    @Value("${mqtt.port}")
    private int port;
    @Value("${mqtt.clientId}")
    private String clientId;
    @Value("${mqtt.username}")
    private String username;
    @Value("${mqtt.password}")
    private String password;
    @Value("${mqtt.connectionTimeout}")
    private int connectionTimeout;
    @Value("${mqtt.keepAliveInterval}")
    private int keepAliveInterval;
    @Value("${mqtt.cleanSession}")
    private boolean cleanSession;
    @Value("${mqtt.automaticReconnect}")
    private boolean automaticReconnect;

    //连接到mqtt的连接参数配置
    public void getOptions() {
        mqttConnectOptions = new MqttConnectOptions();
        //断开后，是否自动连接
        mqttConnectOptions.setAutomaticReconnect(automaticReconnect);
        //连接状态不保存
        mqttConnectOptions.setCleanSession(cleanSession);
        //设置超时时间 单位为秒
        mqttConnectOptions.setConnectionTimeout(connectionTimeout);
        //设置会话心跳时间 单位为秒
        mqttConnectOptions.setKeepAliveInterval(keepAliveInterval);
        //账号
        mqttConnectOptions.setUserName(username);
        //密码
        mqttConnectOptions.setPassword(password.toCharArray());
    }

    public void start() {
        try {
            if (mqttClient == null) {
                getOptions();
                mqttClient = new MqttClient("tcp://" + host + ":" + port, clientId + System.currentTimeMillis(), new MemoryPersistence());
                //设置回调
                mqttClient.setCallback(new EmqMqttCallback());
                //mqtt连接
                mqttClient.connect(mqttConnectOptions);
                if (mqttClient.isConnected()) {
                    log.info("mqtt客户端启动成功");
                    subTopic(TopicEnum.processIssue.name()); //工艺下发
                    subTopic(TopicEnum.processClaim.name()); //工艺索取
                    subTopic(TopicEnum.passwordIssue.name()); //密码下发
                    subTopic(TopicEnum.commandIssue.name()); //控制命令下发
                } else {
                    log.info("mqtt客户端启动失败");
                }
            }
        } catch (Exception e) {
            log.error("emq服务器未启动：{}", e.getMessage());
        }
    }

    /**
     * emq主题和消息发布
     *
     * @param topic   主题
     * @param message 消息
     * @param qos     级别
     */
    public static void publishMessage(String topic, String message, int qos) {
        try {
            if (null != mqttClient && mqttClient.isConnected()) {
                MqttMessage mqttMessage = new MqttMessage();
                mqttMessage.setQos(qos);
                mqttMessage.setPayload(message.getBytes());
                mqttClient.publish(topic, mqttMessage);
                //log.info("消息发布成功：" + topic);
            } else {
                reConnectMqtt();
            }
        } catch (Exception e) {
            log.error("emq主题消息发布异常：{}", e.getMessage());
        }
    }

    /**
     * mqtt客户端重连
     */
    public static void reConnectMqtt() {
        try {
            if (null != mqttClient && null != mqttConnectOptions) {
                mqttClient.connect(mqttConnectOptions);
                if (mqttClient.isConnected()) {
                    log.info("mqtt客户端重连成功");
                } else {
                    log.info("mqtt客户端重连失败");
                }
            } else {
                log.info("mqtt客户端连接为空,重连失败");
            }
        } catch (Exception e) {
            log.error("mqtt客户端重连异常：{}", e.getMessage());
        }
    }

    /**
     * 订阅主题
     *
     * @param topic qos默认为0
     */
    public static void subTopic(String topic) {
        try {
            mqttClient.subscribe(topic, 0);
            log.info("主题订阅成功：" + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param gos   级别
     */
    public void subTopic(String topic, int gos) {
        try {
            if (null != mqttClient) {
                log.info("主题订阅成功：" + topic);
                mqttClient.subscribe(topic, gos);
            } else {
                reConnectMqtt();
                mqttClient.subscribe(topic, gos);
                log.info("主题订阅成功：" + topic);
            }
        } catch (MqttException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 清空主题
     *
     * @param topic 主题
     */
    public void cleanTopic(String topic) {
        try {
            if (null != mqttClient) {
                mqttClient.unsubscribe(topic);
                log.info("取消订阅成功：" + topic);
            } else {
                reConnectMqtt();
                mqttClient.unsubscribe(topic);
                log.info("取消订阅成功：" + topic);
            }
        } catch (MqttException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
