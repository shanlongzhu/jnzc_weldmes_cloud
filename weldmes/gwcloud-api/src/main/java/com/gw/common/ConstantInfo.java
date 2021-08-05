package com.gw.common;

import lombok.Getter;

/**
 * @Author zhanghan
 * @Date 2021/7/5 14:46
 * @Description 常量信息类
 */
@Getter
public class ConstantInfo {

    /**
     * 菜单/按钮类型标识     1 -->菜单     2 -->按钮
     */
    public static final String BUTTON_FLAG = "2";

    /**
     * 管理员角色标识
     */
    public static final String ADMIN_FLAG = "admin";

    /**
     * 焊机绑定任务标识    1 -->有绑定     0 -->没有绑定
     */
    public static final int WELD_EXIST_FLAG = 1;

    /**
     * 焊机绑定任务标识    1 -->有绑定     0 -->没有绑定
     */
    public static final int WELD_NO_EXIST_FLAG = 0;

    /**
     * 字典表焊机设备类型标识
     */
    public static final String DICTIONARY_WELD_TYPE_FLAG = "3";
}
