package com.gw.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author LiZhengKai
 * @Date 2021/7/21 16:26
 * @Description
 */

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

    /**
     * 所属班组
     */
    private String deptName;

    /**
     * 设备总数
     */
    private Long allCount;

    /**
     * 开机设备数
     */
    private Long onOffCount;

    /**
     * 实焊设备数
     */
    private Long realWeldOnline;

    /**
     * 未绑定设备数
     */
    private Long noTaskCount;

    /**
     * 设备利用率
     */
    private Double equipUtilization;

    /**
     * 焊接任务数
     */
    private Long taskCount;

    /**
     * 焊接时间
     */
    private Long realWeldTime;

    /**
     * 工作时间
     */
    private Long onOffTime;

    /**
     * 超规范时间
     */
    private String supergageTime;

    /**
     * 规范符合率
     */
    private Double standardPercentage;

    /**
     * 焊材消耗
     */
    private Double materialsConsumption;

    /**
     * 电能消耗
     */
    private Double powerConsumption;

    /**
     * 焊接效率
     */
    private Double weldingEfficiency;

    /**
     * 正常时间
     */
    private Double normalTime;
}
