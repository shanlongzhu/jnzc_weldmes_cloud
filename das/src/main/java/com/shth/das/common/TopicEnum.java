package com.shth.das.common;

/**
 * mqtt主题枚举类
 *
 * @author zsl
 */
public enum TopicEnum {

    /**
     * 任务领取下发
     */
    taskClaimIssue,


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


    /**
     * 松下实时数据主题
     */
    sxrtdata,
    /**
     * 松下状态信息主题
     */
    sxStatusData,
    /**
     * 松下工艺下发回复
     */
    sxProcessReturn,
    /**
     * 松下焊接通道设定回复/读取回复
     */
    sxWeldChannelSetReturn,
    /**
     * 松下CO2工艺索取返回
     */
    sxCO2ProcessClaimReturn,
    /**
     * 松下TIG工艺索取返回
     */
    sxTIGProcessClaimReturn,
    /**
     * 松下工艺索取返回(无数据)
     */
    sxProcessClaimReturn,
    /**
     * 松下工艺删除返回
     */
    sxProcessDeleteReturn,

    /**
     * 松下CO2焊机工艺下发
     */
    sxCo2ProcessIssue,
    /**
     * 松下TIG焊机工艺下发
     */
    sxTigProcessIssue,
    /**
     * 松下焊接通道设定（解锁或锁定）/读取
     */
    sxWeldChannelSet,
    /**
     * 松下工艺索取
     */
    sxProcessClaim,

}
