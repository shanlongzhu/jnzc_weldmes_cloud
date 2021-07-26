package com.shth.das.common;

/**
 * 上行mqtt主题枚举类（das的mqtt客户端发布的主题，需要前端进行订阅）
 *
 * @author zsl
 */
public enum UpTopicEnum {

    /**
     * OTC实时数据主题
     */
    rtcdata,
    /**
     * OTC工艺下发返回
     */
    processIssueReturn,
    /**
     * OTC工艺索取返回
     */
    processClaimReturn,
    /**
     * OTC密码返回
     */
    passwordReturn,
    /**
     * OTC控制命令返回
     */
    commandReturn,


    /**
     * 松下实时数据主题
     */
    sxrtdata,
    /**
     * 松下状态信息主题
     */
    sxStatusData,
    /**
     * 松下GL5系列工艺下发回复
     */
    sxProcessReturn,
    /**
     * 松下GL5系列焊接通道设定回复/读取回复
     */
    sxWeldChannelSetReturn,
    /**
     * 松下GL5系列CO2工艺索取回复
     */
    sxCO2ProcessClaimReturn,
    /**
     * 松下GL5系列松下TIG工艺索取回复
     */
    sxTIGProcessClaimReturn,
    /**
     * 松下GL5系列松下工艺索取回复(无数据)
     */
    sxProcessClaimReturn,
    /**
     * 松下GL5系列松下工艺删除回复
     */
    sxProcessDeleteReturn,
    /**
     * 松下FR2系列通道参数查询（无参数）、下载、删除回复
     */
    sxChannelParamReply,
    /**
     * 松下FR2系列通道参数查询（有参数）
     */
    sxChannelParamReplyHave,

    /**
     * 松下AT3系列查询回复（有参数）
     */
    sxAt3ParamQueryReturn,

}
