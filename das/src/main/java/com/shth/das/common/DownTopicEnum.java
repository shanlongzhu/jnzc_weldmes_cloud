package com.shth.das.common;

/**
 * 下行mqtt主题枚举类（das的mqtt客户端需要订阅以下主题）
 */
public enum DownTopicEnum {

    /**
     * 任务领取下发
     */
    jnTaskClaimIssue,

    /**
     * OTC（1.0）工艺下发
     */
    jnOtcV1ProcessIssue,
    /**
     * OTC（1.0）工艺索取
     */
    jnOtcV1ProcessClaim,
    /**
     * OTC（1.0）密码下发
     */
    jnOtcV1PasswordIssue,
    /**
     * OTC（1.0）控制命令下发
     */
    jnOtcV1CommandIssue,

    /**
     * 松下GL5系列CO2焊机工艺下发
     */
    jnSxGl5Co2ProcessIssue,
    /**
     * 松下GL5系列TIG焊机工艺下发
     */
    jnSxGl5TigProcessIssue,
    /**
     * 松下GL5系列【通道设定、通道读取】
     */
    jnSxGl5WeldChannelSet,
    /**
     * 松下GL5系列【工艺索取、工艺删除】
     */
    jnSxGl5ProcessClaim,
    /**
     * 松下【FR2、AT3】系列【通道参数查询、通道参数删除】
     */
    jnSxFr2ChannelParamQuery,
    /**
     * 松下FR2系列【通道参数下载】
     */
    jnSxFr2ChannelParamDownload,
    /**
     * 松下AT3系列【通道参数下载】
     */
    jnSxAt3ParamDownload,

}
