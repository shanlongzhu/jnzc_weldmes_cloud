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
    OtcV1RtData("OtcV1RtData"),
    /**
     * OTC（1.0）工艺下发返回
     */
    OtcV1ProcessIssueReturn("OtcV1ProcessIssueReturn"),
    /**
     * OTC（1.0）工艺索取返回
     */
    OtcV1ProcessClaimReturn("OtcV1ProcessClaimReturn"),
    /**
     * OTC（1.0）密码返回
     */
    OtcV1PasswordReturn("OtcV1PasswordReturn"),
    /**
     * OTC（1.0）控制命令返回
     */
    OtcV1CommandReturn("OtcV1CommandReturn"),


    /**
     * 松下实时数据主题【GL5、FR2】
     */
    SxRtData("SxRtData"),
    /**
     * 松下状态信息主题【GL5、FR2】
     */
    SxStatusData("SxStatusData"),
    /**
     * 松下GL5系列工艺下发返回
     */
    SxGL5ProcessIssueReturn("SxGL5ProcessIssueReturn"),
    /**
     * 松下GL5系列焊机通道【通道设定返回、通道读取返回】
     */
    SxGL5WeldChannelSetOrReadReturn("SxGL5WeldChannelSetOrReadReturn"),
    /**
     * 松下GL5系列CO2工艺索取返回（有数据）
     */
    SxGL5CO2ProcessClaimReturn("SxGL5CO2ProcessClaimReturn"),
    /**
     * 松下焊机GL5系列TIG工艺索取返回（有数据）
     */
    SxGL5TIGProcessClaimReturn("SxGL5TIGProcessClaimReturn"),
    /**
     * 松下GL5系列工艺索取返回(无数据)
     */
    SxGL5ProcessClaimReturn("SxGL5ProcessClaimReturn"),
    /**
     * 松下GL5系列工艺删除返回
     */
    SxGL5ProcessDeleteReturn("SxGL5ProcessDeleteReturn"),

    /**
     * 松下焊机【FR2、AT3】系列通道参数【通道参数查询回复（无参数）、通道参数下载回复、通道参数删除回复】
     */
    SxFR2OrAT3ChannelParamReply("SxFR2OrAT3ChannelParamReply"),
    /**
     * 松下FR2系列通道参数【通道参数查询回复（有参数）】
     */
    SxFR2ChannelParamReplyHave("SxFR2ChannelParamReplyHave"),
    /**
     * 松下焊机AT3系列【通道参数查询回复（有参数）】
     */
    SxAT3ParamQueryReturn("SxAT3ParamQueryReturn");

    private final String value;

    UpTopicEnum(String name) {
        this.value = name;
    }

    public String getValue() {
        return value;
    }

}
