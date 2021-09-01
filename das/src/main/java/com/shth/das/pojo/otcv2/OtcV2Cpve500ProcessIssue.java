package com.shth.das.pojo.otcv2;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: OTC2.0(CPVE500)工艺下发规范
 * @author: Shan Long
 * @create: 2021-08-23
 */
@Data
public class OtcV2Cpve500ProcessIssue {

    public static final String HEAD = "007E35010101";
    public static final String ORDER = "52";
    public static final String FOOT = "3A7D";

    /**
     * 采集模块编号
     */
    private String gatherNo;
    private String channelNo;                   //通道号
    private BigDecimal spotWeldTime;            //点焊时间
    private BigDecimal preFlowTime;             //提前送气时间
    private BigDecimal initialEle;              //初期电流
    private BigDecimal initialVol;              //初期电压
    private BigDecimal initialVolUnitary;       //初期电压一元
    private BigDecimal weldElectricity;         //焊接电流
    private BigDecimal weldVoltage;             //焊接电压
    private BigDecimal weldVoltageUnitary;      //焊接电压一元
    private BigDecimal extinguishArcEle;        //收弧电流
    private BigDecimal extinguishArcVol;        //收弧电压
    private BigDecimal extinguishArcVolUnitary; //收弧电压一元
    private BigDecimal hysteresisAspirated;     //滞后送气
    private BigDecimal arcPeculiarity;          //电弧特性
    private BigDecimal gases;                   //气体
    private BigDecimal wireDiameter;            //焊丝直径
    private String wireMaterials;               //焊丝材料
    private String weldProcess;                 //焊接过程
    private String controlInfo;                 //控制信息
    private BigDecimal weldEleAdjust;           //焊接电流微调
    private BigDecimal weldVolAdjust;           //焊接电压微调
    private BigDecimal extinguishArcEleAdjust;  //收弧电流微调
    private BigDecimal extinguishArcVolAdjust;  //收弧电压微调
    private BigDecimal pulseFrequency;          //双脉冲频率
    private BigDecimal gasesFlow;               //气体流量
    private BigDecimal weldWireCathodeRate;     //焊丝负极(EN)比率

}
