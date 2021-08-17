package com.gw.entities;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author zhanghan
 * @Date 2021/8/16 16:34
 * @Description 松下CO2工艺实体类
 */
@Data
public class SxCO2ProcessIssue {

    /**
     * id
     */
    private Long id;

    /**
     * 设备CID
     */
    private String weldCid;

    /**
     * 通道编号
     */
    private String channelNo;

    /**
     * 焊机IP
     */
    private String weldIp;

    //预置参数
    /**
     *初期电流上限
     */
    private BigDecimal initialEleMax;

    /**
     * 初期电流下限
     */
    private BigDecimal initialEleMin;

    /**
     * 初期电压上限
     */
    private BigDecimal initialVolMax;

    /**
     * 初期电压下限
     */
    private BigDecimal initialVolMin;

    /**
     * 初期送丝速度上限
     */
    private BigDecimal initialWireSpeedMax;

    /**
     * 初期送丝速度下限
     */
    private BigDecimal initialWireSpeedMin;

    /**
     * 焊接电流上限
     */
    private BigDecimal weldEleMax;

    /**
     * 焊接电流下限
     */
    private BigDecimal weldEleMin;

    /**
     * 焊接电压上限
     */
    private BigDecimal weldVolMax;

    /**
     * 焊接电压下限
     */
    private BigDecimal weldVolMin;

    /**
     * 焊接送丝速度上限
     */
    private BigDecimal weldWireSpeedMax;

    /**
     * 焊接送丝速度下限
     */
    private BigDecimal weldWireSpeedMin;

    /**
     * 收弧电流上限
     */
    private BigDecimal arcEleMax;

    /**
     * 收弧电流下限
     */
    private BigDecimal arcEleMin;

    /**
     * 收弧电压上限
     */
    private BigDecimal arcVolMax;

    /**
     * 收弧电压下限
     */
    private BigDecimal arcVolMin;

    /**
     * 收弧送丝速度上限
     */
    private BigDecimal arcWireSpeedMax;

    /**
     * 收弧送丝速度下限
     */
    private BigDecimal arcWireSpeedMin;

    //焊接条件
    /**
     * 模式选择
     */
    private Integer modeSelect;

    /**
     * 焊接控制
     */
    private Integer weldingControl;

    /**
     * 焊接方式
     */
    private Integer weldingManner;

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
     * 手动送丝
     */
    private Integer wireFeed;

    /**
     * id列表
     */
    private Integer checkGasStatus;

    /**
     * id列表
     */
    private Integer cutStatus;

    /**
     * 切换状态
     */
    private Integer lockStatus;

    /**
     * 电流显示选择
     */
    private Integer eleShowSelect;

    /**
     * 转速
     */
    private BigDecimal rev;

    /**
     * 板厚
     */
    private BigDecimal boardThickness;

    /**
     * 点焊时间
     */
    private BigDecimal spotWeldingTime;

    /**
     * 电压显示选择
     */
    private Integer volShowSelect;

    /**
     * 弧长
     */
    private BigDecimal arcLength;

    /**
     * 电弧特性
     */
    private Integer arcCharacter;

    /**
     * 熔深控制
     */
    private Integer penetrationControl;

    /**
     * 提前送气时间
     */
    private BigDecimal beforeAspiratedTime;

    /**
     * 滞后停气时间
     */
    private BigDecimal afterStopGasTime;    //滞后停气时间

    /**
     * 一元/分别
     */
    private Integer unitaryDifference;      //一元/分别

    /**
     * 当前通道
     */
    private int nowChannel;                 //当前通道

    //动态限流参数
    /**
     * 初期电流上限
     */
    private BigDecimal dclInitialEleMax;

    /**
     * 初期电流下限
     */
    private BigDecimal dclInitialEleMin;

    /**
     * 焊接电流上限
     */
    private BigDecimal dclWeldEleMax;

    /**
     * 焊接电流下限
     */
    private BigDecimal dclWeldEleMin;

    /**
     * 收弧电流上限
     */
    private BigDecimal dclArcEleMax;

    /**
     * 收弧电流下限
     */
    private BigDecimal dclArcEleMin;

    /**
     * 启动延时时间
     */
    private BigDecimal startDelayTime;

    /**
     * 限流修正周期
     */
    private BigDecimal clAmendPeriod;

    //超限报警参数
    /**
     * 初期电流上限
     */
    private BigDecimal oaInitialEleMax;

    /**
     * 初期电流下限
     */
    private BigDecimal oaInitialEleMin;

    /**
     * 初期电压上限
     */
    private BigDecimal oaInitialVolMax;

    /**
     * 初期电压下限
     */
    private BigDecimal oaInitialVolMin;

    /**
     * 焊接电流上限
     */
    private BigDecimal oaWeldEleMax;

    /**
     * 焊接电流下限
     */
    private BigDecimal oaWeldEleMin;

    /**
     * 焊接电压上限
     */
    private BigDecimal oaWeldVolMax;

    /**
     * 焊接电压下限
     */
    private BigDecimal oaWeldVolMin;

    /**
     * 收弧电流上限
     */
    private BigDecimal oaArcEleMax;

    /**
     * 收弧电流下限
     */
    private BigDecimal oaArcEleMin;

    /**
     * 收弧电压上限
     */
    private BigDecimal oaArcVolMax;

    /**
     * 收弧电压下限
     */
    private BigDecimal oaArcVolMin;

    /**
     * 起弧延时时间
     */
    private BigDecimal arcDelayTime;

    /**
     * 报警延时时间
     */
    private BigDecimal alarmDelayTime;

    /**
     * 停机延时时间
     */
    private BigDecimal haltDelayTime;

    /**
     * 停机冻结时间
     */
    private BigDecimal haltFreezeTime;
}
