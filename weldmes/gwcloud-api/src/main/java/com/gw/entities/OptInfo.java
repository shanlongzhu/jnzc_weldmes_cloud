package com.gw.entities;

import lombok.Data;

/**
 * @Author zhanghan
 * @Date 2021/6/2 11:24
 * @Description 条件查询实体类
 */
@Data
public class OptInfo {


    /**
     * 所属班组
     */
    private int grade;

    /**
     * 任务状态
     */
    private int taskStatus;
}
