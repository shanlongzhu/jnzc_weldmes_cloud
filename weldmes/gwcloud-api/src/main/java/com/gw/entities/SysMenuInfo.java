package com.gw.entities;

import lombok.Data;

import java.util.List;


/**
 * @Author zhanghan
 * @Date 2021/6/7 13:07
 * @Description  系统菜单信息类
 */
@Data
public class SysMenuInfo {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父菜单ID，一级菜单为0
     */
    private Long parentId;

    /**
     * 菜单URL
     */
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:view,user:create)
     */
    private String perms;

    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    private int type;

    /**
     * 菜单图标
     */
    private String icon;

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
     * 是否删除 -1：已删除  0：正常
     */
    private int del_flag;

    /**
     * 二级菜单信息
     */
    private List<SysMenuInfo> secondMenuInfos;

    /**
     * 子菜单信息
     */
    private List<SysMenuInfo> childrenMenuInfos;

    /**
     * 按钮信息
     */
    private List<SysMenuInfo> menuButtonInfos;
}
