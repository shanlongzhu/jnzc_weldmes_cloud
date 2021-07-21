package com.shth.das.pojo.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @description: 焊工刷卡领取任务
 * @author: Shan Long
 * @create: 2021-07-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class TaskClaimIssue implements Serializable {

    /**
     * 焊工id
     */
    private BigInteger welderId;
    /**
     * 焊工姓名
     */
    private String welderName;
    /**
     * 焊工组织id
     */
    private BigInteger welderDeptId;
    /**
     * 任务id
     */
    private BigInteger taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务编号
     */
    private String taskNo;
    /**
     * 设备类型（默认0：OTC，1：松下）
     */
    private int weldType;
    /**
     * 开始标志（默认0：开始任务，1：结束任务）
     */
    private int startFlag;
    /**
     * OTC设备必须传采集编号
     */
    private String gatherNo;
    /**
     * 松下设备必须传设备IP
     */
    private String weldIp;

}
