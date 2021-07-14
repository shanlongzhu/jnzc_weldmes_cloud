package com.gw.entities;


import lombok.Data;

import java.io.Serializable;

@Data
public class TaskClaim implements Serializable {

    /**
     * 主键
     */
    private long id;

    /**
     * 焊工id
     */
    private Long welderId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 焊机id
     */
    private Long weldId;

    /**
     * 领取时间
     */
    private String claimTime;


}
