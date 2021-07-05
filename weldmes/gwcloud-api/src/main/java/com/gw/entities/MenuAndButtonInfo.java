package com.gw.entities;

import lombok.Data;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/25 16:23
 * @Description   目录/菜单/按钮 信息实体类
 */
@Data
public class MenuAndButtonInfo {

    /**
     * 目录/菜单/按钮 父级id
     */
    private Long parentId;

    /**
     * 目录/菜单/按钮 id
     */
    private Long id;

    /**
     * 目录/菜单/按钮 名字
     */
    private String label;

    /**
     * 目录/菜单/按钮 标识
     */
    private String mark;

    /**
     * 目录/菜单/按钮 类型
     */
    private String type;

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
     * 子菜单列表
     */
    List<MenuAndButtonInfo> menus;

    /**
     * 三级菜单列表
     */
    List<MenuAndButtonInfo> threeLevelMenuInfos;

    /**
     * 按钮列表
     */
    List<MenuAndButtonInfo> buttons;

}
