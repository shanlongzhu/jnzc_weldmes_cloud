package com.shth.das.pojo.jnotc;

import lombok.Data;

import java.io.Serializable;


/**
 * 控制命令下发实体类
 */
@Data
public class JNCommandIssue implements Serializable {

    //枚举类
    public enum Flag {
        //头部
        HEAD("007E00000000"),
        //控制命令
        ORDER("54"),
        //尾部
        FOOT("007D");

        public final String value;

        Flag(String s) {
            this.value = s;
        }
    }

    //采集编号
    private String gatherNo;

    //控制命令
    private String command;

}
