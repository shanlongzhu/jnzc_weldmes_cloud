package com.gw.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.sql.Timestamp;


@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@Accessors(chain=true)
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
public class TaskInfo {

    /**
     * id
     */
    private long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 计划开始时间
     */
    private String planStarttime;

    /**
     * 计划结束时间
     */
    private String planEndtime;

    /**
     * 实际开始时间
     */
    private String realityStarttime;

    /**
     * 实际结束时间
     */
    private String realityEndtime;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 焊工id
     */
    private Long welderId;

    /**
     * 焊工姓名
     */
    private String welderName;

    /**
     * 等级字典id
     */
    private Long grade;

    /**
     * 等级id对应字符串
     */
    private String gradeIdToStr;

    /**
     * 评价星级字典id
     */
    private Long evaluateStars;

    /**
     * 评价星级字典id对应字符串
     */
    private String evaluateStarsIdToStr;

    /**
     * 评价内容
     */
    private String evaluateContent;

    /**
     * 状态字典id
     */
    private int status;

    /**
     * 状态字典id对应字符串
     */
    private String statusStr;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 所属作业区名称
     */
    private String workSpaceName;

    /**
     * 所属作业区id
     */
    private Long workSpaceId;

    /**
     * 电流上限
     */
    private Double eleMax;

    /**
     * 电流下限
     */
    private Double eleMin;

    /**
     * 电压上限
     */
    private Double volMax;

    /**
     * 电压下限
     */
    private Double volMin;
}
