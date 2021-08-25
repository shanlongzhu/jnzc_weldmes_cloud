package com.shth.das.mqtt;

import com.alibaba.fastjson.JSON;
import com.shth.das.business.JnOtcRtDataProtocol;
import com.shth.das.business.JnSxRtDataProtocol;
import com.shth.das.common.CommonMap;
import com.shth.das.common.CommonThreadPool;
import com.shth.das.common.DownTopicEnum;
import com.shth.das.pojo.db.TaskClaimIssue;
import com.shth.das.pojo.jnotc.JNCommandIssue;
import com.shth.das.pojo.jnotc.JNPasswordIssue;
import com.shth.das.pojo.jnotc.JNProcessClaim;
import com.shth.das.pojo.jnotc.JNProcessIssue;
import com.shth.das.pojo.jnsx.*;
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
            CommonThreadPool.THREAD_POOL_EXECUTOR.execute(() -> messageManage(topic, message));
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
    private void messageManage(String topic, String message) {
        try {
            //OTC工艺下发主题匹配下发规范模板进行解析
            if (DownTopicEnum.processIssue.name().equals(topic)) {
                //匹配下发规范模板
                List<JNProcessIssue> list = JSON.parseArray(message, JNProcessIssue.class);
                if (CommonUtils.isNotEmpty(list)) {
                    for (JNProcessIssue issue : list) {
                        String gatherNo = issue.getGatherNo();
                        //Java对象解析成16进制字符串
                        String str = JnOtcRtDataProtocol.jnIssueProtocol(issue);
                        otcChannelWrite(gatherNo, str, "----->OTC工艺下发成功", topic);
                    }
                }
            }
            //OTC工艺索取主题匹配索取规范模板解析
            if (DownTopicEnum.processClaim.name().equals(topic)) {
                JNProcessClaim jnProcessClaim = JSON.parseObject(message, JNProcessClaim.class);
                if (null != jnProcessClaim) {
                    String gatherNo = jnProcessClaim.getGatherNo();
                    //Java对象解析成16进制字符串
                    String str = JnOtcRtDataProtocol.jnClaimProtocol(jnProcessClaim);
                    otcChannelWrite(gatherNo, str, "----->OTC工艺索取成功", topic);
                }
            }
            //OTC密码下发
            if (DownTopicEnum.passwordIssue.name().equals(topic)) {
                JNPasswordIssue jnPasswordIssue = JSON.parseObject(message, JNPasswordIssue.class);
                if (null != jnPasswordIssue) {
                    String gatherNo = jnPasswordIssue.getGatherNo();
                    //java对象转16进制字符串
                    String str = JnOtcRtDataProtocol.jnPasswordProtocol(jnPasswordIssue);
                    otcChannelWrite(gatherNo, str, "----->OTC密码下发成功", topic);
                }
            }
            //OTC控制命令下发
            if (DownTopicEnum.commandIssue.name().equals(topic)) {
                JNCommandIssue jnCommandIssue = JSON.parseObject(message, JNCommandIssue.class);
                if (null != jnCommandIssue) {
                    String gatherNo = jnCommandIssue.getGatherNo();
                    //java对象转16进制字符串
                    String str = JnOtcRtDataProtocol.jnCommandProtocol(jnCommandIssue);
                    otcChannelWrite(gatherNo, str, "----->OTC控制命令下发成功", topic);
                }
            }
            //松下GL5系列CO2工艺下发
            if (DownTopicEnum.sxCo2ProcessIssue.name().equals(topic)) {
                SxCO2ProcessIssue sxCo2ProcessIssue = JSON.parseObject(message, SxCO2ProcessIssue.class);
                if (null != sxCo2ProcessIssue) {
                    final String weldCid = sxCo2ProcessIssue.getWeldCid();
                    String str = JnSxRtDataProtocol.sxCO2ProcessProtocol(sxCo2ProcessIssue);
                    sxChannelWrite(weldCid, str, "----->松下CO2工艺下发成功", topic);
                }
            }
            //松下GL5系列TIG工艺下发
            if (DownTopicEnum.sxTigProcessIssue.name().equals(topic)) {
                SxTIGProcessIssue sxTigProcessIssue = JSON.parseObject(message, SxTIGProcessIssue.class);
                if (null != sxTigProcessIssue) {
                    final String weldCid = sxTigProcessIssue.getWeldCid();
                    String str = JnSxRtDataProtocol.sxTigProcessProtocol(sxTigProcessIssue);
                    sxChannelWrite(weldCid, str, "----->松下TIG工艺下发成功", topic);
                }
            }
            //松下GL5系列焊机通道设定/读取
            if (DownTopicEnum.sxWeldChannelSet.name().equals(topic)) {
                SxWeldChannelSetting sxWeldChannelSetting = JSON.parseObject(message, SxWeldChannelSetting.class);
                if (null != sxWeldChannelSetting) {
                    final String weldCid = sxWeldChannelSetting.getWeldCid();
                    String str = JnSxRtDataProtocol.sxWeldChannelSetProtocol(sxWeldChannelSetting);
                    sxChannelWrite(weldCid, str, "----->松下焊机通道设定/读取成功", topic);
                }
            }
            //松下GL5系列工艺索取/删除
            if (DownTopicEnum.sxProcessClaim.name().equals(topic)) {
                SxProcessClaim sxProcessClaim = JSON.parseObject(message, SxProcessClaim.class);
                if (null != sxProcessClaim) {
                    final String weldCid = sxProcessClaim.getWeldCid();
                    String str = JnSxRtDataProtocol.sxProcessClaimProtocol(sxProcessClaim);
                    sxChannelWrite(weldCid, str, "----->松下工艺索取/删除成功", topic);
                }
            }
            //焊工刷卡领取任务
            if (DownTopicEnum.taskClaimIssue.name().equals(topic)) {
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
                                CommonMap.OTC_TASK_CLAIM_MAP.put(gatherNo, taskClaimIssue);
                            }
                        }
                        //否则就是结束任务
                        else {
                            String gatherNo = taskClaimIssue.getGatherNo();
                            if (CommonUtils.isNotEmpty(gatherNo)) {
                                gatherNo = Integer.valueOf(gatherNo).toString();
                                CommonMap.OTC_TASK_CLAIM_MAP.remove(gatherNo);
                            }
                        }
                    }
                    //否则就是松下设备
                    else {
                        //判断是开始任务还是结束任务（0：开始）
                        if (taskClaimIssue.getStartFlag() == 0) {
                            final String weldCid = taskClaimIssue.getWeldCid();
                            if (CommonUtils.isNotEmpty(weldCid)) {
                                //如果已经开始任务，则会将之前的任务顶掉
                                CommonMap.SX_TASK_CLAIM_MAP.put(weldCid, taskClaimIssue);
                            }
                        }
                        //否则就是结束任务
                        else {
                            final String weldCid = taskClaimIssue.getWeldCid();
                            if (CommonUtils.isNotEmpty(weldCid)) {
                                CommonMap.SX_TASK_CLAIM_MAP.remove(weldCid);
                            }
                        }
                    }
                }
            }
            //松下FR2系列通道参数查询/删除
            if (DownTopicEnum.sxChannelParamQuery.name().equals(topic)) {
                SxChannelParamQuery sxChannelParamQuery = JSON.parseObject(message, SxChannelParamQuery.class);
                if (null != sxChannelParamQuery) {
                    final String weldCid = sxChannelParamQuery.getWeldCid();
                    String str = JnSxRtDataProtocol.sxChannelParamQueryProtocol(sxChannelParamQuery);
                    sxChannelWrite(weldCid, str, "----->FR2系列通道参数查询/删除成功", topic);
                }
            }
            //松下松下FR2系列通道参数下载
            if (DownTopicEnum.sxChannelParamDownload.name().equals(topic)) {
                SxChannelParamReplyHave channelParamReplyHave = JSON.parseObject(message, SxChannelParamReplyHave.class);
                if (null != channelParamReplyHave) {
                    String weldCid = channelParamReplyHave.getWeldCid();
                    String str = JnSxRtDataProtocol.sxChannelParamReplyHaveProtocol(channelParamReplyHave);
                    sxChannelWrite(weldCid, str, "----->松下FR2系列通道参数下载成功", topic);
                }
            }
            //松下AT3系列参数下载
            if (DownTopicEnum.sxAt3ParamDownload.name().equals(topic)) {
                At3ParamDownload at3ParamDownload = JSON.parseObject(message, At3ParamDownload.class);
                if (null != at3ParamDownload) {
                    final String weldCid = at3ParamDownload.getWeldCid();
                    String str = JnSxRtDataProtocol.At3ParamDownloadProtocol(at3ParamDownload);
                    sxChannelWrite(weldCid, str, "----->松下AT3系列参数下载成功", topic);
                }
            }
        } catch (Exception e) {
            log.error("messageManage error:{}", e.getMessage());
        }
    }

    /**
     * 根据采集编号查找通道并写入数据
     *
     * @param gatherNo 采集编号
     * @param str      16进制字符串
     * @param msg      打印内容
     * @param topic    主题
     */
    private void otcChannelWrite(String gatherNo, String str, String msg, String topic) {
        try {
            if (CommonUtils.isNotEmpty(gatherNo) && CommonUtils.isNotEmpty(str)) {
                if (CommonMap.OTC_GATHER_NO_CTX_MAP.containsKey(gatherNo)) {
                    Channel channel = CommonMap.OTC_GATHER_NO_CTX_MAP.get(gatherNo).channel();
                    //判断该焊机通道是否打开、是否活跃、是否可写
                    if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                        channel.writeAndFlush(str).sync();
                        log.info("{}:{}", msg, topic);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("数据下行异常：{}", e.getMessage());
        }
    }

    /**
     * 根据设备IP查找通道并写入数据
     *
     * @param weldCid 设备CID
     * @param str     16进制字符串
     * @param msg     打印内容
     * @param topic   主题
     */
    private void sxChannelWrite(String weldCid, String str, String msg, String topic) {
        try {
            if (CommonUtils.isNotEmpty(weldCid) && CommonUtils.isNotEmpty(str)) {
                if (!CommonMap.SX_WELD_CID_CTX_MAP.isEmpty() && CommonMap.SX_WELD_CID_CTX_MAP.containsKey(weldCid)) {
                    final Channel channel = CommonMap.SX_WELD_CID_CTX_MAP.get(weldCid).channel();
                    //判断该焊机通道是否打开、是否活跃、是否可写
                    if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                        channel.writeAndFlush(str).sync();
                        log.info("{}:{}", msg, topic);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("数据下行异常：{}", e.getMessage());
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
