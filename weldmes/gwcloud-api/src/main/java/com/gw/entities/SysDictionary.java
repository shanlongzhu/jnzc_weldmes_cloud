package com.gw.entities;

import lombok.Data;

/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  字典实体类
 * @Params
 */
@Data
public class SysDictionary{

    /**
     * 主键
     */
    private Long id;

    /**
     * 类型
     */
    private String type;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 值
     */
    private String value;

    /**
     * 描述
     */
    private String valueName;

    /**
     * 备注
     */
    private String remarks;

    private String valueNames;

    private String valueNamess;

    private String valueNamesss;
    private String valueNamessss;

}
