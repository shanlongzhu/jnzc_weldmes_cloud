package com.shth.das.common;

/**
 * mqtt主题枚举类
 */
public enum TopicEnum {

    rtcdata,                //实时数据主题
    processIssueReturn,     //工艺下发返回
    processClaimReturn,     //工艺索取返回
    passwordReturn,         //密码返回
    commandReturn,          //控制命令返回

    processIssue,           //工艺下发
    processClaim,           //工艺索取
    passwordIssue,          //密码下发
    commandIssue;           //控制命令下发

}
