package com.shth.das.business;

import com.shth.das.pojo.otcv2.OtcV2Cpve500ProcessIssue;
import com.shth.das.util.CommonUtils;

/**
 * @description: OTC2.0协议拼接处理
 * @author: Shan Long
 * @create: 2021-09-01
 */
public class OtcV2ProtocolJoint {

    /**
     * OTC2.0（CPVE500）工艺下发协议拼接
     *
     * @param processIssue OTC2.0（CPVE500）工艺实体类
     * @return 16进制字符串
     */
    public static String otcV2Cpve500ProcessIssue(OtcV2Cpve500ProcessIssue processIssue) {
        if (null != processIssue) {
            String head = OtcV2Cpve500ProcessIssue.HEAD;
            String order = OtcV2Cpve500ProcessIssue.ORDER;
            String foot = OtcV2Cpve500ProcessIssue.FOOT;
            String gatherNo = CommonUtils.lengthJoint(processIssue.getGatherNo(), 4);
            String channelNo = CommonUtils.lengthJoint(processIssue.getChannelNo(), 2);
            String spotWeldTime = CommonUtils.lengthJoint(processIssue.getSpotWeldTime().intValue(), 4);
            String preFlowTime = CommonUtils.lengthJoint(processIssue.getPreFlowTime().intValue(), 4);
            String initialEle = CommonUtils.lengthJoint(processIssue.getInitialEle().intValue(), 4);
            String initialVol = CommonUtils.lengthJoint(processIssue.getInitialVol().intValue(), 4);
            String initialVolUnitary = CommonUtils.lengthJoint(processIssue.getInitialVolUnitary().intValue(), 4);
            String weldElectricity = CommonUtils.lengthJoint(processIssue.getWeldElectricity().intValue(), 4);
            String weldVoltage = CommonUtils.lengthJoint(processIssue.getWeldVoltage().intValue(), 4);
            String weldVoltageUnitary = CommonUtils.lengthJoint(processIssue.getWeldVoltageUnitary().intValue(), 4);
            String extinguishArcEle = CommonUtils.lengthJoint(processIssue.getExtinguishArcEle().intValue(), 4);
            String extinguishArcVol = CommonUtils.lengthJoint(processIssue.getExtinguishArcVol().intValue(), 4);
            String extinguishArcVolUnitary = CommonUtils.lengthJoint(processIssue.getExtinguishArcVolUnitary().intValue(), 4);
            String hysteresisAspirated = CommonUtils.lengthJoint(processIssue.getHysteresisAspirated().intValue(), 4);
            String arcPeculiarity = CommonUtils.lengthJoint(processIssue.getArcPeculiarity().intValue(), 4);
            String gases = CommonUtils.lengthJoint(processIssue.getGases().intValue(), 2);
            String wireDiameter = CommonUtils.lengthJoint(processIssue.getWireDiameter().intValue(), 2);
            String wireMaterials = CommonUtils.lengthJoint(processIssue.getWireMaterials(), 2);
            String weldProcess = CommonUtils.lengthJoint(processIssue.getWeldProcess(), 2);
            String controlInfo = CommonUtils.lengthJoint(processIssue.getControlInfo(), 4);
            String weldEleAdjust = CommonUtils.lengthJoint(processIssue.getWeldEleAdjust().intValue(), 2);
            String weldVolAdjust = CommonUtils.lengthJoint(processIssue.getWeldVolAdjust().intValue(), 2);
            String extinguishArcEleAdjust = CommonUtils.lengthJoint(processIssue.getExtinguishArcEleAdjust().intValue(), 2);
            String extinguishArcVolAdjust = CommonUtils.lengthJoint(processIssue.getExtinguishArcVolAdjust().intValue(), 2);
            String pulseFrequency = CommonUtils.lengthJoint(processIssue.getPulseFrequency().intValue(), 4);
            String gasesFlow = CommonUtils.lengthJoint(processIssue.getGasesFlow().intValue(), 4);
            String weldWireCathodeRate = CommonUtils.lengthJoint(processIssue.getWeldWireCathodeRate().intValue(), 4);
            String reserved1 = "0000";
            String str = head + order + gatherNo + channelNo + spotWeldTime + preFlowTime + initialEle + initialVol + initialVolUnitary +
                    weldElectricity + weldVoltage + weldVoltageUnitary + extinguishArcEle + extinguishArcVol + extinguishArcVolUnitary +
                    hysteresisAspirated + arcPeculiarity + gases + wireDiameter + wireMaterials + weldProcess + controlInfo + weldEleAdjust +
                    weldVolAdjust + extinguishArcEleAdjust + extinguishArcVolAdjust + pulseFrequency + gasesFlow + weldWireCathodeRate +
                    reserved1 + foot;
            if (str.length() == 112) {
                return str.toUpperCase();
            }
        }
        return null;
    }
}
