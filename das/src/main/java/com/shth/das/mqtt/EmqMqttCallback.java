package com.shth.das.mqtt;

import com.alibaba.fastjson.JSON;
import com.shth.das.business.JnRtDataProtocol;
import com.shth.das.business.SxRtDataProtocol;
import com.shth.das.common.CommonDbData;
import com.shth.das.common.TopicEnum;
import com.shth.das.netty.NettyServerHandler;
import com.shth.das.pojo.db.TaskClaimIssue;
import com.shth.das.pojo.jnotc.JNCommandIssue;
import com.shth.das.pojo.jnotc.JNPasswordIssue;
import com.shth.das.pojo.jnotc.JNProcessClaim;
import com.shth.das.pojo.jnotc.JNProcessIssue;
import com.shth.das.pojo.jnsx.SxCO2ProcessIssue;
import com.shth.das.pojo.jnsx.SxProcessClaim;
import com.shth.das.pojo.jnsx.SxTIGProcessIssue;
import com.shth.das.pojo.jnsx.SxWeldChannelSetting;
import com.shth.das.util.CommonUtils;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

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
        log.info("连接丢失，正在重连..." + throwable.getMessage());
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
        log.info("mqtt客户端收到消息主题：{} 消息内容：{}", topic, new String(mqttMessage.getPayload()));
        try {
            String message = new String(mqttMessage.getPayload());
            CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> messageManage(topic, message));
        } catch (Exception e) {
            log.error("messageArrived Exception:{}", e.getMessage());
        }
    }

    /**
     * mqtt客户端消息处理（下行）
     *
     * @param topic   主题
     * @param message 消息内容
     */
    @SuppressWarnings("AlibabaMethodTooLong")
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
                        String str = JnRtDataProtocol.jnIssueProtocol(issue);
                        String clientIp = JnRtDataProtocol.getClientIpByGatherNo(gatherNo);
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
                    String str = JnRtDataProtocol.jnClaimProtocol(jnProcessClaim);
                    String clientIp = JnRtDataProtocol.getClientIpByGatherNo(gatherNo);
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
                    String str = JnRtDataProtocol.jnPasswordProtocol(jnPasswordIssue);
                    String clientIp = JnRtDataProtocol.getClientIpByGatherNo(gatherNo);
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
                    String str = JnRtDataProtocol.jnCommandProtocol(jnCommandIssue);
                    String clientIp = JnRtDataProtocol.getClientIpByGatherNo(gatherNo);
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
            //松下CO2工艺下发
            if (TopicEnum.sxCo2ProcessIssue.name().equals(topic)) {
                SxCO2ProcessIssue sxCo2ProcessIssue = JSON.parseObject(message, SxCO2ProcessIssue.class);
                if (null != sxCo2ProcessIssue) {
                    String weldIp = sxCo2ProcessIssue.getWeldIp();
                    String str = SxRtDataProtocol.sxCO2ProcessProtocol(sxCo2ProcessIssue);
                    if (CommonUtils.isNotEmpty(weldIp) && CommonUtils.isNotEmpty(str)) {
                        if (NettyServerHandler.MAP.size() > 0 && NettyServerHandler.MAP.containsKey(weldIp)) {
                            Channel channel = NettyServerHandler.MAP.get(weldIp).channel();
                            //判断该焊机通道是否打开、是否活跃、是否可写
                            if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                                channel.writeAndFlush(str).sync();
                                log.info("-----松下CO2工艺下发成功:{}", topic);
                            }
                        }
                    }
                }
            }
            //松下TIG工艺下发
            if (TopicEnum.sxTigProcessIssue.name().equals(topic)) {
                SxTIGProcessIssue sxTigProcessIssue = JSON.parseObject(message, SxTIGProcessIssue.class);
                if (null != sxTigProcessIssue) {
                    String weldIp = sxTigProcessIssue.getWeldIp();
                    String str = SxRtDataProtocol.sxTigProcessProtocol(sxTigProcessIssue);
                    if (CommonUtils.isNotEmpty(weldIp) && CommonUtils.isNotEmpty(str)) {
                        if (NettyServerHandler.MAP.size() > 0 && NettyServerHandler.MAP.containsKey(weldIp)) {
                            Channel channel = NettyServerHandler.MAP.get(weldIp).channel();
                            //判断该焊机通道是否打开、是否活跃、是否可写
                            if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                                channel.writeAndFlush(str).sync();
                                log.info("-----松下TIG工艺下发成功:{}", topic);
                            }
                        }
                    }
                }
            }
            //松下焊机通道设定/读取
            if (TopicEnum.sxWeldChannelSet.name().equals(topic)) {
                SxWeldChannelSetting sxWeldChannelSetting = JSON.parseObject(message, SxWeldChannelSetting.class);
                if (null != sxWeldChannelSetting) {
                    String weldIp = sxWeldChannelSetting.getWeldIp();
                    String str = SxRtDataProtocol.sxWeldChannelSetProtocol(sxWeldChannelSetting);
                    if (CommonUtils.isNotEmpty(weldIp) && CommonUtils.isNotEmpty(str)) {
                        if (NettyServerHandler.MAP.size() > 0 && NettyServerHandler.MAP.containsKey(weldIp)) {
                            Channel channel = NettyServerHandler.MAP.get(weldIp).channel();
                            //判断该焊机通道是否打开、是否活跃、是否可写
                            if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                                channel.writeAndFlush(str).sync();
                                System.out.println("读取：------" + str);
                                log.info("-----松下焊机通道设定/读取成功:{}", topic);
                            }
                        }
                    }
                }
            }
            //松下工艺索取/删除
            if (TopicEnum.sxProcessClaim.name().equals(topic)) {
                SxProcessClaim sxProcessClaim = JSON.parseObject(message, SxProcessClaim.class);
                if (null != sxProcessClaim) {
                    String weldIp = sxProcessClaim.getWeldIp();
                    String str = SxRtDataProtocol.sxProcessClaimProtocol(sxProcessClaim);
                    if (CommonUtils.isNotEmpty(weldIp) && CommonUtils.isNotEmpty(str)) {
                        if (NettyServerHandler.MAP.size() > 0 && NettyServerHandler.MAP.containsKey(weldIp)) {
                            Channel channel = NettyServerHandler.MAP.get(weldIp).channel();
                            //判断该焊机通道是否打开、是否活跃、是否可写
                            if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                                channel.writeAndFlush(str).sync();
                                log.info("-----松下工艺索取/删除成功:{}", topic);
                            }
                        }
                    }
                }
            }
            //焊工刷卡领取任务
            if (TopicEnum.taskClaimIssue.name().equals(topic)) {
                TaskClaimIssue taskClaimIssue = JSON.parseObject(message, TaskClaimIssue.class);
                if (null != taskClaimIssue) {
                    //如果是OTC设备
                    if (taskClaimIssue.getWeldType() == 0) {
                        //判断是开始任务还是结束任务（0：开始）
                        if (taskClaimIssue.getStartFlag() == 0) {
                            String gatherNo = taskClaimIssue.getGatherNo();
                            if (CommonUtils.isNotEmpty(gatherNo)) {
                                gatherNo = Integer.valueOf(gatherNo).toString();
                                //如果已经开始任务，则会将之前的任务顶掉
                                CommonDbData.OTC_TASK_CLAIM_MAP.put(gatherNo, taskClaimIssue);
                            }
                        }
                        //否则就是结束任务
                        else {
                            String gatherNo = taskClaimIssue.getGatherNo();
                            if (CommonUtils.isNotEmpty(gatherNo)) {
                                gatherNo = Integer.valueOf(gatherNo).toString();
                                CommonDbData.OTC_TASK_CLAIM_MAP.remove(gatherNo);
                            }
                        }
                    }
                    //否则就是松下设备
                    else {
                        //判断是开始任务还是结束任务（0：开始）
                        if (taskClaimIssue.getStartFlag() == 0) {
                            String weldIp = taskClaimIssue.getWeldIp();
                            if (CommonUtils.isNotEmpty(weldIp)) {
                                //如果已经开始任务，则会将之前的任务顶掉
                                CommonDbData.SX_TASK_CLAIM_MAP.put(weldIp, taskClaimIssue);
                            }
                        }
                        //否则就是结束任务
                        else {
                            String weldIp = taskClaimIssue.getWeldIp();
                            if (CommonUtils.isNotEmpty(weldIp)) {
                                CommonDbData.SX_TASK_CLAIM_MAP.remove(weldIp);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("messageManage error:{}", e.getMessage());
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
