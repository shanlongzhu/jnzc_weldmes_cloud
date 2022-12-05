package com.shth.das.pojo.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 松下焊机表（数据库表）
 */
@Data
@TableName(value = "machine_weldsx_info")
public class SxWeldModel implements Serializable {

    @TableId(type = IdType.AUTO)
    private BigInteger id;          //ID
    private String weldNo;          //设备序号/编号
    private String weldCid;         //设备CID（设备唯一标识）
    private String weldCode;        //设备编码（名称）
    private String weldIp;          //IP地址
    private String weldModel;       //设备机型
    private Integer powerSupply;    //电源类型(字典)
    private Integer wireFeederModel; //送丝机类型(字典)
    private Integer weldKind;       //设备种类/类型(字典)[如果是FR2、AT3【0：数字焊机，1：模拟焊机】]
    private Integer weldCpuNum;     //焊机CPU个数
    private String cpu1No;          //cpu1编号
    private Integer cpu1Model;      //cpu1类型(字典)
    private String cpu1Version;     //cpu1软件版本
    private String cpu2No;
    private Integer cpu2Model;
    private String cpu2Version;
    private String cpu3No;
    private Integer cpu3Model;
    private String cpu3Version;
    private Integer weldStatus;     //设备状态（字典正常、维修）
    private Integer weldFirm;       //设备厂商（字典OTC、松下）
    private BigInteger deptId;      //组织机构ID
    private String createTime;      //创建时间

}
