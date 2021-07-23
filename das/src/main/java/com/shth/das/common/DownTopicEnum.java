package com.shth.das.common;

/**
 * 下行mqtt主题枚举类（das的mqtt客户端需要订阅以下主题）
 */
public enum DownTopicEnum {

    /**
     * 任务领取下发
     */
    taskClaimIssue,

    /**
     * OTC工艺下发
     */
    processIssue,
    /**
     * OTC工艺索取
     */
    processClaim,
    /**
     * OTC密码下发
     */
    passwordIssue,
    /**
     * OTC控制命令下发
     */
    commandIssue,
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

    /**
     * 松下FR2系列通道参数查询/删除
     */
    sxChannelParamQuery,
    /**
     * 松下FR2系列通道参数下载
     */
    sxChannelParamDownload,
}
