package com.shth.das.pojo.jnotc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 江南项目实时数据实体类
 */
@Data
//@Document("otc_weld_rtdata")
@TableName("otcrtd")
public class JNRtDataDB extends Model<JNRtDataDB> {

    private static final long serialVersionUID = 2047021588347136198L;

    /**
     * 焊机发送的数据字段
     */
    @TableField("weldModel")
    private Integer weldModel;              //焊机型号
    @TableField("gatherNo")
    private String gatherNo;                //采集模块编号
    @TableField("welderNo")
    private String welderNo;                //焊工号（卡号）
    @TableField("weldTime")
    private String weldTime;                //焊机时间
    @TableField("electricity")
    private BigDecimal electricity;         //电流
    @TableField("voltage")
    private BigDecimal voltage;             //电压
    @TableField("wireFeedRate")
    private BigDecimal wireFeedRate;        //送丝速度
    @TableField("presetEle")
    private BigDecimal presetEle;           //预置(给定)电流
    @TableField("presetVol")
    private BigDecimal presetVol;           //预置(给定)电压
    @TableField("weldedJunctionNo")
    private String weldedJunctionNo;        //焊口号
    @TableField("weldStatus")
    private Integer weldStatus;             //焊机状态
    @TableField("wireDiameter")
    private BigDecimal wireDiameter;        //焊丝直径
    @TableField("wireMaterialsGases")
    private BigDecimal wireMaterialsGases;  //焊丝材料和保护气体
    @TableField("weldElectricity")
    private BigDecimal weldElectricity;     //焊接电流
    @TableField("weldVoltage")
    private BigDecimal weldVoltage;         //焊接电压
    @TableField("weldEleAdjust")
    private BigDecimal weldEleAdjust;       //焊接电流微调
    @TableField("weldVolAdjust")
    private BigDecimal weldVolAdjust;       //焊接电压微调
    @TableField("channelNo")
    private String channelNo;               //当前通道号
    @TableField("alarmsEleMax")
    private BigDecimal alarmsEleMax;        //报警电流上限
    @TableField("alarmsEleMin")
    private BigDecimal alarmsEleMin;        //报警电流下限
    @TableField("alarmsVolMax")
    private BigDecimal alarmsVolMax;        //报警电压上限
    @TableField("alarmsVolMin")
    private BigDecimal alarmsVolMin;        //报警电压下限

    /**
     * 数据库字段
     */
    @TableId(type = IdType.AUTO)
    private BigInteger id;              //主键ID
    @TableField("machineId")
    private BigInteger machineId;       //焊机id
    @TableField("machineNo")
    private String machineNo;           //焊机编号
    @TableField("machineDeptId")
    private BigInteger machineDeptId;   //焊机组织id
    @TableField("gatherId")
    private BigInteger gatherId;        //采集模块id
    @TableField("gatherDeptId")
    private BigInteger gatherDeptId;    //采集模块组织id
    @TableField("welderId")
    private BigInteger welderId;        //焊工id（任务领取时不为空）
    @TableField("welderName")
    private String welderName;          //焊工姓名（任务领取时不为空）
    @TableField("welderDeptId")
    private BigInteger welderDeptId;    //焊工组织id（任务领取时不为空）
    @TableField("taskId")
    private BigInteger taskId;          //任务id（任务领取时不为空）
    @TableField("taskName")
    private String taskName;            //任务名称（任务领取时不为空）
    @TableField("taskNo")
    private String taskNo;              //任务编号（任务领取时不为空）
    @TableField("createTime")
    private String createTime;          //创建时间

}
