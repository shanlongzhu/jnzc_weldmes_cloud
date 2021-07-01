package com.gw.entities;

import lombok.Data;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/29 9:12
 * @Description  部门信息实体类
 */
@Data
public class SysDeptInfo {

    /**
     * id
     */
    private Long id;

    private String name;

    private Long parentId;

    private int orderNum;

    private String createBy;

    private String createTime;

    private String lastUpdateBy;

    private String lastUpdateTime;

    private int delFlag;

    private List<SysDeptInfo> list;



}
