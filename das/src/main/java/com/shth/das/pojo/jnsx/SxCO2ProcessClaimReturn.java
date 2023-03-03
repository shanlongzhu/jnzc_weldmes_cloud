package com.shth.das.pojo.jnsx;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 松下GL5系列CO2焊机工艺索取返回（CO2数据有返回）
 * @author: Shan Long
 * @create: 2021-07-12
 */
@Data
public class SxCO2ProcessClaimReturn implements Serializable {

    /**
     * 设备CID
     */
    private String weldCid;
    /**
     * 索取的焊机IP
     */
    private String weldIp;
    /**
     * 焊机时间
     */
    private String weldTime;
    /**
     * 设备类型
     */
    private Integer weldType;
    /**
     * 通道编号
     */
    private String channelNo;
    /**
     * 数据标志（1：有数据；0：无数据）
     */
    private Integer dataFlag;
    /**
     * 通道标志
     */
    private Integer channelFlag;

    /*
    预置参数
     */
    private BigDecimal initialEleMax;       //初期电流上限
    private BigDecimal initialEleMin;       //初期电流下限
    private BigDecimal initialVolMax;       //初期电压上限
    private BigDecimal initialVolMin;       //初期电压下限
    private BigDecimal initialWireSpeedMax; //初期送丝速度上限
    private BigDecimal initialWireSpeedMin; //初期送丝速度下限
    private BigDecimal weldEleMax;          //焊接电流上限
    private BigDecimal weldEleMin;          //焊接电流下限
    private BigDecimal weldVolMax;          //焊接电压上限
    private BigDecimal weldVolMin;          //焊接电压下限
    private BigDecimal weldWireSpeedMax;    //焊接送丝速度上限
    private BigDecimal weldWireSpeedMin;    //焊接送丝速度下限
    private BigDecimal arcEleMax;           //收弧电流上限
    private BigDecimal arcEleMin;           //收弧电流下限
    private BigDecimal arcVolMax;           //收弧电压上限
    private BigDecimal arcVolMin;           //收弧电压下限
    private BigDecimal arcWireSpeedMax;     //收弧送丝速度上限
    private BigDecimal arcWireSpeedMin;     //收弧送丝速度下限

    /*
    焊接条件
     */
    private Integer modeSelect;     //模式选择
    private Integer weldingControl; //焊接控制
    private Integer weldingManner;  //焊接方式
    private Integer texture;        //材质
    private Integer wireDiameter;   //丝径
    private Integer gases;          //气体
    private Integer wireFeed;       //手动送丝
    private Integer checkGasStatus; //检气状态
    private Integer cutStatus;      //切换状态
    private Integer lockStatus;     //锁定状态
    private Integer eleShowSelect;          //电流显示选择
    private BigDecimal rev;                 //转速
    private BigDecimal boardThickness;      //板厚
    private BigDecimal spotWeldingTime;     //点焊时间
    private Integer volShowSelect;          //电压显示选择
    private BigDecimal arcLength;           //弧长
    private Integer arcCharacter;           //电弧特性
    private Integer penetrationControl;     //熔深控制
    private BigDecimal beforeAspiratedTime; //提前送气时间
    private BigDecimal afterStopGasTime;    //滞后停气时间
    private Integer unitaryDifference;      //一元/分别
    private Integer nowChannel;                 //当前通道

    /*
    动态限流参数
     */
    private BigDecimal dclInitialEleMax;    //初期电流上限
    private BigDecimal dclInitialEleMin;    //初期电流下限
    private BigDecimal dclWeldEleMax;       //焊接电流上限
    private BigDecimal dclWeldEleMin;       //焊接电流下限
    private BigDecimal dclArcEleMax;        //收弧电流上限
    private BigDecimal dclArcEleMin;        //收弧电流下限
    private BigDecimal startDelayTime;      //启动延时时间
    private BigDecimal clAmendPeriod;       //限流修正周期

    /*
    超限报警参数
     */
    private BigDecimal oaInitialEleMax;    //初期电流上限
    private BigDecimal oaInitialEleMin;    //初期电流下限
    private BigDecimal oaInitialVolMax;    //初期电压上限
    private BigDecimal oaInitialVolMin;    //初期电压下限
    private BigDecimal oaWeldEleMax;       //焊接电流上限
    private BigDecimal oaWeldEleMin;       //焊接电流下限
    private BigDecimal oaWeldVolMax;       //焊接电压上限
    private BigDecimal oaWeldVolMin;       //焊接电压下限
    private BigDecimal oaArcEleMax;        //收弧电流上限
    private BigDecimal oaArcEleMin;        //收弧电流下限
    private BigDecimal oaArcVolMax;        //收弧电压上限
    private BigDecimal oaArcVolMin;        //收弧电压下限
    private BigDecimal arcDelayTime;       //起弧延时时间
    private BigDecimal alarmDelayTime;     //报警延时时间
    private BigDecimal haltDelayTime;      //停机延时时间
    private BigDecimal haltFreezeTime;     //停机冻结时间

}
