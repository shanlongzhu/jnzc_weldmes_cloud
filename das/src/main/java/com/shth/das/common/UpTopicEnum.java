package com.shth.das.common;

/**
 * 上行mqtt主题枚举类（das的mqtt客户端发布的主题，需要前端进行订阅）
 *
 * @author zsl
 */
public enum UpTopicEnum {

    /**
     * OTC（1.0）实时数据主题
     */
    rtcdata,
    /**
     * OTC（1.0）工艺下发返回
     */
    processIssueReturn,
    /**
     * OTC（1.0）工艺索取返回
     */
    processClaimReturn,
    /**
     * OTC（1.0）密码返回
     */
    passwordReturn,
    /**
     * OTC（1.0）控制命令返回
     */
    commandReturn,


    /**
     * 松下实时数据主题【GL5、FR2】
     */
    sxrtdata,
    /**
     * 松下状态信息主题【GL5、FR2】
     */
    sxStatusData,
    /**
     * 松下GL5系列工艺下发返回
     */
    sxProcessReturn,
    /**
     * 松下GL5系列焊机通道【通道设定返回、通道读取返回】
     */
    sxWeldChannelSetReturn,
    /**
     * 松下GL5系列CO2工艺索取返回（有数据）
     */
    sxCO2ProcessClaimReturn,
    /**
     * 松下焊机GL5系列TIG工艺索取返回（有数据）
     */
    sxTIGProcessClaimReturn,
    /**
     * 松下GL5系列工艺索取返回(无数据)
     */
    sxProcessClaimReturn,
    /**
     * 松下GL5系列工艺删除返回
     */
    sxProcessDeleteReturn,
    /**
     * 松下焊机【FR2、AT3】系列通道参数【通道参数查询回复（无参数）、通道参数下载回复、通道参数删除回复】
     */
    sxChannelParamReply,
    /**
     * 松下FR2系列通道参数【通道参数查询回复（有参数）】
     */
    sxChannelParamReplyHave,
    /**
     * 松下焊机AT3系列【通道参数查询回复（有参数）】
     */
    sxAt3ParamQueryReturn,

}
