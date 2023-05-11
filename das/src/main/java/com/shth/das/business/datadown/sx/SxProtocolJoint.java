package com.shth.das.business.datadown.sx;

import com.shth.das.codeparam.SxVerificationCode;
import com.shth.das.pojo.jnsx.*;
import com.shth.das.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class SxProtocolJoint {

    /**
     * 松下CO2焊机工艺下发参数拼接
     *
     * @param sxCO2ProcessIssue 松下CO2焊机工艺下发实体类
     * @return 16进制字符串
     */
    public String sxCO2ProcessProtocol(SxCO2ProcessIssue sxCO2ProcessIssue) {
        if (null != sxCO2ProcessIssue) {
            try {
                //头部固定参数
                String head = SxVerificationCode.SX_CO2_PROCESS_ISSUE_HEAD;
                LocalDateTime localDateTime = LocalDateTime.now();
                String year = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("yy")), 2);
                String month = CommonUtils.lengthJoint(localDateTime.getMonthValue(), 2);
                String day = CommonUtils.lengthJoint(localDateTime.getDayOfMonth(), 2);
                String hour = CommonUtils.lengthJoint(localDateTime.getHour(), 2);
                String minute = CommonUtils.lengthJoint(localDateTime.getMinute(), 2);
                String second = CommonUtils.lengthJoint(localDateTime.getSecond(), 2);
                String millisecond = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("SSS")), 4);
                String datetime = year + month + day + hour + minute + second + millisecond;
                String readWriteFlag = "00000000020000";
                String channelNo = CommonUtils.lengthJoint(sxCO2ProcessIssue.getChannelNo(), 2);
                String dataFlag = "0100";
                //预置参数
                String initialEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getInitialEleMax().intValue(), 4);
                String initialEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getInitialEleMin().intValue(), 4);
                String initialVolMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getInitialVolMax().intValue(), 4);
                String initialVolMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getInitialVolMin().intValue(), 4);
                String initialWireSpeedMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getInitialWireSpeedMax().intValue(), 4);
                String initialWireSpeedMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getInitialWireSpeedMin().intValue(), 4);
                String weldEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldEleMax().intValue(), 4);
                String weldEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldEleMin().intValue(), 4);
                String weldVolMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldVolMax().intValue(), 4);
                String weldVolMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldVolMin().intValue(), 4);
                String weldWireSpeedMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldWireSpeedMax().intValue(), 4);
                String weldWireSpeedMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldWireSpeedMin().intValue(), 4);
                String arcEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcEleMax().intValue(), 4);
                String arcEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcEleMin().intValue(), 4);
                String arcVolMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcVolMax().intValue(), 4);
                String arcVolMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcVolMin().intValue(), 4);
                String arcWireSpeedMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcWireSpeedMax().intValue(), 4);
                String arcWireSpeedMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcWireSpeedMin().intValue(), 4);
                String reserved1 = "00000000000000000000";
                //焊接条件
                String modeSelect = CommonUtils.lengthJoint(sxCO2ProcessIssue.getModeSelect(), 2);
                String weldingControl = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldingControl(), 2);
                String weldingManner = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldingManner(), 2);
                String texture = CommonUtils.lengthJoint(sxCO2ProcessIssue.getTexture(), 2);
                String wireDiameter = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWireDiameter(), 2);
                String gases = CommonUtils.lengthJoint(sxCO2ProcessIssue.getGases(), 2);
                String wireFeed = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWireFeed(), 2);
                String checkGasStatus = CommonUtils.lengthJoint(sxCO2ProcessIssue.getCheckGasStatus(), 2);
                String cutStatus = CommonUtils.lengthJoint(sxCO2ProcessIssue.getCutStatus(), 2);
                String lockStatus = CommonUtils.lengthJoint(sxCO2ProcessIssue.getLockStatus(), 2);
                String eleShowSelect = CommonUtils.lengthJoint(sxCO2ProcessIssue.getEleShowSelect(), 2);
                String rev = CommonUtils.lengthJoint(sxCO2ProcessIssue.getRev().intValue(), 2);
                String boardThickness = CommonUtils.lengthJoint(sxCO2ProcessIssue.getBoardThickness().intValue(), 2);
                String spotWeldingTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getSpotWeldingTime().intValue(), 4);
                String volShowSelect = CommonUtils.lengthJoint(sxCO2ProcessIssue.getVolShowSelect(), 2);
                String arcLength = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcLength().intValue(), 2);
                String arcCharacter = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcCharacter(), 2);
                String penetrationControl = CommonUtils.lengthJoint(sxCO2ProcessIssue.getPenetrationControl(), 2);
                String beforeAspiratedTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getBeforeAspiratedTime().intValue(), 4);
                String afterStopGasTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getAfterStopGasTime().intValue(), 4);
                String unitaryDifference = CommonUtils.lengthJoint(sxCO2ProcessIssue.getUnitaryDifference(), 2);
                String nowChannel = CommonUtils.lengthJoint(sxCO2ProcessIssue.getNowChannel(), 2);
                String reserved2 = "00000000000000000000";
                //动态限流参数
                String dclInitialEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getDclInitialEleMax().intValue(), 4);
                String dclInitialEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getDclInitialEleMin().intValue(), 4);
                String dclWeldEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getDclWeldEleMax().intValue(), 4);
                String dclWeldEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getDclWeldEleMin().intValue(), 4);
                String dclArcEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getDclArcEleMax().intValue(), 4);
                String dclArcEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getDclArcEleMin().intValue(), 4);
                String startDelayTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getStartDelayTime().intValue(), 2);
                String clAmendPeriod = CommonUtils.lengthJoint(sxCO2ProcessIssue.getClAmendPeriod().intValue(), 2);
                String reserved3 = "00000000000000000000";
                //超限报警参数
                String oaInitialEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaInitialEleMax().intValue(), 4);
                String oaInitialEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaInitialEleMin().intValue(), 4);
                String oaInitialVolMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaInitialVolMax().intValue(), 4);
                String oaInitialVolMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaInitialVolMin().intValue(), 4);
                String oaWeldEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaWeldEleMax().intValue(), 4);
                String oaWeldEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaWeldEleMin().intValue(), 4);
                String oaWeldVolMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaWeldVolMax().intValue(), 4);
                String oaWeldVolMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaWeldVolMin().intValue(), 4);
                String oaArcEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaArcEleMax().intValue(), 4);
                String oaArcEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaArcEleMin().intValue(), 4);
                String oaArcVolMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaArcVolMax().intValue(), 4);
                String oaArcVolMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaArcVolMin().intValue(), 4);
                String arcDelayTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcDelayTime().intValue(), 2);
                String alarmDelayTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getAlarmDelayTime().intValue(), 2);
                String haltDelayTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getHaltDelayTime().intValue(), 2);
                String haltFreezeTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getHaltFreezeTime().intValue(), 2);
                String reserved4 = "0000000000000000000000000000000000000000000000000000000000";
                String str = head + datetime + readWriteFlag + channelNo + dataFlag + initialEleMax + initialEleMin + initialVolMax + initialVolMin + initialWireSpeedMax + initialWireSpeedMin + weldEleMax + weldEleMin + weldVolMax + weldVolMin + weldWireSpeedMax + weldWireSpeedMin + arcEleMax + arcEleMin + arcVolMax + arcVolMin + arcWireSpeedMax + arcWireSpeedMin + reserved1 + modeSelect + weldingControl + weldingManner + texture + wireDiameter + gases + wireFeed + checkGasStatus + cutStatus + lockStatus + eleShowSelect + rev + boardThickness + spotWeldingTime + volShowSelect + arcLength + arcCharacter + penetrationControl + beforeAspiratedTime + afterStopGasTime + unitaryDifference + nowChannel + reserved2 + dclInitialEleMax + dclInitialEleMin + dclWeldEleMax + dclWeldEleMin + dclArcEleMax + dclArcEleMin + startDelayTime + clAmendPeriod + reserved3 + oaInitialEleMax + oaInitialEleMin + oaInitialVolMax + oaInitialVolMin + oaWeldEleMax + oaWeldEleMin + oaWeldVolMax + oaWeldVolMin + oaArcEleMax + oaArcEleMin + oaArcVolMax + oaArcVolMin + arcDelayTime + alarmDelayTime + haltDelayTime + haltFreezeTime + reserved4;
                str = str.toUpperCase();
                if (str.length() == 406) {
                    return str;
                }
            } catch (Exception e) {
                log.error("松下CO2焊机工艺下发参数拼接异常：", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 松下TIG焊机工艺下发参数拼接
     *
     * @param sxTigProcessIssue 松下TIG焊机实体类
     * @return 16进制工艺下发参数
     */
    public String sxTigProcessProtocol(SxTIGProcessIssue sxTigProcessIssue) {
        if (null != sxTigProcessIssue) {
            try {
                //头部固定参数
                String head = SxVerificationCode.SX_TIG_PROCESS_ISSUE_HEAD;
                LocalDateTime localDateTime = LocalDateTime.now();
                String year = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("yy")), 2);
                String month = CommonUtils.lengthJoint(localDateTime.getMonthValue(), 2);
                String day = CommonUtils.lengthJoint(localDateTime.getDayOfMonth(), 2);
                String hour = CommonUtils.lengthJoint(localDateTime.getHour(), 2);
                String minute = CommonUtils.lengthJoint(localDateTime.getMinute(), 2);
                String second = CommonUtils.lengthJoint(localDateTime.getSecond(), 2);
                String millisecond = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("SSS")), 4);
                String datetime = year + month + day + hour + minute + second + millisecond;
                String readWriteFlag = "00000000020100";
                String channelNo = CommonUtils.lengthJoint(sxTigProcessIssue.getChannelNo(), 2);
                String dataFlag = "0100";
                //预置参数
                String initialEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getInitialEleMax().intValue(), 4);
                String initialEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getInitialEleMin().intValue(), 4);
                String initialVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getInitialVolMax().intValue(), 4);
                String initialVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getInitialVolMin().intValue(), 4);
                String firstWeldEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getFirstWeldEleMax().intValue(), 4);
                String firstWeldEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getFirstWeldEleMin().intValue(), 4);
                String firstWeldVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getFirstWeldVolMax().intValue(), 4);
                String firstWeldVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getFirstWeldVolMin().intValue(), 4);
                String secondWeldEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getSecondWeldEleMax().intValue(), 4);
                String secondWeldEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getSecondWeldEleMin().intValue(), 4);
                String secondWeldVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getSecondWeldVolMax().intValue(), 4);
                String secondWeldVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getSecondWeldVolMin().intValue(), 4);
                String arcEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getArcEleMax().intValue(), 4);
                String arcEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getArcEleMin().intValue(), 4);
                String arcVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getArcVolMax().intValue(), 4);
                String arcVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getArcVolMin().intValue(), 4);
                String peakWeldEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getPeakWeldEleMax().intValue(), 4);
                String peakWeldEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getPeakWeldEleMin().intValue(), 4);
                String peakWeldVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getPeakWeldVolMax().intValue(), 4);
                String peakWeldVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getPeakWeldVolMin().intValue(), 4);
                String reserved1 = "00000000000000000000";
                //焊接条件
                String weldMethod = CommonUtils.lengthJoint(sxTigProcessIssue.getWeldMethod(), 2);
                String arcHaveNot = CommonUtils.lengthJoint(sxTigProcessIssue.getArcHaveNot(), 2);
                String pulseHaveNot = CommonUtils.lengthJoint(sxTigProcessIssue.getPulseHaveNot(), 2);
                String acWaveform = CommonUtils.lengthJoint(sxTigProcessIssue.getAcWaveform(), 2);
                String pulseRate = CommonUtils.lengthJoint(sxTigProcessIssue.getPulseRate().intValue(), 4);
                String pulseFrequency = CommonUtils.lengthJoint(sxTigProcessIssue.getPulseFrequency().intValue(), 4);
                String cleanWidth = CommonUtils.lengthJoint(sxTigProcessIssue.getCleanWidth().intValue(), 4);
                String acFrequency = CommonUtils.lengthJoint(sxTigProcessIssue.getAcFrequency().intValue(), 4);
                String mixFrequency = CommonUtils.lengthJoint(sxTigProcessIssue.getMixFrequency().intValue(), 4);
                String mixAcRate = CommonUtils.lengthJoint(sxTigProcessIssue.getMixAcRate().intValue(), 4);
                String pulseRadian = CommonUtils.lengthJoint(sxTigProcessIssue.getPulseRadian().intValue(), 4);
                String arcStiffness = CommonUtils.lengthJoint(sxTigProcessIssue.getArcStiffness().intValue(), 4);
                String handWeldThrust = CommonUtils.lengthJoint(sxTigProcessIssue.getHandWeldThrust().intValue(), 4);
                String beforeAspiratedTime = CommonUtils.lengthJoint(sxTigProcessIssue.getBeforeAspiratedTime().intValue(), 4);
                String afterStopGasTime = CommonUtils.lengthJoint(sxTigProcessIssue.getAfterStopGasTime().intValue(), 4);
                String mainWeldRiseTime = CommonUtils.lengthJoint(sxTigProcessIssue.getMainWeldRiseTime().intValue(), 4);
                String mainWeldDeclineTime = CommonUtils.lengthJoint(sxTigProcessIssue.getMainWeldDeclineTime().intValue(), 4);
                String mainWeldRiseRadian = CommonUtils.lengthJoint(sxTigProcessIssue.getMainWeldRiseRadian().intValue(), 4);
                String mainWeldDeclineRadian = CommonUtils.lengthJoint(sxTigProcessIssue.getMainWeldDeclineRadian().intValue(), 4);
                String spotWeldingTime = CommonUtils.lengthJoint(sxTigProcessIssue.getSpotWeldingTime().intValue(), 4);
                String spotWeldIntervalTime = CommonUtils.lengthJoint(sxTigProcessIssue.getSpotWeldIntervalTime().intValue(), 4);
                String spotWeldRiseTime = CommonUtils.lengthJoint(sxTigProcessIssue.getSpotWeldRiseTime().intValue(), 4);
                String spotWeldDeclineTime = CommonUtils.lengthJoint(sxTigProcessIssue.getSpotWeldDeclineTime().intValue(), 4);
                String spotWeldRiseRadian = CommonUtils.lengthJoint(sxTigProcessIssue.getSpotWeldRiseRadian().intValue(), 4);
                String spotWeldDeclineRadian = CommonUtils.lengthJoint(sxTigProcessIssue.getSpotWeldDeclineRadian().intValue(), 4);
                String maxChannel = CommonUtils.lengthJoint(sxTigProcessIssue.getMaxChannel(), 2);
                String nowChannel = CommonUtils.lengthJoint(sxTigProcessIssue.getNowChannel(), 2);
                String handWeldWeldEle = CommonUtils.lengthJoint(sxTigProcessIssue.getHandWeldWeldEle().intValue(), 4);
                String handWeldArcEle = CommonUtils.lengthJoint(sxTigProcessIssue.getHandWeldArcEle().intValue(), 4);
                String reserved2 = "000000000000";
                //动态限流参数
                String dclInitialEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getDclInitialEleMax().intValue(), 4);
                String dclInitialEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getDclInitialEleMin().intValue(), 4);
                String dclWeldEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getDclWeldEleMax().intValue(), 4);
                String dclWeldEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getDclWeldEleMin().intValue(), 4);
                String dclSecondWeldEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getDclSecondWeldEleMax().intValue(), 4);
                String dclSecondWeldEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getDclSecondWeldEleMin().intValue(), 4);
                String dclArcEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getDclArcEleMax().intValue(), 4);
                String dclArcEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getDclArcEleMin().intValue(), 4);
                String startDelayTime = CommonUtils.lengthJoint(sxTigProcessIssue.getStartDelayTime().intValue(), 2);
                String clAmendPeriod = CommonUtils.lengthJoint(sxTigProcessIssue.getClAmendPeriod().intValue(), 2);
                String reserved3 = "00000000000000000000";
                //超限报警参数
                String oaInitialEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaInitialEleMax().intValue(), 4);
                String oaInitialEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaInitialEleMin().intValue(), 4);
                String oaInitialVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaInitialVolMax().intValue(), 4);
                String oaInitialVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaInitialVolMin().intValue(), 4);
                String oaWeldEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaWeldEleMax().intValue(), 4);
                String oaWeldEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaWeldEleMin().intValue(), 4);
                String oaWeldVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaWeldVolMax().intValue(), 4);
                String oaWeldVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaWeldVolMin().intValue(), 4);
                String oaSecondWeldEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaSecondWeldEleMax().intValue(), 4);
                String oaSecondWeldEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaSecondWeldEleMin().intValue(), 4);
                String oaSecondWeldVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaSecondWeldVolMax().intValue(), 4);
                String oaSecondWeldVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaSecondWeldVolMin().intValue(), 4);
                String oaArcEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaArcEleMax().intValue(), 4);
                String oaArcEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaArcEleMin().intValue(), 4);
                String oaArcVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaArcVolMax().intValue(), 4);
                String oaArcVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaArcVolMin().intValue(), 4);
                String arcDelayTime = CommonUtils.lengthJoint(sxTigProcessIssue.getArcDelayTime().intValue(), 2);
                String alarmDelayTime = CommonUtils.lengthJoint(sxTigProcessIssue.getAlarmDelayTime().intValue(), 2);
                String haltDelayTime = CommonUtils.lengthJoint(sxTigProcessIssue.getHaltDelayTime().intValue(), 2);
                String haltFreezeTime = CommonUtils.lengthJoint(sxTigProcessIssue.getHaltFreezeTime().intValue(), 2);
                String reserved4 = "00000000000000000000";
                String str = head + datetime + readWriteFlag + channelNo + dataFlag + initialEleMax + initialEleMin + initialVolMax + initialVolMin + firstWeldEleMax + firstWeldEleMin + firstWeldVolMax + firstWeldVolMin + secondWeldEleMax + secondWeldEleMin + secondWeldVolMax + secondWeldVolMin + arcEleMax + arcEleMin + arcVolMax + arcVolMin + peakWeldEleMax + peakWeldEleMin + peakWeldVolMax + peakWeldVolMin + reserved1 + weldMethod + arcHaveNot + pulseHaveNot + acWaveform + pulseRate + pulseFrequency + cleanWidth + acFrequency + mixFrequency + mixAcRate + pulseRadian + arcStiffness + handWeldThrust + beforeAspiratedTime + afterStopGasTime + mainWeldRiseTime + mainWeldDeclineTime + mainWeldRiseRadian + mainWeldDeclineRadian + spotWeldingTime + spotWeldIntervalTime + spotWeldRiseTime + spotWeldDeclineTime + spotWeldRiseRadian + spotWeldDeclineRadian + maxChannel + nowChannel + handWeldWeldEle + handWeldArcEle + reserved2 + dclInitialEleMax + dclInitialEleMin + dclWeldEleMax + dclWeldEleMin + dclSecondWeldEleMax + dclSecondWeldEleMin + dclArcEleMax + dclArcEleMin + startDelayTime + clAmendPeriod + reserved3 + oaInitialEleMax + oaInitialEleMin + oaInitialVolMax + oaInitialVolMin + oaWeldEleMax + oaWeldEleMin + oaWeldVolMax + oaWeldVolMin + oaSecondWeldEleMax + oaSecondWeldEleMin + oaSecondWeldVolMax + oaSecondWeldVolMin + oaArcEleMax + oaArcEleMin + oaArcVolMax + oaArcVolMin + arcDelayTime + alarmDelayTime + haltDelayTime + haltFreezeTime + reserved4;
                str = str.toUpperCase();
                if (str.length() == 446) {
                    return str;
                }
            } catch (Exception e) {
                log.error("松下GL5系列TIG焊机工艺下发字符串拼接异常:", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 松下GL5系列【通道设定、通道读取】
     *
     * @param sxWeldChannelSetting 松下焊机通道设定实体类
     * @return 16进制字符串
     */
    public String sxWeldChannelSetProtocol(SxWeldChannelSetting sxWeldChannelSetting) {
        if (null != sxWeldChannelSetting) {
            try {
                String head = SxVerificationCode.SX_WELD_CHANNEL_SET_HEAD;
                LocalDateTime localDateTime = LocalDateTime.now();
                String year = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("yy")), 2);
                String month = CommonUtils.lengthJoint(localDateTime.getMonthValue(), 2);
                String day = CommonUtils.lengthJoint(localDateTime.getDayOfMonth(), 2);
                String hour = CommonUtils.lengthJoint(localDateTime.getHour(), 2);
                String minute = CommonUtils.lengthJoint(localDateTime.getMinute(), 2);
                String second = CommonUtils.lengthJoint(localDateTime.getSecond(), 2);
                String millisecond = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("SSS")), 4);
                String datetime = year + month + day + hour + minute + second + millisecond;
                String reserved1 = "00000000";
                //读写标志：0：主动上传；1：读取；2：设置；3：删除
                String readWriteFlag = CommonUtils.lengthJoint(sxWeldChannelSetting.getReadWriteFlag(), 2);
                String reserved2 = "000000";
                String function = CommonUtils.lengthJoint(sxWeldChannelSetting.getFunction(), 2);
                String channelSelect = CommonUtils.lengthJoint(sxWeldChannelSetting.getChannelSelect(), 2);
                String reserved = "000000000000000000000000";
                String str = head + datetime + reserved1 + readWriteFlag + reserved2 + function + channelSelect + reserved;
                str = str.toUpperCase();
                if (str.length() == 106) {
                    return str;
                }
            } catch (Exception e) {
                log.error("松下GL5系列【通道设定、通道读取】：", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 松下GL5系列工艺【工艺索取、工艺删除】协议拼接
     *
     * @param sxProcessClaim 松下工艺[索取/删除]实体类
     * @return 16进制字符串
     */
    public String sxProcessClaimProtocol(SxProcessClaim sxProcessClaim) {
        if (null != sxProcessClaim) {
            try {
                String head = SxVerificationCode.SX_PROCESS_CLAIM_HEAD;
                LocalDateTime localDateTime = LocalDateTime.now();
                String year = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("yy")), 2);
                String month = CommonUtils.lengthJoint(localDateTime.getMonthValue(), 2);
                String day = CommonUtils.lengthJoint(localDateTime.getDayOfMonth(), 2);
                String hour = CommonUtils.lengthJoint(localDateTime.getHour(), 2);
                String minute = CommonUtils.lengthJoint(localDateTime.getMinute(), 2);
                String second = CommonUtils.lengthJoint(localDateTime.getSecond(), 2);
                String millisecond = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("SSS")), 4);
                String datetime = year + month + day + hour + minute + second + millisecond;
                String reserved1 = "00000000";
                //读写标志
                String readWriteFlag = CommonUtils.lengthJoint(sxProcessClaim.getReadWriteFlag(), 2);
                String reserved2 = "0000";
                String channelNo = CommonUtils.lengthJoint(sxProcessClaim.getChannelNo(), 2);
                String reserved3 = "0000000000000000000000000000";
                String str = head + datetime + reserved1 + readWriteFlag + reserved2 + channelNo + reserved3;
                str = str.toUpperCase();
                if (str.length() == 106) {
                    return str;
                }
            } catch (Exception e) {
                log.error("松下GL5系列工艺【工艺索取、工艺删除】字符串拼接异常：", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 松下【FR2、AT3】系列【通道参数查询、通道参数删除】
     *
     * @param sxChannelParamQuery 松下【FR2、AT3】系列通道参数查询/删除
     * @return 16进制字符串
     */
    public String sxChannelParamQueryProtocol(SxChannelParamQuery sxChannelParamQuery) {
        if (null != sxChannelParamQuery) {
            String head = SxVerificationCode.SX_CHANNEL_PARAM_HEAD;
            String command = CommonUtils.lengthJoint(sxChannelParamQuery.getCommand(), 2);
            String channel = CommonUtils.lengthJoint(sxChannelParamQuery.getChannel(), 2);
            String reserved = "0000";
            String str = head + command + channel + reserved;
            str = str.toUpperCase();
            if (str.length() == 52) {
                return str;
            }
        }
        return null;
    }

    /**
     * 松下FR2系列【通道参数下载】
     *
     * @param paramDownload 参数下载实体类
     * @return 16进制字符串
     */
    public String sxChannelParamReplyHaveProtocol(SxChannelParamReplyHave paramDownload) {
        if (null != paramDownload) {
            String head = SxVerificationCode.SX_CHANNEL_PARAM_DOWNLOAD_HEAD;
            String command = CommonUtils.lengthJoint(paramDownload.getCommand(), 2);
            String channel = CommonUtils.lengthJoint(paramDownload.getChannel(), 2);
            String reserved = "00";
            String channelFlag = CommonUtils.lengthJoint(paramDownload.getChannelFlag(), 2);
            String presetEleMax = CommonUtils.lengthJoint(paramDownload.getPresetEleMax().intValue(), 4);
            String presetVolMax = CommonUtils.lengthJoint(paramDownload.getPresetVolMax().intValue(), 4);
            String presetEleMin = CommonUtils.lengthJoint(paramDownload.getPresetEleMin().intValue(), 4);
            String presetVolMin = CommonUtils.lengthJoint(paramDownload.getPresetVolMin().intValue(), 4);
            String initialEleMax = CommonUtils.lengthJoint(paramDownload.getInitialEleMax().intValue(), 4);
            String initialVolMax = CommonUtils.lengthJoint(paramDownload.getInitialVolMax().intValue(), 4);
            String initialEleMin = CommonUtils.lengthJoint(paramDownload.getInitialEleMin().intValue(), 4);
            String initialVolMin = CommonUtils.lengthJoint(paramDownload.getInitialVolMin().intValue(), 4);
            String arcEleMax = CommonUtils.lengthJoint(paramDownload.getArcEleMax().intValue(), 4);
            String arcVolMax = CommonUtils.lengthJoint(paramDownload.getArcVolMax().intValue(), 4);
            String arcEleMin = CommonUtils.lengthJoint(paramDownload.getArcEleMin().intValue(), 4);
            String arcVolMin = CommonUtils.lengthJoint(paramDownload.getArcVolMin().intValue(), 4);
            String texture = CommonUtils.lengthJoint(paramDownload.getTexture(), 2);
            String wireDiameter = CommonUtils.lengthJoint(paramDownload.getWireDiameter(), 2);
            String gases = CommonUtils.lengthJoint(paramDownload.getGases(), 2);
            String weldingControl = CommonUtils.lengthJoint(paramDownload.getWeldingControl(), 2);
            String pulseHaveNot = CommonUtils.lengthJoint(paramDownload.getPulseHaveNot(), 2);
            String spotWeldingTime = CommonUtils.lengthJoint(paramDownload.getSpotWeldingTime().intValue(), 4);
            String unitaryDifference = CommonUtils.lengthJoint(paramDownload.getUnitaryDifference(), 2);
            String dryExtendLength = CommonUtils.lengthJoint(paramDownload.getDryExtendLength(), 2);
            String reserved1 = "000000";
            String weldMax = CommonUtils.lengthJoint(paramDownload.getWeldMax().intValue(), 4);
            String weldMin = CommonUtils.lengthJoint(paramDownload.getWeldMin().intValue(), 4);
            String initialMax = CommonUtils.lengthJoint(paramDownload.getInitialMax().intValue(), 4);
            String initialMin = CommonUtils.lengthJoint(paramDownload.getInitialMin().intValue(), 4);
            String arcMax = CommonUtils.lengthJoint(paramDownload.getArcMax().intValue(), 4);
            String arcMin = CommonUtils.lengthJoint(paramDownload.getArcMin().intValue(), 4);
            String delayTime = CommonUtils.lengthJoint(paramDownload.getDelayTime().intValue(), 2);
            String amendPeriod = CommonUtils.lengthJoint(paramDownload.getAmendPeriod().intValue(), 2);
            String presetEleAlarmMax = CommonUtils.lengthJoint(paramDownload.getPresetEleAlarmMax().intValue(), 4);
            String presetVolAlarmMax = CommonUtils.lengthJoint(paramDownload.getPresetVolAlarmMax().intValue(), 4);
            String presetEleAlarmMin = CommonUtils.lengthJoint(paramDownload.getPresetEleAlarmMin().intValue(), 4);
            String presetVolAlarmMin = CommonUtils.lengthJoint(paramDownload.getPresetVolAlarmMin().intValue(), 4);
            String initialEleAlarmMax = CommonUtils.lengthJoint(paramDownload.getInitialEleAlarmMax().intValue(), 4);
            String initialVolAlarmMax = CommonUtils.lengthJoint(paramDownload.getInitialVolAlarmMax().intValue(), 4);
            String initialEleAlarmMin = CommonUtils.lengthJoint(paramDownload.getInitialEleAlarmMin().intValue(), 4);
            String initialVolAlarmMin = CommonUtils.lengthJoint(paramDownload.getInitialVolAlarmMin().intValue(), 4);
            String arcEleAlarmMax = CommonUtils.lengthJoint(paramDownload.getArcEleAlarmMax().intValue(), 4);
            String arcVolAlarmMax = CommonUtils.lengthJoint(paramDownload.getArcVolAlarmMax().intValue(), 4);
            String arcEleAlarmMin = CommonUtils.lengthJoint(paramDownload.getArcEleAlarmMin().intValue(), 4);
            String arcVolAlarmMin = CommonUtils.lengthJoint(paramDownload.getArcVolAlarmMin().intValue(), 4);
            String arcDelayTime = CommonUtils.lengthJoint(paramDownload.getArcDelayTime().intValue(), 2);
            String alarmDelayTime = CommonUtils.lengthJoint(paramDownload.getAlarmDelayTime().intValue(), 2);
            //String alarmHaltTime = CommonUtils.lengthJoint(paramDownload.getAlarmHaltTime().intValue(), 2);
            //String reserved2 = "00";
            //String alarmFlag = CommonUtils.lengthJoint(paramDownload.getAlarmFlag(), 2);
            //String reserved3 = "00000000";
            String str = head + command + channel + reserved + channelFlag + presetEleMax + presetVolMax + presetEleMin + presetVolMin + initialEleMax + initialVolMax + initialEleMin + initialVolMin + arcEleMax + arcVolMax + arcEleMin + arcVolMin + texture + wireDiameter + gases + weldingControl + pulseHaveNot + spotWeldingTime + unitaryDifference + dryExtendLength + reserved1 + weldMax + weldMin + initialMax + initialMin + arcMax + arcMin + delayTime + amendPeriod + presetEleAlarmMax + presetVolAlarmMax + presetEleAlarmMin + presetVolAlarmMin + initialEleAlarmMax + initialVolAlarmMax + initialEleAlarmMin + initialVolAlarmMin + arcEleAlarmMax + arcVolAlarmMax + arcEleAlarmMin + arcVolAlarmMin + arcDelayTime + alarmDelayTime;
            str = str.toUpperCase();
            if (str.length() == 204) {
                return str;
            }
        }
        return null;
    }

    /**
     * 松下AT3系列【通道参数下载】协议拼接
     *
     * @param at3ParamDownload 实体类
     * @return 16进制字符串
     */
    public String at3ParamDownloadProtocol(At3ParamDownload at3ParamDownload) {
        if (null != at3ParamDownload) {
            String head = SxVerificationCode.SX_AT3_PARAM_DOWNLOAD_HEAD;
            String command = CommonUtils.lengthJoint(at3ParamDownload.getCommand(), 2);
            String channel = CommonUtils.lengthJoint(at3ParamDownload.getChannel(), 2);
            String reserved = "00";
            String channelFlag = CommonUtils.lengthJoint(at3ParamDownload.getChannelFlag(), 2);
            String presetEleMax = CommonUtils.lengthJoint(at3ParamDownload.getPresetEleMax().intValue(), 4);
            String presetVolMax = CommonUtils.lengthJoint(at3ParamDownload.getPresetVolMax().intValue(), 4);
            String presetEleMin = CommonUtils.lengthJoint(at3ParamDownload.getPresetEleMin().intValue(), 4);
            String presetVolMin = CommonUtils.lengthJoint(at3ParamDownload.getPresetVolMin().intValue(), 4);
            String eleAlarmMax = CommonUtils.lengthJoint(at3ParamDownload.getEleAlarmMax().intValue(), 4);
            String volAlarmMax = CommonUtils.lengthJoint(at3ParamDownload.getVolAlarmMax().intValue(), 4);
            String eleAlarmMin = CommonUtils.lengthJoint(at3ParamDownload.getEleAlarmMin().intValue(), 4);
            String volAlarmMin = CommonUtils.lengthJoint(at3ParamDownload.getVolAlarmMin().intValue(), 4);
            String alarmDelayTime = CommonUtils.lengthJoint(at3ParamDownload.getAlarmDelayTime().intValue(), 2);
            String alarmHaltTime = CommonUtils.lengthJoint(at3ParamDownload.getAlarmHaltTime().intValue(), 2);
            String reserved1 = "0000";
            String str = head + command + channel + reserved + channelFlag + presetEleMax + presetVolMax + presetEleMin + presetVolMin + eleAlarmMax + volAlarmMax + eleAlarmMin + volAlarmMin + alarmDelayTime + alarmHaltTime + reserved1;
            str = str.toUpperCase();
            if (str.length() == 92) {
                return str;
            }
        }
        return null;
    }

}
