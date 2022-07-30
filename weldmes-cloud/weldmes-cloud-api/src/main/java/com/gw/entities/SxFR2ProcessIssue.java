package com.gw.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SxFR2ProcessIssue {

    /**
     * 主键
     */
    private Long id;

    /**
     * 工艺库id
     */
    private Long wpsLibraryId;

    /**
     * 焊机IP
     */
    private String weldIp;

    /**
     * 控制（1.查询，2.下载，3.删除）
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

    /**
     * 预置电流上限
     */
    private BigDecimal presetEleMax;

    /**
     * 预置电压上限
     */
    private BigDecimal presetVolMax;

    /**
     * 预置电流下限
     */
    private BigDecimal presetEleMin;

    /**
     * 预置电压下限
     */
    private BigDecimal presetVolMin;

    /**
     * 初期电流上限
     */
    private BigDecimal initialEleMax;

    /**
     * 初期电压上限
     */
    private BigDecimal initialVolMax;

    /**
     * 初期电流下限
     */
    private BigDecimal initialEleMin;

    /**
     * 初期电压下限
     */
    private BigDecimal initialVolMin;

    /**
     * 收弧电流上限
     */
    private BigDecimal arcEleMax;

    /**
     * 收弧电压上限
     */
    private BigDecimal arcVolMax;

    /**
     * 收弧电流下限
     */
    private BigDecimal arcEleMin;

    /**
     * 收弧电压下限
     */
    private BigDecimal arcVolMin;

    /**
     * 材质
     */
    private Integer texture;

    /**
     * 丝径
     */
    private Integer wireDiameter;

    /**
     * 气体
     */
    private Integer gases;

    /**
     * 焊接控制
     */
    private Integer weldingControl;

    /**
     * 脉冲有无
     */
    private Integer pulseHaveNot;

    /**
     * 点焊时间
     */
    private BigDecimal spotWeldingTime;

    /**
     * 一元/分别
     */
    private Integer unitaryDifference;

    /**
     * 干伸长度
     */
    private Integer dryExtendLength;

    /**
     * 焊接上限
     */
    private BigDecimal weldMax;

    /**
     * 焊接下限
     */
    private BigDecimal weldMin;

    /**
     * 初期上限
     */
    private BigDecimal initialMax;

    /**
     * 初期下限
     */
    private BigDecimal initialMin;

    /**
     * 收弧上限
     */
    private BigDecimal arcMax;

    /**
     * 收弧下限
     */
    private BigDecimal arcMin;

    /**
     * 延时时间
     */
    private BigDecimal delayTime;

    /**
     * 修正周期
     */
    private BigDecimal amendPeriod;

    /**
     * 预置电流报警上限
     */
    private BigDecimal presetEleAlarmMax;

    /**
     * 预置电压报警上限
     */
    private BigDecimal presetVolAlarmMax;

    /**
     * 预置电流报警下限
     */
    private BigDecimal presetEleAlarmMin;

    /**
     * 预置电压报警下限
     */
    private BigDecimal presetVolAlarmMin;

    /**
     * 初期电流报警上限
     */
    private BigDecimal initialEleAlarmMax;

    /**
     * 初期电压报警上限
     */
    private BigDecimal initialVolAlarmMax;

    /**
     * 初期电流报警下限
     */
    private BigDecimal initialEleAlarmMin;

    /**
     * 初期电压报警下限
     */
    private BigDecimal initialVolAlarmMin;

    /**
     * 收弧电流报警上限
     */
    private BigDecimal arcEleAlarmMax;

    /**
     * 收弧电压报警上限
     */
    private BigDecimal arcVolAlarmMax;

    /**
     * 收弧电流报警下限
     */
    private BigDecimal arcEleAlarmMin;

    /**
     * 收弧电压报警下限
     */
    private BigDecimal arcVolAlarmMin;

    /**
     * 起弧延时时间
     */
    private BigDecimal arcDelayTime;

    /**
     * 报警延时时间
     */
    private BigDecimal alarmDelayTime;

    /**
     * 报警停机时间
     */
    private BigDecimal alarmHaltTime;

    /**
     * 流量上限(查询回复)
     */
    private BigDecimal flowMax;

    /**
     * 标志（下载参数）
     */
    private Integer alarmFlag;
}
