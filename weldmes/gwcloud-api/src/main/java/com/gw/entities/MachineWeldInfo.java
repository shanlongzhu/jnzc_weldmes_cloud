package com.gw.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@Accessors(chain=true)
public class MachineWeldInfo implements Serializable {


    private long id;

    private String machineNo;

    private Byte status;

    private Byte type;

    private Byte firm;

    private Byte model;

    private long deptId;

    private String gId;

    private long isNetwork;

    private Timestamp productionDate;

    private String ipPath;

    private String createBy;

    private String createTime;

    private String lastUpdateBy;

    private Timestamp lastUpdateTime;

    private SysDictionary sysDictionary;

    private SysDept sysDept;

    private MachineGatherInfo machineGatherInfo;

    /**
     * 区域(字典)
     */
    private Long area;

    /**
     * 区域
     */
    private String areaStr;

    /**
     * 跨间(字典)
     */
    private Long bay;

    /**
     * 跨间
     */
    private String bayStr;

    /**
     * 绑定任务标识
     */
    private int taskFlag;

    /**
     * 类型名称
     */
    private String typeStr;

    /**
     * 设备状态名称
     */
    private String statusStr;

    /**
     * 厂商名称
     */
    private String firmStr;

    /**
     * 型号名称
     */
    private String modelStr;

    /**
     * 采集编号
     */
    private String gatherNo;

    /**
     * 设备标识
     */
    private String macFlag;

    /**
     * 所属项目
     */
    private String deptName;


}
