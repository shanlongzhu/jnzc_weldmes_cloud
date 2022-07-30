package com.gw.common;

import lombok.Getter;

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

    /**
     * 字典表区域标识
     */
    public static final String AREA_FLAG = "16";

    /**
     * 字典表跨间标识
     */
    public static final String BAY_FLAG = "17";

    /**
     * 制造部层级标识
     */
    public static final Long MANUFACTUR_FLAG = 1L;

    /**
     * 集团层级标识
     */
    public static final Long GROUP_FLAG = 0L;

    /**
     * OTC焊机类型标识
     */
    public static final Integer MACHINE_TYPE_FLAG = 0;

    /**
     * OTC表名前缀标识
     */
    public static final String OTC_TABLE_NAME_PREFIX_FLAG = "otcrtd";

    /**
     * 松下表名前缀标识
     */
    public static final String SX_TABLE_NAME_PREFIX_FLAG = "sxrtd";


}
