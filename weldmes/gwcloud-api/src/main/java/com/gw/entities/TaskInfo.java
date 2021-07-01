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

    private long id;

    private String taskName;

    private String taskNo;

    private String planStarttime;

    private String planEndtime;

    private String realityStarttime;

    private String realityEndtime;

    private Long deptId;

    //所属班组
    private String deptName;

    private Long welderId;

    //焊工姓名
    private String welderName;

    //任务等级
    private byte grade;

    //任务等级
    private String gradeIdToStr;

    //评价星级Id
    private byte evaluateStars;

    //评价星级
    private String evaluateStarsIdToStr;

    private String evaluateContent;

    //任务状态Id
    private int status;

    //任务状态
    private String statusStr;

    private String createBy;

    private Timestamp createTime;

    //所属作业区
    private String workSpaceName;

    //所属作业区
    private Long workSpaceId;
}
