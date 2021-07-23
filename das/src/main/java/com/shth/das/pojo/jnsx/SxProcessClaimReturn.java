package com.shth.das.pojo.jnsx;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 松下GL5系列工艺索取返回实体类（默认无数据）
 * @author: Shan Long
 * @create: 2021-07-12
 */
@Data
public class SxProcessClaimReturn implements Serializable {

    /**
     * 下发的焊机IP
     */
    private String weldIp;
    /**
     * 焊机时间
     */
    private String weldTime;
    /**
     * 设备类型
     */
    private Integer weldType;
    /**
     * 通道编号
     */
    private String channelNo;
    /**
     * 数据标志（1：有数据；0：无数据）
     */
    private int dataFlag;

}
