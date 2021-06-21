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
 * 密码下发实体类
 */
public class JNPasswordIssue implements Serializable {

    //枚举类
    public enum Flag {
        //头部
        HEAD("007E00000000"),
        //控制命令
        ORDER("53"),
        //尾部
        FOOT("007D");

        public final String value;

        Flag(String s) {
            this.value = s;
        }
    }

    private String gatherNo; //采集编号

    private String password; //密码[1~999]，默认：1

}
