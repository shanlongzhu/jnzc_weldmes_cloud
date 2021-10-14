package com.shth.das.pojo.jnotc;

import lombok.Data;

/**
 * OTC下发程序包路径
 */
@Data
public class OtcV1IssueProgramPath {

    /**
     * 采集编号
     */
    private String gatherNo;

    /**
     * 端口
     */
    private String port;

    /**
     * 程序包路径
     */
    private String packagePath;

}
