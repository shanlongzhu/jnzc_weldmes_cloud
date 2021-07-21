package com.shth.das.pojo.jnotc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@SuppressWarnings("ALL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
/**
 * 控制命令返回实体类
 */
public class JNCommandReturn implements Serializable {

    /**
     * 采集编号
     */
    private String gatherNo;

    /**
     * 控制命令下发标志：0成功，1失败
     */
    private Integer flag;
}
