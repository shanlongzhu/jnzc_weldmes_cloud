package com.shth.das.pojo.jnsx;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 松下TIG焊机工艺索取返回实体类
 * @author: Shan Long
 * @create: 2021-07-12
 */
@Data
public class SxTIGProcessClaimReturn implements Serializable {

    /**
     * 下发的焊机IP
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
    private int dataFlag;
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
    private BigDecimal firstWeldEleMax;     //第一焊接电流上限
    private BigDecimal firstWeldEleMin;     //第一焊接电流下限
    private BigDecimal firstWeldVolMax;     //第一焊接电压上限
    private BigDecimal firstWeldVolMin;     //第一焊接电压下限
    private BigDecimal secondWeldEleMax;    //第二焊接电流上限
    private BigDecimal secondWeldEleMin;    //第二焊接电流下限
    private BigDecimal secondWeldVolMax;    //第二焊接电压上限
    private BigDecimal secondWeldVolMin;    //第二焊接电压下限
    private BigDecimal arcEleMax;           //收弧电流上限
    private BigDecimal arcEleMin;           //收弧电流下限
    private BigDecimal arcVolMax;           //收弧电压上限
    private BigDecimal arcVolMin;           //收弧电压下限
    private BigDecimal peakWeldEleMax;      //峰值焊接电流上限
    private BigDecimal peakWeldEleMin;      //峰值焊接电流下限
    private BigDecimal peakWeldVolMax;      //峰值焊接电压上限
    private BigDecimal peakWeldVolMin;      //峰值焊接电压下限

    /*
    焊接条件
     */
    private Integer weldMethod;             //焊接方法
    private Integer arcHaveNot;             //收弧有无
    private Integer pulseHaveNot;           //脉冲有无
    private Integer acWaveform;             //交流波形
    private BigDecimal pulseRate;           //脉冲比率(5-95)
    private BigDecimal pulseFrequency;      //脉冲频率
    private BigDecimal cleanWidth;          //清洁宽度
    private BigDecimal acFrequency;         //AC频率
    private BigDecimal mixFrequency;        //MIX频率
    private BigDecimal mixAcRate;           //MIX（AC）比率
    private BigDecimal pulseRadian;         //脉冲弧度
    private BigDecimal arcStiffness;        //电弧挺度
    private BigDecimal handWeldThrust;      //手工焊推力
    private BigDecimal beforeAspiratedTime; //提前送气时间
    private BigDecimal afterStopGasTime;    //滞后停气时间
    private BigDecimal mainWeldRiseTime;    //主焊上升时间
    private BigDecimal mainWeldDeclineTime; //主焊下降时间
    private BigDecimal mainWeldRiseRadian;  //主焊上升弧度
    private BigDecimal mainWeldDeclineRadian;   //主焊下降弧度
    private BigDecimal spotWeldingTime;         //点焊时间
    private BigDecimal spotWeldIntervalTime;    //点焊间隔时间
    private BigDecimal spotWeldRiseTime;        //点焊上升时间
    private BigDecimal spotWeldDeclineTime;     //点焊下降时间
    private BigDecimal spotWeldRiseRadian;      //点焊上升弧度
    private BigDecimal spotWeldDeclineRadian;   //点焊下降弧度
    private Integer maxChannel;                 //最大通道
    private Integer nowChannel;                 //当前通道
    private BigDecimal handWeldWeldEle;         //手工焊焊接电流
    private BigDecimal handWeldArcEle;          //手工焊引弧电流

    /*
    动态限流参数
     */
    private BigDecimal dclInitialEleMax;    //初期电流上限
    private BigDecimal dclInitialEleMin;    //初期电流下限
    private BigDecimal dclWeldEleMax;       //焊接电流上限
    private BigDecimal dclWeldEleMin;       //焊接电流下限
    private BigDecimal dclSecondWeldEleMax; //第二焊接电流上限
    private BigDecimal dclSecondWeldEleMin; //第二焊接电流下限
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
    private BigDecimal oaSecondWeldEleMax; //第二焊接电流上限
    private BigDecimal oaSecondWeldEleMin; //第二焊接电流下限
    private BigDecimal oaSecondWeldVolMax; //第二焊接电压上限
    private BigDecimal oaSecondWeldVolMin; //第二焊接电压下限
    private BigDecimal oaArcEleMax;        //收弧电流上限
    private BigDecimal oaArcEleMin;        //收弧电流下限
    private BigDecimal oaArcVolMax;        //收弧电压上限
    private BigDecimal oaArcVolMin;        //收弧电压下限
    private BigDecimal arcDelayTime;       //起弧延时时间
    private BigDecimal alarmDelayTime;     //报警延时时间
    private BigDecimal haltDelayTime;      //停机延时时间
    private BigDecimal haltFreezeTime;     //停机冻结时间

}
