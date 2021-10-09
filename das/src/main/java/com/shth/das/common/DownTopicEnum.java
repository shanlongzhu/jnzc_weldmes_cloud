package com.shth.das.common;

/**
 * 下行mqtt主题枚举类（das的mqtt客户端需要订阅以下主题）
 */
public enum DownTopicEnum {

    /**
     * 任务领取下发
     */
    TaskClaimIssue("TaskClaimIssue"),

    /**
     * OTC（1.0）工艺下发
     */
    OtcV1ProcessIssue("OtcV1ProcessIssue"),
    /**
     * OTC（1.0）工艺索取
     */
    OtcV1ProcessClaim("OtcV1ProcessClaim"),
    /**
     * OTC（1.0）密码下发
     */
    OtcV1PasswordIssue("OtcV1PasswordIssue"),
    /**
     * OTC（1.0）控制命令下发
     */
    OtcV1CommandIssue("OtcV1CommandIssue"),

    /**
     * 松下GL5系列CO2焊机工艺下发
     */
    SxGl5Co2ProcessIssue("SxGl5Co2ProcessIssue"),
    /**
     * 松下GL5系列TIG焊机工艺下发
     */
    SxGl5TigProcessIssue("SxGl5TigProcessIssue"),
    /**
     * 松下GL5系列【通道设定、通道读取】
     */
    SxGl5WeldChannelSet("SxGl5WeldChannelSet"),
    /**
     * 松下GL5系列【工艺索取、工艺删除】
     */
    SxGl5ProcessClaim("SxGl5ProcessClaim"),
    /**
     * 松下【FR2、AT3】系列【通道参数查询、通道参数删除】
     */
    SxFr2ChannelParamQuery("SxFr2ChannelParamQuery"),
    /**
     * 松下FR2系列【通道参数下载】
     */
    SxFr2ChannelParamDownload("SxFr2ChannelParamDownload"),
    /**
     * 松下AT3系列【通道参数下载】
     */
    SxAt3ParamDownload("SxAt3ParamDownload");

    private final String value;

    DownTopicEnum(String name) {
        this.value = name;
    }

    public String getValue() {
        return value;
    }
}
