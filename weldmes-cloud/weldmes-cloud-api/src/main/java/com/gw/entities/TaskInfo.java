package com.gw.entities;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@ColumnWidth(20)
public class TaskInfo {

    /**
     * id
     */
    @ExcelIgnore
    private long id;

    /**
     * 任务名称
     */
    @ExcelIgnore
    private String taskName;

    /**
     * 任务编号
     */
    @ExcelProperty(value = "任务编号",index = 0)
    private String taskNo;

    /**
     * 等级id对应字符串
     */
    @ExcelProperty(value = "任务等级",index = 1)
    private String gradeIdToStr;

    /**
     * 部门名称
     */
    @ExcelProperty(value = "所属班组",index = 2)
    private String deptName;

    /**
     * 焊工姓名表示的是焊工编号
     */
    @ExcelProperty(value = "焊工编号",index = 3)
    private String welderName;

    /**
     * 计划开始时间
     */
    @ExcelProperty(value = "计划开始时间",index = 4)
    private String planStarttime;

    /**
     * 实际开始时间
     */
    @ExcelProperty(value = "实际开始时间",index = 5)
    private String realityStarttime;

    /**
     * 计划结束时间
     */
    @ExcelProperty(value = "计划结束时间",index = 6)
    private String planEndtime;

    /**
     * 实际结束时间
     */
    @ExcelProperty(value = "实际结束时间",index = 7)
    private String realityEndtime;

    /**
     * 电压上限
     */
    @ExcelProperty(value = "电压上限",index = 8)
    private Double volMax;

    /**
     * 电压下限
     */
    @ExcelProperty(value = "电压下限",index = 9)
    private Double volMin;

    /**
     * 电流上限
     */
    @ExcelProperty(value = "电流上限",index = 10)
    private Double eleMax;

    /**
     * 电流下限
     */
    @ExcelProperty(value = "电流下限",index = 11)
    private Double eleMin;

    /**
     * 评价内容
     */
    @ExcelProperty(value = "任务评价",index = 12)
    private String evaluateContent;

    /**
     * 评价星级字典id对应字符串
     */
    @ExcelProperty(value = "评价等级",index = 13)
    private String evaluateStarsIdToStr;

    /**
     * 工艺备注
     */
    @ColumnWidth(48)
    @ExcelProperty(value = "工艺备注",index = 14)
    private String craftRemarks;

    /**
     * 部门id
     */
    @ExcelIgnore
    private Long deptId;

    /**
     * 焊工id
     */
    @ExcelIgnore
    private Long welderId;

    /**
     * 等级字典id
     */
    @ExcelIgnore
    private Long grade;

    /**
     * 评价星级字典id
     */
    @ExcelIgnore
    private Long evaluateStars;

    /**
     * 状态字典id
     */
    @ExcelIgnore
    private int status;

    /**
     * 状态字典id对应字符串
     */
    @ExcelIgnore
    private String statusStr;

    /**
     * 创建人
     */
    @ExcelIgnore
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelIgnore
    private String createTime;

    /**
     * 所属作业区名称
     */
    @ExcelIgnore
    private String workSpaceName;

    /**
     * 所属作业区id
     */
    @ExcelIgnore
    private Long workSpaceId;

}
