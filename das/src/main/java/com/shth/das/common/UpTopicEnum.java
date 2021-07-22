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
     * 松下工艺下发回复
     */
    sxProcessReturn,
    /**
     * 松下焊接通道设定回复/读取回复
     */
    sxWeldChannelSetReturn,
    /**
     * 松下CO2工艺索取回复
     */
    sxCO2ProcessClaimReturn,
    /**
     * 松下TIG工艺索取回复
     */
    sxTIGProcessClaimReturn,
    /**
     * 松下工艺索取回复(无数据)
     */
    sxProcessClaimReturn,
    /**
     * 松下工艺删除回复
     */
    sxProcessDeleteReturn,

}
