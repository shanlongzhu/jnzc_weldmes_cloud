package com.shth.das.pojo.jnotc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 工艺索取返回实体类
 */
@SuppressWarnings("ALL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class JNProcessClaimReturn implements Serializable {

    private String gatherNo; //采集编号
    private Integer status;  //命令状态(控制命令)
    private String channelNo; //通道号：0-30
    private BigDecimal spotWeldTime;    //点焊时间
    private BigDecimal preflowTime;     //提前送气时间
    private BigDecimal initialEle;      //初期电流
    private BigDecimal initialVol;      //初期电压
    private BigDecimal initialVolUnitary;   //初期电压一元
    private BigDecimal weldElectricity;     //焊接电流
    private BigDecimal weldVoltage;         //焊接电压
    private BigDecimal weldVoltageUnitary;  //焊接电压一元
    private BigDecimal extinguishArcEle;    //收弧电流
    private BigDecimal extinguishArcVol;    //收弧电压
    private BigDecimal extinguishArcVolUnitary;  //收弧电压一元
    private BigDecimal hysteresisAspirated; //滞后送气
    private BigDecimal arcPeculiarity;      //电弧特性
    private BigDecimal gases;               //气体
    private BigDecimal wireDiameter;        //焊丝直径
    private String wireMaterials;           //焊丝材料
    private String weldProcess;             //焊接过程
    private String controlInfo;             //控制信息
    private BigDecimal weldEleAdjust;       //焊接电流微调
    private BigDecimal weldVolAdjust;       //焊接电压微调
    private BigDecimal extinguishArcEleAdjust;  //收弧电流微调
    private BigDecimal extinguishArcVolAdjust;  //收弧电压微调
    private BigDecimal alarmsElectricityMax;    //报警电流上限
    private BigDecimal alarmsElectricityMin;    //报警电流下限
    private BigDecimal alarmsVoltageMax;        //报警电压上限
    private BigDecimal alarmsVoltageMin;        //报警电压下限

}
