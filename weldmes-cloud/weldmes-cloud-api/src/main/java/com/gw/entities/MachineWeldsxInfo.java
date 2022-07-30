package com.gw.entities;

import lombok.Data;

@Data
public class MachineWeldsxInfo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 设备序号/编号(唯一)
     */
    private String weldNo;

    /**
     * 设备CID(唯一标识)
     */
    private String weldCid;

    /**
     * 设备编码（名称）
     */
    private String weldCode;

    /**
     * IP地址
     */
    private String weldIp;

    /**
     * 设备机型
     */
    private String weldModel;

    /**
     * 电源类型(字典)
     */
    private Long powerSupply;

    /**
     * 电源类型
     */
    private String powerSupplyStr;

    /**
     * 送丝机类型(字典)
     */
    private Long wireFeederModel;

    /**
     * 送丝机类型
     */
    private String wireFeederModelStr;

    /**
     * 设备种类(字典)
     */
    private Long weldKind;

    /**
     * 设备种类
     */
    private String weldKindStr;

    /**
     * 焊机CPU个数
     */
    private Integer weldCpuNum;

    /**
     * cpu1编号
     */
    private String cpu1No;

    /**
     * cpu1类型(字典)
     */
    private Long cpu1Model;

    /**
     * cpu1类型
     */
    private String cpu1ModelStr;

    /**
     * cpu1软件版本
     */
    private String cpu1Version;

    /**
     * cpu2编号
     */
    private String cpu2No;

    /**
     * cpu2类型(字典)
     */
    private Long cpu2Model;

    /**
     * cpu2类型
     */
    private String cpu2ModelStr;

    /**
     * cpu2软件版本
     */
    private String cpu2Version;

    /**
     * cpu3编号
     */
    private String cpu3No;

    /**
     * cpu3类型(字典)
     */
    private Long cpu3Model;

    /**
     * cpu3类型
     */
    private String cpu3ModelStr;

    /**
     * cpu3软件版本
     */
    private String cpu3Version;

    /**
     * 设备状态（字典正常、维修）
     */
    private Long weldStatus;

    /**
     * 设备状态
     */
    private String weldStatusStr;

    /**
     * 设备厂商（字典OTC、松下）
     */
    private Long weldFirm;

    /**
     * 设备厂商
     */
    private String weldFirmStr;

    /**
     * 组织机构ID
     */
    private Long deptId;

    /**
     * 组织机构ID
     */
    private String deptIdStr;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 设备型号(字典)
     */
    private Long equipType;

    /**
     * 设备型号
     */
    private String equipTypeStr;

    /**
     * 区域
     */
    private Long area;

    /**
     * 跨间
     */
    private Long bay;

}
