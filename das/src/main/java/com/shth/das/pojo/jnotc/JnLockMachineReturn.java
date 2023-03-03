package com.shth.das.pojo.jnotc;

import lombok.Data;

/**
 * @description: 锁焊机和解锁焊机指令返回
 * @author: Shan Long
 * @create: 2021-08-24
 */
@Data
public class JnLockMachineReturn {

    /**
     * 采集编号（10进制数字）
     */
    private String gatherNo;

    /**
     * 控制命令（18：锁焊机，19：解锁）
     */
    private Integer command;

    /**
     * 接收结果（0：成功， 1：失败）
     */
    private Integer result;

}
