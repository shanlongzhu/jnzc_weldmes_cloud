package com.shth.das.pojo.jnotc;

import lombok.Data;

/**
 * OTC程序包路径下发返回
 */
@Data
public class OtcV1ProgramPathIssueReturn {

    /**
     * 采集编号
     */
    private String gatherNo;

    /**
     * 状态（0：成功）
     */
    private Integer status;

}
