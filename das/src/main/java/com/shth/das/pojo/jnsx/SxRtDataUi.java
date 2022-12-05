package com.shth.das.pojo.jnsx;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 松下实时数据解析前端展示实体类（GL5、FR2、AT共用）
 */
@Data
public class SxRtDataUi implements Serializable {

    /**
     * 设备CID（设备唯一标识）
     */
    private String weldCid;

    /**
     * 通用数据
     */
    private String weldCode;        //设备编码（名称）
    private String weldIp;          //IP地址
    private String weldModel;       //设备机型
    private Integer weldStatus;     //焊接状态
    private String weldTime;        //焊机时间
    private Integer weldType;      //设备类型
    private String functionFlag;   //功能标志
    private BigDecimal initialEle; //初期电流
    private BigDecimal initialVol; //初期电压
    private BigDecimal initialWireSpeed; //初期送丝速度
    private BigDecimal weldEle;     //焊接电流
    private BigDecimal weldVol;     //焊接电压
    private BigDecimal weldWireSpeed;   //焊接送丝速度
    private BigDecimal arcEle;      //收弧电流
    private BigDecimal arcVol;      //收弧电压
    private BigDecimal arcWireSpeed;    //收弧送丝速度
    private BigDecimal realityWeldEle;  //实际焊接电流
    private BigDecimal realityWeldVol;  //实际焊接电压
    private BigDecimal realityWireSpeed;    //实际送丝速度
    private String alarmsCode;              //报警代码

    private int weldFlag;               //设备标志（0:GL5CO2,1:FR2CO2,2:FR2TIG,3:AT）

    //FR2系列CO2
    private BigDecimal presetInitialEle; //预置初期电流
    private BigDecimal presetArcEle;     //预置收弧电流
    private BigDecimal presetEle;        //预置电流
    private BigDecimal presetVol;        //预置电压
    private BigDecimal presetInitialVol; //预置初期电压
    private BigDecimal presetArcVol;     //预置收弧电压
    private BigDecimal wireSpeed;        //送丝速度
    private Integer runFlag;             //运行标志

    //FR2系列TIG
    private BigDecimal presetWeldEle;    //预置焊接电流
    private BigDecimal presetMaxEle;     //预置峰值电流
    private BigDecimal pulseRate;        //脉冲比率(5-95)
    private BigDecimal pulseFrequency;   //脉冲频率
    private BigDecimal cleanWidth;       //清洁宽度
    private BigDecimal acFrequency;      //AC频率
    private BigDecimal mixFrequency;     //MIX频率
    private BigDecimal mixAcRate;        //MIX（AC）比率

}
