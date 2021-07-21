package com.gw.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@Accessors(chain=true)
public class WeldStatisticsData implements Serializable {
    private Long id;
    private Integer weldModel;
    private String gatherNo;
    private String welderNo;
    private Integer weldStatus;
    private Long weldDuration;
    private Long machineId;
    private String machineNo;
    private Long machineDeptId;
    private Long gatherId;
    private Long gatherDeptId;
    private Long welderId;
    private String welderName;
    private Long welderDeptId;
    private Long taskId;
    private String taskName;
    private String taskNo;
    private String createTime;
    private String startTime;
    private String endTime;
    private BigDecimal electricity;
    private BigDecimal voltage;
    private BigDecimal wireFeedRate;
    private BigDecimal wireDiameter;
    private BigDecimal wireMaterialsGases;
    private SysDept sysDept;
    private MachineWeldInfo machineWeldInfo;
    private Integer count;
    private Integer count2;
    private Integer count3;
    private Integer count4;
    private Integer count5;
    private Double utilization;
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Timestamp time;
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Timestamp time2;
    private Double utilization2;
    private WelderInfo welderInfo;
    private TaskInfo taskInfo;
    private SysDictionary sysDictionary;
}
