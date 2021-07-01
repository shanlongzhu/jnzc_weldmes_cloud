package com.gw.entities;

import lombok.Data;

/**
 * @Author zhanghan
 * @Date 2021/6/25 16:23
 * @Description  前端角色 目录/菜单/按钮 信息
 */
@Data
public class MenuAndButtonInfo {

    //前端页面中的 目录/菜单/按钮 父级id  枚举类中配置  与前端保持一致
    private int parentId;

    //前端页面中的 目录/菜单/按钮 id      枚举类中配置  与前端保持一致
    private int id;

    //目录/菜单/按钮 名字
    private String label;

    //目录/菜单/按钮 标识                枚举类中配置  与前端保持一致
    private String mark;

    //目录/菜单/按钮 类型
    private String type;

}
