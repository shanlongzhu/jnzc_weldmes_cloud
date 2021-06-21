package com.shth.das.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
/**
 * 工艺下发返回实体类
 */
public class JNProcessIssueReturn implements Serializable {

    /**
     * 采集编号
     */
    private String gatherNo;
    /**
     * 规范号/通道号[CPVE：1-30]
     */
    private String channelNo;
    /**
     * 下发结果[0成功、1失败]
     */
    private Integer flag;

}
