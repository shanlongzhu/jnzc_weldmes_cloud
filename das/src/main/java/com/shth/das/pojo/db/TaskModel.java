package com.shth.das.pojo.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 任务实体类
 */
@Data
@TableName(value = "task_info")
public class TaskModel implements Serializable {

    @TableId(type = IdType.AUTO)
    private BigInteger id;
    @TableField("task_name")
    private String taskName;
    @TableField("task_no")
    private String taskNo;
    @TableField("plan_starttime")
    private String planStarttime;
    @TableField("plan_endtime")
    private String planEndtime;
    @TableField("reality_starttime")
    private String realityStarttime;
    @TableField("reality_endtime")
    private String realityEndtime;
    @TableField("dept_id")
    private BigInteger deptId;
    @TableField("welder_id")
    private BigInteger welderId;
    private Integer grade;
    @TableField("evaluate_stars")
    private Integer evaluateStars;
    @TableField("evaluate_content")
    private String evaluateContent;
    private Integer status;
    @TableField(exist = false) //数据库不存在该字段
    private String welderNo;  //焊工号（卡号）

}
