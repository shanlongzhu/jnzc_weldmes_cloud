package com.shth.das.business;

import com.alibaba.druid.util.StringUtils;
import com.shth.das.codeparam.MqttParam;
import com.shth.das.common.DownTopicEnum;
import com.shth.das.common.GainTopicName;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @description: mqtt客户端收到消息后协议拼接和解析
 * @author: Shan Long
 * @create: 2021-09-01
 */
public class MqttMessageManage {

    /**
     * MAP<K,V>,K:String:主题（topic）
     * V:Consumer<String> 入参无返回，<String>：message消息内容
     */
    private final Map<String, Consumer<MqttParam>> mqttMessageMap = new HashMap<>();

    public MqttMessageManage() {
        MqttMessageAnalysis mqttMessageAnalysis = new MqttMessageAnalysis();
        //焊工刷卡领取任务
        mqttMessageMap.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.TaskClaimIssue), mqttMessageAnalysis::taskClaimIssue);
        //OTC（1.0）工艺下发
        mqttMessageMap.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1ProcessIssue), mqttMessageAnalysis::otcV1ProcessIssue);
        //OTC（1.0）工艺索取
        mqttMessageMap.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1ProcessClaim), mqttMessageAnalysis::otcV1ProcessClaim);
        //OTC（1.0）密码下发
        mqttMessageMap.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1PasswordIssue), mqttMessageAnalysis::otcV1PasswordIssue);
        //OTC（1.0）控制命令下发
        mqttMessageMap.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1CommandIssue), mqttMessageAnalysis::otcV1CommandIssue);
        //松下GL5系列CO2焊机工艺下发
        mqttMessageMap.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxGl5Co2ProcessIssue), mqttMessageAnalysis::sxGl5Co2ProcessIssue);
        //松下GL5系列TIG焊机工艺下发
        mqttMessageMap.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxGl5TigProcessIssue), mqttMessageAnalysis::sxGl5TigProcessIssue);
        //松下GL5系列【通道设定、通道读取】:106
        mqttMessageMap.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxGl5WeldChannelSet), mqttMessageAnalysis::sxGl5WeldChannelSet);
        //松下GL5系列【工艺索取、工艺删除】:106
        mqttMessageMap.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxGl5ProcessClaim), mqttMessageAnalysis::sxGl5ProcessClaim);
        //松下【FR2、AT3】系列【通道参数查询、通道参数删除】
        mqttMessageMap.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxFr2ChannelParamQuery), mqttMessageAnalysis::sxFr2ChannelParamQuery);
        //松下FR2系列【通道参数下载】
        mqttMessageMap.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxFr2ChannelParamDownload), mqttMessageAnalysis::sxFr2ChannelParamDownload);
        //松下AT3系列【通道参数下载】
        mqttMessageMap.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxAt3ParamDownload), mqttMessageAnalysis::sxAt3ParamDownload);
        //OTC程序包路径下发
        mqttMessageMap.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1IssueProgramPath), mqttMessageAnalysis::otcV1IssueProgramPath);
    }

    /**
     * 根据主题找方法解析消息体
     *
     * @param topic   主题
     * @param message 消息内容
     */
    public void mqttMessageManage(String topic, String message) {
        if (!StringUtils.isEmpty(topic) && !StringUtils.isEmpty(message)) {
            if (this.mqttMessageMap.containsKey(topic)) {
                MqttParam param = new MqttParam();
                param.setTopic(topic);
                param.setMessage(message);
                this.mqttMessageMap.get(topic).accept(param);
            }
        }
    }

}
