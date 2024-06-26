package com.shth.das.pojo.jnsx;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 松下AT3系列参数下载
 * @author: Shan Long
 * @create: 2021-07-26
 */
@Data
public class At3ParamDownload implements Serializable {

    /**
     * 设备CID
     */
    private String weldCid;

    /**
     * 控制（2.下载）
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
    private BigDecimal eleAlarmMax;         //电流报警上限
    private BigDecimal volAlarmMax;         //电压报警上限
    private BigDecimal eleAlarmMin;         //电流报警下限
    private BigDecimal volAlarmMin;         //电压报警下限
    private BigDecimal alarmDelayTime;      //报警延时时间
    private BigDecimal alarmHaltTime;       //报警停机时间

}
