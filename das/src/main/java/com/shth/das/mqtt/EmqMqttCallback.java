package com.shth.das.mqtt;

import com.alibaba.fastjson.JSON;
import com.shth.das.business.JNRtDataProtocol;
import com.shth.das.common.CommonDbData;
import com.shth.das.common.TopicEnum;
import com.shth.das.netty.NettyServerHandler;
import com.shth.das.pojo.JNCommandIssue;
import com.shth.das.pojo.JNPasswordIssue;
import com.shth.das.pojo.JNProcessClaim;
import com.shth.das.pojo.JNProcessIssue;
import com.shth.das.util.CommonUtils;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * mqtt消息回调
 */
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
        log.info("连接丢失，正在重连...");
        EmqMqttClient.reConnectMqtt();
    }

    /**
     * subscribe后得到的消息会执行到这里面
     * message 格式：[{command:'52',gatherNo:'0005'},{command:'52',gatherNo:'0006'}]
     *
     * @param topic       主题
     * @param mqttMessage 消息体
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        log.info("mqtt客户端收到消息主题：{} 消息内容：{}", topic, new String(mqttMessage.getPayload()));
        try {
            String message = new String(mqttMessage.getPayload());
            CommonDbData.executor.execute(new Runnable() {
                @Override
                public void run() {
                    messageManage(topic, message);
                }
            });
        } catch (Exception e) {
            log.error("messageArrived Exception:{}", e.getMessage());
        }
    }

    /**
     * mqtt客户端消息处理
     *
     * @param topic   主题
     * @param message 消息内容
     */
    public void messageManage(String topic, String message) {
        try {
            //工艺下发主题匹配下发规范模板进行解析
            if (TopicEnum.processIssue.name().equals(topic)) {
                //匹配下发规范模板
                List<JNProcessIssue> list = JSON.parseArray(message, JNProcessIssue.class);
                if (CommonUtils.isNotEmpty(list)) {
                    for (JNProcessIssue issue : list) {
                        String gatherNo = issue.getGatherNo();
                        //Java对象解析成16进制字符串
                        String str = JNRtDataProtocol.jnIssueProtocol(issue);
                        String clientIp = getClientIpByGatherNo(gatherNo);
                        if (CommonUtils.isNotEmpty(str) && CommonUtils.isNotEmpty(clientIp) && NettyServerHandler.MAP.size() > 0 && NettyServerHandler.MAP.containsKey(clientIp)) {
                            Channel channel = NettyServerHandler.MAP.get(clientIp).channel();
                            //判断该焊机通道是否打开、是否活跃、是否可写
                            if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                                channel.writeAndFlush(str).sync();
                                log.info("-----工艺下发成功:{}", topic);
                            }
                        }
                    }
                }
            }
            //工艺索取主题匹配索取规范模板解析
            if (TopicEnum.processClaim.name().equals(topic)) {
                JNProcessClaim jnProcessClaim = JSON.parseObject(message, JNProcessClaim.class);
                if (null != jnProcessClaim) {
                    String gatherNo = jnProcessClaim.getGatherNo();
                    //Java对象解析成16进制字符串
                    String str = JNRtDataProtocol.jnClaimProtocol(jnProcessClaim);
                    String clientIp = getClientIpByGatherNo(gatherNo);
                    if (CommonUtils.isNotEmpty(str) && CommonUtils.isNotEmpty(clientIp) && NettyServerHandler.MAP.size() > 0 && NettyServerHandler.MAP.containsKey(clientIp)) {
                        Channel channel = NettyServerHandler.MAP.get(clientIp).channel();
                        //判断该焊机通道是否打开、是否活跃、是否可写
                        if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                            channel.writeAndFlush(str).sync();
                            log.info("-----工艺索取成功:{}", topic);
                        }
                    }
                }
            }
            //密码下发
            if (TopicEnum.passwordIssue.name().equals(topic)) {
                JNPasswordIssue jnPasswordIssue = JSON.parseObject(message, JNPasswordIssue.class);
                if (null != jnPasswordIssue) {
                    String gatherNo = jnPasswordIssue.getGatherNo();
                    //java对象转16进制字符串
                    String str = JNRtDataProtocol.jnPasswordProtocol(jnPasswordIssue);
                    String clientIp = getClientIpByGatherNo(gatherNo);
                    if (CommonUtils.isNotEmpty(str) && CommonUtils.isNotEmpty(clientIp) && NettyServerHandler.MAP.size() > 0 && NettyServerHandler.MAP.containsKey(clientIp)) {
                        Channel channel = NettyServerHandler.MAP.get(clientIp).channel();
                        //判断该焊机通道是否打开、是否活跃、是否可写
                        if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                            channel.writeAndFlush(str).sync();
                            log.info("-----密码下发成功:{}", topic);
                        }
                    }
                }
            }
            //控制命令下发
            if (TopicEnum.commandIssue.name().equals(topic)) {
                JNCommandIssue jnCommandIssue = JSON.parseObject(message, JNCommandIssue.class);
                if (null != jnCommandIssue) {
                    String gatherNo = jnCommandIssue.getGatherNo();
                    //java对象转16进制字符串
                    String str = JNRtDataProtocol.jnCommandProtocol(jnCommandIssue);
                    String clientIp = getClientIpByGatherNo(gatherNo);
                    if (CommonUtils.isNotEmpty(str) && CommonUtils.isNotEmpty(clientIp) && NettyServerHandler.MAP.size() > 0 && NettyServerHandler.MAP.containsKey(clientIp)) {
                        Channel channel = NettyServerHandler.MAP.get(clientIp).channel();
                        //判断该焊机通道是否打开、是否活跃、是否可写
                        if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                            channel.writeAndFlush(str).sync();
                            log.info("-----控制命令下发成功:{}", topic);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("messageManage error:{}", e.getMessage());
        }
    }

    /**
     * 根据采集编号查找采集盒IP地址
     *
     * @param gatherNo 采集编号
     * @return 返回采集盒IP地址
     */
    public static String getClientIpByGatherNo(String gatherNo) {
        String clientIp = "";
        if (CommonUtils.isNotEmpty(gatherNo)) {
            synchronized (NettyServerHandler.gatherAndIpMap) {
                Set<Map.Entry<String, String>> entries = NettyServerHandler.gatherAndIpMap.entrySet();
                //循环查找value（采集编号）的key（IP地址）
                for (Map.Entry<String, String> entry : entries) {
                    String gatherno = entry.getValue(); //采集编号
                    if (Integer.valueOf(gatherNo).equals(Integer.valueOf(gatherno))) {
                        clientIp = entry.getKey(); //采集盒IP地址
                        break;
                    }
                }
            }
        }
        return clientIp;
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
