package com.gw.entities;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author zhanghan
 * @Date 2021/10/18 17:22
 * @Description 生产任务详情实体类
 */
@Data
@ColumnWidth(20)
public class WeldStatisticsDataProductionTask {

    /**
     * 焊工编号
     */
    @ExcelProperty(value = "焊工编号",index = 0)
    private String welderNo;

    /**
     * 焊工姓名
     */
    @ExcelProperty(value = "焊工姓名",index = 1)
    private String welderName;

    /**
     * 焊机编号
     */
    @ExcelProperty(value = "焊机编号",index = 2)
    private String machineNo;

    /**
     * 任务编号
     */
    @ExcelProperty(value = "任务编号",index = 3)
    private String taskNo;

    /**
     * 所属班组
     */
    @ExcelProperty(value = "所属班组",index = 4)
    private String deptName;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间",index = 5)
    private String startTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间",index = 6)
    private String endTime;

    /**
     * 电流范围
     */
    @ExcelProperty(value = "电流范围",index = 7)
    private String eleScope;

    /**
     * 平均-电流
     */
    @ExcelProperty(value = "平均电流",index = 8)
    private BigDecimal electricity;

    /**
     * 电压范围
     */
    @ExcelProperty(value = "电压范围",index = 9)
    private String volScope;

    /**
     * 平均-电压
     */
    @ExcelProperty(value = "平均电压",index = 10)
    private BigDecimal voltage;

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
