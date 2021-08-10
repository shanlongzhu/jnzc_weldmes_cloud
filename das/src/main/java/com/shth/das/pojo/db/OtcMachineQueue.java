package com.shth.das.pojo.db;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: OTC设备存储队列实体类
 * @author: Shan Long
 * @create: 2021-08-02
 */
@Data
public class OtcMachineQueue implements Serializable {

    /**
     * 采集编号
     */
    private String gatherNo;
    /**
     * 采集盒IP地址
     */
    private String weldIp;

    private String weldTime;

}
