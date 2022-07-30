package com.gw.entities;

import lombok.Data;

@Data
public class ApiInfo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 接口操作
     */
    private String menuOperation;
}
