package com.shth.das.pojo.jnotc;

import lombok.Data;

import java.io.Serializable;

@Data
/**
 * 密码返回实体类
 */
public class JNPasswordReturn implements Serializable {

    /**
     * 采集编号
     */
    private String gatherNo;

    /**
     * 控制命令下发标志：0成功，1失败
     */
    private Integer flag;

}
