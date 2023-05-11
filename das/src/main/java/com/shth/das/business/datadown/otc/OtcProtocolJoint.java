package com.shth.das.business.datadown.otc;

import com.shth.das.pojo.jnotc.*;
import com.shth.das.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OtcProtocolJoint {

    /**
     * 下发规范字符串拼接
     *
     * @param model 下发规范实体类
     * @return 返回解析完成的16进制字符串
     */
    public String jnIssueProtocol(JNProcessIssue model) {
        if (null != model) {
            String head = JNProcessIssue.Flag.HEAD.value;
            String order = JNProcessIssue.Flag.ORDER.value;
            String foot = JNProcessIssue.Flag.FOOT.value;
            String gatherNo = CommonUtils.lengthJoint(model.getGatherNo(), 4);//采集编号：0001
            String channelNo = CommonUtils.lengthJoint(model.getChannelNo(), 2);//通道号：01
            String spotWeldTime = CommonUtils.lengthJoint(model.getSpotWeldTime().intValue(), 4);//点焊时间：30
            String preflowTime = CommonUtils.lengthJoint(model.getPreflowTime().intValue(), 4);//提前送气时间：1
            String initialEle = CommonUtils.lengthJoint(model.getInitialEle().intValue(), 4);//初期电流:100
            String initialVol = CommonUtils.lengthJoint(model.getInitialVol().intValue(), 4);//初期电压:190
            String initialVolUnitary = CommonUtils.lengthJoint(model.getInitialVolUnitary().intValue(), 4);//初期电压一元:0
            String weldElectricity = CommonUtils.lengthJoint(model.getWeldElectricity().intValue(), 4);//焊接电流:100
            String weldVoltage = CommonUtils.lengthJoint(model.getWeldVoltage().intValue(), 4);//焊接电压:190
            String weldVoltageUnitary = CommonUtils.lengthJoint(model.getWeldVoltageUnitary().intValue(), 4);//焊接电压一元:0
            String extinguishArcEle = CommonUtils.lengthJoint(model.getExtinguishArcEle().intValue(), 4);//收弧电流:100
            String extinguishArcVol = CommonUtils.lengthJoint(model.getExtinguishArcVol().intValue(), 4);//收弧电压:190
            String extinguishArcVolUnitary = CommonUtils.lengthJoint(model.getExtinguishArcVolUnitary().intValue(), 4);//收弧电压一元:0
            String hysteresisAspirated = CommonUtils.lengthJoint(model.getHysteresisAspirated().intValue(), 4);//滞后送气:1
            String arcPeculiarity = CommonUtils.lengthJoint(model.getArcPeculiarity().intValue(), 4);//电弧特性:0
            String gases = CommonUtils.lengthJoint(model.getGases().intValue(), 2);//气体:0
            String wireDiameter = CommonUtils.lengthJoint(model.getWireDiameter().intValue(), 2);//焊丝直径:12
            String wireMaterials = CommonUtils.lengthJoint(model.getWireMaterials(), 2);//焊丝材料:0
            String weldProcess = CommonUtils.lengthJoint(model.getWeldProcess(), 2);//焊接过程:0
            String controlInfo = CommonUtils.lengthJoint(model.getControlInfo(), 4);//控制信息:229
            String weldEleAdjust = CommonUtils.lengthJoint(model.getWeldEleAdjust().intValue(), 2);//焊接电流微调:0
            String weldVolAdjust = CommonUtils.lengthJoint(model.getWeldVolAdjust().intValue(), 2);//焊接电压微调:0
            String extinguishArcEleAdjust = CommonUtils.lengthJoint(model.getExtinguishArcEleAdjust().intValue(), 2);//收弧电流微调:0
            String extinguishArcVolAdjust = CommonUtils.lengthJoint(model.getExtinguishArcVolAdjust().intValue(), 2);//收弧电压微调:0
            String alarmsElectricityMax = CommonUtils.lengthJoint(model.getAlarmsElectricityMax().intValue(), 4);//报警电流上限:0
            String alarmsElectricityMin = CommonUtils.lengthJoint(model.getAlarmsElectricityMin().intValue(), 4);//报警电流下限:0
            String alarmsVoltageMax = CommonUtils.lengthJoint(model.getAlarmsVoltageMax().intValue(), 4);//报警电压上限:0
            String alarmsVoltageMin = CommonUtils.lengthJoint(model.getAlarmsVoltageMin().intValue(), 4);//报警电压下限:0
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(head).append(order).append(gatherNo).append(channelNo).append(spotWeldTime).append(preflowTime).append(initialEle).append(initialVol).append(initialVolUnitary).append(weldElectricity).append(weldVoltage).append(weldVoltageUnitary).append(extinguishArcEle).append(extinguishArcVol).append(extinguishArcVolUnitary).append(hysteresisAspirated).append(arcPeculiarity).append(gases).append(wireDiameter).append(wireMaterials).append(weldProcess).append(controlInfo).append(weldEleAdjust).append(weldVolAdjust).append(extinguishArcEleAdjust).append(extinguishArcVolAdjust).append(alarmsElectricityMax).append(alarmsElectricityMin).append(alarmsVoltageMax).append(alarmsVoltageMin);
            //String str = head + order + gatherNo + channelNo + spotWeldTime + preflowTime + initialEle + initialVol + initialVolUnitary + weldElectricity + weldVoltage + weldVoltageUnitary + extinguishArcEle + extinguishArcVol + extinguishArcVolUnitary + hysteresisAspirated + arcPeculiarity + gases + wireDiameter + wireMaterials + weldProcess + controlInfo + weldEleAdjust + weldVolAdjust + extinguishArcEleAdjust + extinguishArcVolAdjust + alarmsElectricityMax + alarmsElectricityMin + alarmsVoltageMax + alarmsVoltageMin + foot;
            //str = str.toUpperCase(); //字母全部转为大写
            return stringBuilder.toString().toUpperCase();
        }
        return null;
    }

    /**
     * 索取规范字符串拼接
     *
     * @param claimModel 索取规范实体类
     * @return 返回16进制字符串
     */
    public String jnClaimProtocol(JNProcessClaim claimModel) {
        if (null != claimModel) {
            String head = JNProcessClaim.Flag.HEAD.value;
            String foot = JNProcessClaim.Flag.FOOT.value;
            String order = JNProcessClaim.Flag.ORDER.value;
            //采集编号：0001
            String gatherNo = CommonUtils.lengthJoint(claimModel.getGatherNo(), 4);
            //通道号：01
            String channelNo = CommonUtils.lengthJoint(claimModel.getChannelNo(), 2);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(head).append(order).append(gatherNo).append(channelNo).append(foot);
//            String str = head + order + gatherNo + channelNo + foot;
//            str = str.toUpperCase();
            return stringBuilder.toString().toUpperCase();
        }
        return null;
    }

    /**
     * 密码下发字符串拼接
     *
     * @param jnPasswordIssue 密码下发实体类
     * @return 16进制字符串
     */
    public String jnPasswordProtocol(JNPasswordIssue jnPasswordIssue) {
        if (null != jnPasswordIssue) {
            String head = JNPasswordIssue.Flag.HEAD.value;
            String order = JNPasswordIssue.Flag.ORDER.value;
            String foot = JNPasswordIssue.Flag.FOOT.value;
            String gatherNo = CommonUtils.lengthJoint(jnPasswordIssue.getGatherNo(), 4);
            String password = CommonUtils.lengthJoint(jnPasswordIssue.getPassword(), 4);
//            String str = head + order + gatherNo + password + foot;
//            str = str.toUpperCase();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(head).append(order).append(gatherNo).append(password).append(foot);
            return stringBuilder.toString().toUpperCase();
        }
        return null;
    }

    /**
     * 控制命令下发字符串拼接
     *
     * @param jnCommandIssue 控制命令下发实体类
     * @return 16进制字符串
     */
    public String jnCommandProtocol(JNCommandIssue jnCommandIssue) {
        if (null != jnCommandIssue) {
            String head = JNCommandIssue.Flag.HEAD.value;
            String order = JNCommandIssue.Flag.ORDER.value;
            String foot = JNCommandIssue.Flag.FOOT.value;
            String gatherNo = CommonUtils.lengthJoint(jnCommandIssue.getGatherNo(), 4);
            String command = CommonUtils.lengthJoint(jnCommandIssue.getCommand(), 2);
//            String str = head + order + gatherNo + command + foot;
//            str = str.toUpperCase();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(head).append(order).append(gatherNo).append(command).append(foot);
            return stringBuilder.toString().toUpperCase();
        }
        return null;
    }

    /**
     * OTC程序包路径下发
     *
     * @param programPath OtcV1IssueProgramPath
     * @return String
     */
    public String otcV1IssueProgramPath(OtcV1IssueProgramPath programPath) {
        if (null != programPath) {
            try {
                String head = "007E3C01010111";
                String gatherNo = CommonUtils.lengthJoint(programPath.getGatherNo(), 4);
                String port = CommonUtils.lengthJoint(programPath.getPort(), 4);
                //ASCII码转16进制
                String path = CommonUtils.convertStringToHex(programPath.getPackagePath());
                //长度拼接（后面拼接0）
                String packagePath = CommonUtils.backLengthJoint(path, 100);
                String foot = "007D";
                StringBuilder stringBuilder = new StringBuilder();
//                 String str = head + gatherNo + port + packagePath + foot;
                stringBuilder.append(head).append(gatherNo).append(port).append(packagePath).append(foot);
                if (stringBuilder.toString().length() == 126) {
                    return stringBuilder.toString().toUpperCase();
                }
            } catch (Exception e) {
                log.error("OTC程序包路径下发字符串拼接异常：", e);
            }
        }
        return null;
    }

}
