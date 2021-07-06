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

    /**
     * 机构名称
     */
    private String name;

    /**
     * 上级机构ID，一级机构为0
     */
    private Long parentId;

    /**
     * 排序
     */
    private int orderNum;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新人
     */
    private String lastUpdateBy;

    /**
     * 更新时间
     */
    private String lastUpdateTime;

    /**
     * 是否删除  -1：已删除  0：正常
     */
    private int delFlag;

    /**
     * 机构列表
     */
    private List<SysDeptInfo> list;



}
