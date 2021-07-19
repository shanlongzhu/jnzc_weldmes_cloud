package com.gw.entities;

import lombok.Data;

/**
 * @Author zhanghan
 * @Date 2021/7/19 15:01
 * @Description 任务焊机绑定信息类
 */
@Data
public class WeldClaimTaskInfo {

    /**
     * 焊工姓名
     */
    private String welderName;

    /**
     * 焊工编号
     */
    private String welderNo;

    /**
     * 所在班组
     */
    private String deptName;

    /**
     * 焊机编号
     */
    private String weldeNo;

    /**
     * 焊机类型
     */
    private String weldeType;

    /**
     * 任务编号
     */
    private String taskNo;
}
