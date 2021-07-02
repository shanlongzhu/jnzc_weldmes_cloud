package com.gw.common;

/**
 * @Author zhanghan
 * @Date 2021/6/9 14:24
 * @Description 菜单枚举类
 */
public enum MenuEnum {

    CATALOGUE_FLAG(0,"目录"),
    CHILDREN_MENU_FLAG(1,"子菜单"),
    SECOND_MENU_FLAG(2,"二级菜单"),
    BUTTON_FLAG(3,"功能按钮");

    /**
     * 菜单类型标识
     */
    private int id;

    /**
     * 类型名称
     */
    private String typeName;

    MenuEnum(int id,String typeName){
        this.typeName=typeName;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
