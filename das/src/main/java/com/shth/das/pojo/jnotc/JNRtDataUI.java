package com.shth.das.pojo.jnotc;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 前端实时数据展示的实体类
 */
@Data
public class JNRtDataUI implements Serializable {

    /**
     * 采集编号
     */
    private String gatherNo;

    /**
     * 焊机状态（-1关机，0待机，3、5、7焊接，其他：故障）
     */
    private Integer weldStatus;

    /**
     * 电流
     */
    private BigDecimal electricity;

    /**
     * 电压
     */
    private BigDecimal voltage;

    /**
     * 焊机IP
     */
    private String weldIp;

    /**
     * 焊接时间
     */
    private String weldTime;

    /**
     * 送丝速度
     */
    private BigDecimal wireFeedRate;

    /**
     * 焊工id
     */
    private BigInteger welderId;

    /**
     * 焊工姓名
     */
    private String welderName;

    /**
     * 焊工编号
     */
    private String welderNo;

    /**
     * 焊工组织id
     */
    private BigInteger welderDeptId;

    /**
     * 任务id
     */
    private BigInteger taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 焊机id
     */
    private BigInteger machineId;

    /**
     * 焊机编号
     */
    private String machineNo;

    /**
     * 焊机组织id
     */
    private BigInteger machineDeptId;

    /**
     * 设备类型（默认0：OTC，1：松下）
     */
    private Integer weldType;

    /**
     * 当前通道号
     */
    private String channelNo;

    /**
     * 预置(给定)电流
     */
    private BigDecimal presetEle;

    /**
     * 预置(给定)电压
     */
    private BigDecimal presetVol;

    /**
     * 焊接电流
     */
    private BigDecimal weldElectricity;

    /**
     * 焊接电压
     */
    private BigDecimal weldVoltage;

    /**
     * 焊接电流微调
     */
    private BigDecimal weldEleAdjust;

    /**
     * 焊接电压微调
     */
    private BigDecimal weldVolAdjust;

}
