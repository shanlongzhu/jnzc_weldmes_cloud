package com.shth.das.pojo.jnsx;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 松下设备状态信息实体类（GL5、FR2、AT共用）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class SxStatusDataUI implements Serializable {

    //共有
    private String weldIp;              //焊机IP
    private String weldTime;            //焊接时间
    private String functionFlag;        //功能标志
    private Integer texture;            //材质
    private Integer wireDiameter;       //丝径
    private Integer gases;              //气体
    private Integer weldingControl;     //焊接控制
    private BigDecimal spotWeldingTime; //点焊时间
    private int nowChannel;             //当前通道
    private int maxChannel;             //最大通道
    private Integer unitaryDifference;  //一元/分别

    private int weldFlag;               //设备标志（0:GL5CO2,1:FR2CO2,2:FR2TIG,3:AT）

    //GL5系列CO2
    private Integer weldType;           //设备类型
    private Integer modeSelect;         //模式选择
    private Integer weldingManner;      //焊接方式
    private Integer wireFeed;           //手动送丝
    private Integer checkGasStatus;     //检气状态
    private Integer cutStatus;          //切换状态
    private Integer lockStatus;         //锁定状态
    private Integer eleShowSelect;      //电流显示选择
    private BigDecimal rev;             //转速
    private BigDecimal boardThickness;  //板厚
    private Integer volShowSelect;      //电压显示选择
    private BigDecimal arcLength;       //弧长
    private Integer arcCharacter;       //电弧特性
    private Integer penetrationControl;     //熔深控制
    private BigDecimal beforeAspiratedTime; //提前送气时间
    private BigDecimal afterStopGasTime;    //滞后停气时间

    //FR2系列CO2
    private String weldModel;          //设备机型
    private String welderNo;           //工人编号
    private String workpieceNoMin;     //工件编号
    private Integer pulseHaveNot;      //脉冲有无
    private Integer dryExtendLength;   //干伸长度
    private String workpieceNoMax;     //工件编号

    //FR2系列TIG
    private Integer weldMethod;             //焊接方法
    private Integer arcHaveNot;             //收弧有无
    private Integer acWaveform;             //交流波形
    private BigDecimal riseTime;            //上升时间
    private BigDecimal declineTime;         //下降时间

}
