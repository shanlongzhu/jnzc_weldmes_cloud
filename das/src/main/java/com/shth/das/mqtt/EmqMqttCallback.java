package com.shth.das.mqtt;

import com.shth.das.business.MqttMessageManage;
import com.shth.das.common.CommonThreadPool;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

/**
 * mqtt消息回调
 */
@SuppressWarnings({"ALL", "AlibabaMethodTooLong"})
@Slf4j
public class EmqMqttCallback implements MqttCallback {

    /**
     * 连接丢失后，一般在这里面进行重连
     *
     * @param throwable
     */
    @SneakyThrows
    @Override
    public void connectionLost(Throwable throwable) {
        log.warn("连接丢失，正在重连..." + throwable.getMessage());
        Thread.sleep(2000);
        EmqMqttClient.reConnectMqtt();
    }

    /**
     * subscribe后得到的消息会执行到这里面
     *
     * @param topic       主题
     * @param mqttMessage 消息体
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        try {
            log.info("mqtt客户端收到消息主题：{} 消息内容：{}", topic, new String(mqttMessage.getPayload(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            String message = new String(mqttMessage.getPayload(),"UTF-8");
            CommonThreadPool.THREAD_POOL_EXECUTOR.execute(() -> new MqttMessageManage().mqttMessageManage(topic, message));
        } catch (Exception e) {
            log.error("MQTT客户端消息回调-数据接收处理异常：", e);
        }
    }

    /**
     * 消息发送成功时的回调
     *
     * @param iMqttDeliveryToken
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //log.info("消息发送成功：" + iMqttDeliveryToken.isComplete());
    }
}
