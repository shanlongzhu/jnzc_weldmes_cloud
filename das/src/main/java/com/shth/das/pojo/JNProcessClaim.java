package com.shth.das.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 工艺索取规范实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class JNProcessClaim implements Serializable {

    //枚举类
    public enum Flag {
        //头部
        HEAD("007E00000000"),
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
