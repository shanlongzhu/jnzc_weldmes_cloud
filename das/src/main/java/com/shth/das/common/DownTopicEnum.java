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
     * OTC（1.0）工艺下发
     */
    processIssue,
    /**
     * OTC（1.0）工艺索取
     */
    processClaim,
    /**
     * OTC（1.0）密码下发
     */
    passwordIssue,
    /**
     * OTC（1.0）控制命令下发
     */
    commandIssue,

    /**
     * 松下GL5系列CO2焊机工艺下发
     */
    sxGl5Co2ProcessIssue,
    /**
     * 松下GL5系列TIG焊机工艺下发
     */
    sxGl5TigProcessIssue,
    /**
     * 松下GL5系列【通道设定、通道读取】
     */
    sxGl5WeldChannelSet,
    /**
     * 松下GL5系列【工艺索取、工艺删除】
     */
    sxGl5ProcessClaim,
    /**
     * 松下【FR2、AT3】系列【通道参数查询、通道参数删除】
     */
    sxFr2ChannelParamQuery,
    /**
     * 松下FR2系列【通道参数下载】
     */
    sxFr2ChannelParamDownload,
    /**
     * 松下AT3系列【通道参数下载】
     */
    sxAt3ParamDownload,

}
