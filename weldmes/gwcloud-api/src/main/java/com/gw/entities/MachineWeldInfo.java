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
    private long gId;
    private long isNetwork;
    private Timestamp productionDate;
    private String ipPath;
    private String createBy;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
}