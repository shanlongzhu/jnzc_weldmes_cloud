package com.shth.das.mqtt;

import com.shth.das.common.DownTopicEnum;
import com.shth.das.common.GainTopicName;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * mq客户端，连接emq服务器，订阅和发送消息
 */
@Component
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

    @PostConstruct
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
                    batchSubTopic();
                } else {
                    log.info("mqtt客户端启动失败");
                }
            }
        } catch (Exception e) {
            log.error("emq服务器未启动：{}", e.getMessage());
        }
    }

    /**
     * 主题批量订阅
     */
    private static void batchSubTopic() {
        //任务领取下发
        subTopic(GainTopicName.getMqttDownTopicName(DownTopicEnum.TaskClaimIssue));
        //OTC1.0工艺下发
        subTopic(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1ProcessIssue));
        //OTC1.0工艺索取
        subTopic(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1ProcessClaim));
        //OTC1.0密码下发
        subTopic(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1PasswordIssue));
        //OTC1.0控制命令下发
        subTopic(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1CommandIssue));
        //松下GL5系列CO2焊机工艺下发
        subTopic(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxGl5Co2ProcessIssue));
        //松下GL5系列TIG焊机工艺下发
        subTopic(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxGl5TigProcessIssue));
        //松下GL5系列【通道设定、通道读取】
        subTopic(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxGl5WeldChannelSet));
        //松下GL5系列【工艺索取、工艺删除】
        subTopic(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxGl5ProcessClaim));
        //松下【FR2、AT3】系列【通道参数查询、通道参数删除】
        subTopic(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxFr2ChannelParamQuery));
        //松下FR2系列【通道参数下载】
        subTopic(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxFr2ChannelParamDownload));
        //松下AT3系列【通道参数下载】
        subTopic(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxAt3ParamDownload));
        //OTC程序包路径下发
        subTopic(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1IssueProgramPath));
    }

    /**
     * emq主题和消息发布
     *
     * @param topic   主题
     * @param message 消息
     * @param qos     服务质量（0：至多一次，1：至少一次，2：只有一次）
     */
    public static void publishMessage(String topic, String message, int qos) {
        try {
            if (null != mqttClient && mqttClient.isConnected()) {
                MqttMessage mqttMessage = new MqttMessage();
                mqttMessage.setQos(qos);
                mqttMessage.setPayload(message.getBytes(StandardCharsets.UTF_8));
                //重连后不接受MQ服务的最新消息（只接收连接后的消息）
                mqttMessage.setRetained(false);
                mqttClient.publish(topic, mqttMessage);
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
                    batchSubTopic();
                } else {
                    mqttClient.disconnect();
                    mqttClient.close();
                    log.error("mqtt客户端重连失败");
                }
            } else {
                log.error("mqtt客户端连接为空,重连失败");
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
            log.error("MQTT客户端订阅主题异常：{}", e.getMessage());
        }
    }

    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   服务质量
     */
    public void subTopic(String topic, int qos) {
        try {
            if (null != mqttClient) {
                log.info("主题订阅成功：" + topic);
                mqttClient.subscribe(topic, qos);
            } else {
                reConnectMqtt();
                mqttClient.subscribe(topic, qos);
                log.info("重连后主题订阅成功：" + topic);
            }
        } catch (MqttException e) {
            log.error("MQTT客户端订阅某个主题异常：{}", e.getMessage());
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
            log.error("MQTT客户端清空主题异常：{}", e.getMessage());
        }
    }

    /**
     * 关闭客户端
     */
    public static void shutdownMqtt() {
        try {
            if (null != mqttClient && mqttClient.isConnected()) {
                mqttClient.disconnect();
                mqttClient.close();
            }
        } catch (Exception e) {
            log.error("MQTT客户端关闭异常：{}", e.getMessage());
        }
    }

}
