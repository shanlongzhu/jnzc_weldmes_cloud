package com.gw.entities;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ColumnWidth(20)
public class WeldStatisticsDataArtifact {

    /**
     * 任务编号
     */
    @ExcelProperty(value = "任务编号",index = 0)
    private String taskNo;

    /**
     * 班组
     */
    @ExcelProperty(value = "班组",index = 1)
    private String deptName;

    /**
     * 参与人员数
     */
    @ExcelProperty(value = "参与人员数",index = 2)
    private String welderNum;

    /**
     * 使用设备数
     */
    @ExcelProperty(value = "使用设备数",index = 3)
    private String machineNum;

    /**
     * 工作时间
     */
    @ExcelProperty(value = "工作时间",index = 4)
    private String onOffTime;

    /**
     * 焊接时间
     */
    @ExcelProperty(value = "焊接时间",index = 5)
    private String realWeldTime;

    /**
     * 正常时间
     */
    @ExcelProperty(value = "正常时间",index = 6)
    private String normalTime;

    /**
     * 焊接效率
     */
    @ExcelProperty(value = "焊接效率",index = 7)
    private Double weldingEfficiency;

    /**
     * 超规范时间
     */
    @ExcelProperty(value = "超规范时间",index = 8)
    private String supergageTime;

    /**
     * 规范符合率
     */
    @ExcelProperty(value = "规范符合率",index = 9)
    private Double standardPercentage;

    /**
     * 焊材消耗
     */
    @ExcelProperty(value = "焊材消耗",index = 10)
    private Double materialsConsumption;

    /**
     * 电能消耗
     */
    @ExcelProperty(value = "电能消耗",index = 11)
    private Double powerConsumption;

    /**
     * 主键
     */
    @ExcelIgnore
    private Long id;

    /**
     * 焊机型号
     */
    @ExcelIgnore
    private Integer weldModel;

    /**
     * 采集编号
     */
    @ExcelIgnore
    private String gatherNo;

    /**
     * 焊工号(卡号)
     */
    @ExcelIgnore
    private String welderNo;

    /**
     * 焊机状态(字典待机、焊接、故障、关机)
     */
    @ExcelIgnore
    private Integer weldStatus;

    /**
     * 焊接时长(秒)
     */
    @ExcelIgnore
    private Long weldDuration;

    /**
     * 焊机Id
     */
    @ExcelIgnore
    private Long machineId;

    /**
     * 焊机编号
     */
    @ExcelIgnore
    private String machineNo;

    /**
     * 焊机组织id
     */
    @ExcelIgnore
    private Long machineDeptId;

    /**
     * 采集模块id
     */
    @ExcelIgnore
    private Long gatherId;

    /**
     * 采集模块组织id
     */
    @ExcelIgnore
    private Long gatherDeptId;

    /**
     * 焊工Id
     */
    @ExcelIgnore
    private Long welderId;

    /**
     * 焊工姓名
     */
    @ExcelIgnore
    private String welderName;

    /**
     * 焊工组织机构Id
     */
    @ExcelIgnore
    private Long welderDeptId;

    /**
     * 任务Id
     */
    @ExcelIgnore
    private Long taskId;

    /**
     * 任务名称
     */
    @ExcelIgnore
    private String taskName;

    /**
     * 创建时间
     */
    @ExcelIgnore
    private String createTime;

    /**
     * 开始时间
     */
    @ExcelIgnore
    private String startTime;

    /**
     * 结束时间
     */
    @ExcelIgnore
    private String endTime;

    /**
     * 平均-电流
     */
    @ExcelIgnore
    private BigDecimal electricity;

    /**
     * 平均-电压
     */
    @ExcelIgnore
    private BigDecimal voltage;

    /**
     * 平均-送丝速度
     */
    @ExcelIgnore
    private BigDecimal wireFeedRate;

    /**
     * 平均-焊丝直径
     */
    @ExcelIgnore
    private BigDecimal wireDiameter;

    /**
     * 平均-焊丝材料和保护气体
     */
    @ExcelIgnore
    private BigDecimal wireMaterialsGases;
}
