package com.shth.das.pojo.jnotc;

import lombok.Data;

import java.io.Serializable;

/**
 * 工艺索取规范实体类
 */
@Data
public class JNProcessClaim implements Serializable {

    //枚举类
    public enum Flag {
        //头部
        HEAD("007E0A010101"),
        //控制命令
        ORDER("56"),
        //尾部
        FOOT("007D");

        public final String value;

        Flag(String s) {
            this.value = s;
        }
    }

    /**
     * 采集编号
     */
    private String gatherNo;
    /**
     * 规范号/通道号[CPVE：1-30]
     */
    private String channelNo;

}
