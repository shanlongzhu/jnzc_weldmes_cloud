package com.gw.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SxAT3ProcessIssue {

    /**
     * 主键
     */
    private Long id;

    /**
     * 工艺库id
     */
    private Long wpsLibraryId;

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
     * 电流报警上限
     */
    private BigDecimal eleAlarmMax;

    /**
     * 电压报警上限
     */
    private BigDecimal volAlarmMax;

    /**
     * 电流报警下限
     */
    private BigDecimal eleAlarmMin;

    /**
     * 电压报警下限
     */
    private BigDecimal volAlarmMin;

    /**
     * 报警延时时间
     */
    private BigDecimal alarmDelayTime;

    /**
     * 报警停机时间
     */
    private BigDecimal alarmHaltTime;
}
