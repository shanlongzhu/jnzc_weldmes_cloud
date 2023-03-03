package com.shth.das.pojo.jnsx;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 松下FR2系列通道参数查询回复（有参数）
 * @author: Shan Long
 * @create: 2021-07-23
 */
@Data
public class SxChannelParamReplyHave implements Serializable {

    /**
     * 设备CID
     */
    private String weldCid;

    /**
     * 焊机IP
     */
    private String weldIp;

    /**
     * 控制（1.查询）
     */
    private Integer command;
    /**
     * 通道号
     */
    private String channel;
    /**
     * 通道标志（0~9）
     */
    private Integer channelFlag;

    private BigDecimal presetEleMax;        //预置电流上限
    private BigDecimal presetVolMax;        //预置电压上限
    private BigDecimal presetEleMin;        //预置电流下限
    private BigDecimal presetVolMin;        //预置电压下限
    private BigDecimal initialEleMax;       //初期电流上限
    private BigDecimal initialVolMax;       //初期电压上限
    private BigDecimal initialEleMin;       //初期电流下限
    private BigDecimal initialVolMin;       //初期电压下限
    private BigDecimal arcEleMax;           //收弧电流上限
    private BigDecimal arcVolMax;           //收弧电压上限
    private BigDecimal arcEleMin;           //收弧电流下限
    private BigDecimal arcVolMin;           //收弧电压下限
    private Integer texture;                //材质
    private Integer wireDiameter;           //丝径
    private Integer gases;                  //气体
    private Integer weldingControl;         //焊接控制
    private Integer pulseHaveNot;           //脉冲有无
    private BigDecimal spotWeldingTime;     //点焊时间
    private Integer unitaryDifference;      //一元/分别
    private Integer dryExtendLength;        //干伸长度
    private BigDecimal weldMax;             //焊接上限
    private BigDecimal weldMin;             //焊接下限
    private BigDecimal initialMax;          //初期上限
    private BigDecimal initialMin;          //初期下限
    private BigDecimal arcMax;              //收弧上限
    private BigDecimal arcMin;              //收弧下限
    private BigDecimal delayTime;           //延时时间
    private BigDecimal amendPeriod;         //修正周期
    private BigDecimal presetEleAlarmMax;        //预置电流报警上限
    private BigDecimal presetVolAlarmMax;        //预置电压报警上限
    private BigDecimal presetEleAlarmMin;        //预置电流报警下限
    private BigDecimal presetVolAlarmMin;        //预置电压报警下限
    private BigDecimal initialEleAlarmMax;       //初期电流报警上限
    private BigDecimal initialVolAlarmMax;       //初期电压报警上限
    private BigDecimal initialEleAlarmMin;       //初期电流报警下限
    private BigDecimal initialVolAlarmMin;       //初期电压报警下限
    private BigDecimal arcEleAlarmMax;           //收弧电流报警上限
    private BigDecimal arcVolAlarmMax;           //收弧电压报警上限
    private BigDecimal arcEleAlarmMin;           //收弧电流报警下限
    private BigDecimal arcVolAlarmMin;           //收弧电压报警下限
    private BigDecimal arcDelayTime;             //起弧延时时间
    private BigDecimal alarmDelayTime;           //报警延时时间
    private BigDecimal alarmHaltTime;            //报警停机时间
    private BigDecimal flowMax;                  //流量上限（查询回复）
    /**
     * 标志（下载参数）
     */
    private Integer alarmFlag;

}
