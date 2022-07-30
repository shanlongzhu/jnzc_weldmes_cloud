package com.gw.entities;

import lombok.Data;

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
