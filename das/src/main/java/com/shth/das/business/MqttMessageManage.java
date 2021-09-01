package com.shth.das.business;

import com.alibaba.druid.util.StringUtils;
import com.shth.das.codeparam.MqttParam;

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
        mqttMessageMap.put("taskClaimIssue", mqttMessageAnalysis::taskClaimIssue);
        //OTC1.0工艺下发
        mqttMessageMap.put("processIssue", mqttMessageAnalysis::otcV1ProcessIssue);
        //OTC1.0工艺索取
        mqttMessageMap.put("processClaim", mqttMessageAnalysis::otcV1ProcessClaim);
        //OTC1.0密码下发
        mqttMessageMap.put("passwordIssue", mqttMessageAnalysis::otcV1PasswordIssue);
        //OTC1.0控制命令下发
        mqttMessageMap.put("commandIssue", mqttMessageAnalysis::otcV1CommandIssue);
        //松下GL5系列CO2焊机工艺下发
        mqttMessageMap.put("sxGl5Co2ProcessIssue", mqttMessageAnalysis::sxGl5Co2ProcessIssue);
        //松下GL5系列TIG焊机工艺下发
        mqttMessageMap.put("sxGl5TigProcessIssue", mqttMessageAnalysis::sxGl5TigProcessIssue);
        //松下GL5系列TIG焊机工艺下发
        mqttMessageMap.put("sxGl5WeldChannelSet", mqttMessageAnalysis::sxGl5WeldChannelSet);
        //松下GL5系列工艺索取/删除
        mqttMessageMap.put("sxGl5ProcessClaim", mqttMessageAnalysis::sxGl5ProcessClaim);
        //松下FR2系列通道参数查询/删除
        mqttMessageMap.put("sxFr2ChannelParamQuery", mqttMessageAnalysis::sxFr2ChannelParamQuery);
        //松下FR2系列通道参数下载
        mqttMessageMap.put("sxFr2ChannelParamDownload", mqttMessageAnalysis::sxFr2ChannelParamDownload);
        //松下AT3系列参数下载
        mqttMessageMap.put("sxAt3ParamDownload", mqttMessageAnalysis::sxAt3ParamDownload);
        //OTC2.0（CPVE500）工艺下发
        mqttMessageMap.put("otcV2CPVE500ProcessIssue", mqttMessageAnalysis::otcV2Cpve500ProcessIssue);
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
