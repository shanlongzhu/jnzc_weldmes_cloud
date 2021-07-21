package com.shth.das.pojo.jnotc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 江南项目实时数据实体类
 */
@SuppressWarnings("ALL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class JNRtDataDB implements Serializable {

    /**
     * 焊机发送的数据字段
     */
    private Integer weldModel;           //焊机型号
    private String gatherNo;             //采集模块编号
    private String welderNo;             //焊工号（卡号）
    private String weldTime;             //焊机时间
    private BigDecimal electricity;         //电流
    private BigDecimal voltage;             //电压
    private BigDecimal wireFeedRate;        //送丝速度
    private BigDecimal presetEle;   //预置(给定)电流
    private BigDecimal presetVol;       //预置(给定)电压
    private String weldedJunctionNo;        //焊口号
    private Integer weldStatus;             //焊机状态
    private BigDecimal wireDiameter;        //焊丝直径
    private BigDecimal wireMaterialsGases;  //焊丝材料和保护气体
    private BigDecimal weldElectricity;     //焊接电流
    private BigDecimal weldVoltage;         //焊接电压
    private BigDecimal weldEleAdjust;   //焊接电流微调
    private BigDecimal weldVolAdjust;       //焊接电压微调
    private String channelNo;                   //当前通道号
    private BigDecimal alarmsEleMax;    //报警电流上限
    private BigDecimal alarmsEleMin;    //报警电流下限
    private BigDecimal alarmsVolMax;        //报警电压上限
    private BigDecimal alarmsVolMin;        //报警电压下限

    /**
     * 数据库字段
     */
    @TableId(type = IdType.AUTO)
    private BigInteger id;              //主键ID
    private BigInteger machineId;       //焊机id
    private String machineNo;           //焊机编号
    private BigInteger machineDeptId;   //焊机组织id
    private BigInteger gatherId;        //采集模块id
    private BigInteger gatherDeptId;    //采集模块组织id
    private BigInteger welderId;        //焊工id（任务领取时不为空）
    private String welderName;          //焊工姓名（任务领取时不为空）
    private BigInteger welderDeptId;    //焊工组织id（任务领取时不为空）
    private BigInteger taskId;          //任务id（任务领取时不为空）
    private String taskName;            //任务名称（任务领取时不为空）
    private String taskNo;              //任务编号（任务领取时不为空）
    private String createTime;          //创建时间

}
