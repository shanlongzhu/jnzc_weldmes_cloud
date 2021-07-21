package com.shth.das.pojo.jnotc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("ALL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
/**
 * 前端实时数据展示的实体类
 */
public class JNRtDataUI implements Serializable {

    /**
     * 采集编号
     */
    private String gatherNo;

    /**
     * 焊机状态（-1关机，0待机，3、5、7焊接，其他：故障）
     */
    private Integer weldStatus;

    /**
     * 电流
     */
    private BigDecimal electricity;

    /**
     * 电压
     */
    private BigDecimal voltage;

    /**
     * 焊机IP
     */
    private String weldIp;

    /**
     * 焊接时间
     */
    private String weldTime;

    /**
     * 送丝速度
     */
    private BigDecimal wireFeedRate;
}
