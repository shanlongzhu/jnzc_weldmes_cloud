package com.shth.das.common;

/**
 * mqtt主题枚举类
 *
 * @author zsl
 */
public enum TopicEnum {

    /**
     * OTC实时数据主题
     */
    rtcdata,
    /**
     * 工艺下发返回
     */
    processIssueReturn,
    /**
     * 工艺索取返回
     */
    processClaimReturn,
    /**
     * 密码返回
     */
    passwordReturn,
    /**
     * 控制命令返回
     */
    commandReturn,


    /**
     * 工艺下发
     */
    processIssue,
    /**
     * 工艺索取
     */
    processClaim,
    /**
     * 密码下发
     */
    passwordIssue,
    /**
     * 控制命令下发
     */
    commandIssue,

}
