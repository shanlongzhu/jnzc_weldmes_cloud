package com.gw.entities;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author LiZhengKai
 * @Date 2021/7/21 16:26
 * @Description
 */
@Data
@ColumnWidth(20)
public class WeldStatisticsData{
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
    private TaskClaim taskClaim;

    /**
     * 所属班组
     */
    @ExcelProperty(value = "所属班组",index = 0)
    private String deptName;

    /**
     * 设备总数
     */
    @ExcelProperty(value = "设备总数",index = 1)
    private Long allCount;

    /**
     * 开机设备数
     */
    @ExcelProperty(value = "开机设备数",index = 2)
    private Long onOffCount;

    /**
     * 实焊设备数
     */
    @ExcelProperty(value = "实焊设备数",index = 3)
    private Long realWeldOnline;

    /**
     * 未绑定设备数
     */
    @ExcelProperty(value = "未绑定设备数",index = 4)
    private Long noTaskCount;

    /**
     * 未绑定设备编号
     */
    @ExcelProperty(value = "开机设备数",index = 2)
    private String noTaskMachineDetail;

    /**
     * 设备利用率
     */
    @ExcelProperty(value = "开机设备数",index = 2)
    private Double equipUtilization;

    /**
     * 焊接任务数
     */
    @ExcelProperty(value = "开机设备数",index = 2)
    private Long taskCount;

    /**
     * 焊接时间
     */
    @ExcelProperty(value = "开机设备数",index = 2)
    private String realWeldTime;

    /**
     * 工作时间
     */
    @ExcelProperty(value = "开机设备数",index = 2)
    private String onOffTime;

    /**
     * 超规范时间
     */
    @ExcelProperty(value = "开机设备数",index = 2)
    private String supergageTime;

    /**
     * 规范符合率
     */
    @ExcelProperty(value = "开机设备数",index = 2)
    private Double standardPercentage;

    /**
     * 焊材消耗
     */
    @ExcelProperty(value = "开机设备数",index = 2)
    private Double materialsConsumption;

    /**
     * 电能消耗
     */
    @ExcelProperty(value = "开机设备数",index = 2)
    private Double powerConsumption;

    /**
     * 焊接效率
     */
    @ExcelProperty(value = "开机设备数",index = 2)
    private Double weldingEfficiency;

    /**
     * 正常时间
     */
    @ExcelProperty(value = "开机设备数",index = 2)
    private String normalTime;

    /**
     * 电流范围
     */
    @ExcelProperty(value = "开机设备数",index = 2)
    private String eleScope;

    /**
     * 电压范围
     */
    @ExcelProperty(value = "开机设备数",index = 2)
    private String volScope;

}
