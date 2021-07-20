package com.gw.entities;

import lombok.Data;

/**
 * @Author zhanghan
 * @Date 2021/7/20 15:52
 * @Description 接口信息实体类
 */
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
