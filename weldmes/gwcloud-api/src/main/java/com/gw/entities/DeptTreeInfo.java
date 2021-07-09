package com.gw.entities;

import lombok.Data;

/**
 * @Author zhanghan
 * @Date 2021/7/9 13:12
 * @Description 部门树状信息实体类
 */
@Data
public class DeptTreeInfo {

    /**
     * 部门id
     */
    private Long id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 父级部门名称
     */
    private String parentName;

    /**
     * 子部门id
     */
    private Long childrenId;

    /**
     * 子部门名称
     */
    private String childrenName;
}
