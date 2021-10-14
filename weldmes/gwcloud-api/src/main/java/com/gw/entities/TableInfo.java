package com.gw.entities;

import lombok.Data;

/**
 * @Author zhanghan
 * @Date 2021/7/15 19:17
 * @Description  表信息对象
 */
@Data
public class TableInfo {

    private String tableName;

    private Long taskId;

    private Long welderId;

    private Long weldMachineId;

    /**
     * 焊机类型（0：OTC，1：松下）
     */
    private Integer weldType;

    private String startTime;

    private String endTime;
}
