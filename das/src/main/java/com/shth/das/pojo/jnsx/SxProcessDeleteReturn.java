package com.shth.das.pojo.jnsx;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 松下GL5系列工艺删除实体类
 * @author: Shan Long
 * @create: 2021-07-13
 */
@Data
public class SxProcessDeleteReturn implements Serializable {

    /**
     * 设备CID
     */
    private String weldCid;
    /**
     * IP地址
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
}
