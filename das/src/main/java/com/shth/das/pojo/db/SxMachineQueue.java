package com.shth.das.pojo.db;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 松下设备队列的实体类
 * @author: Shan Long
 * @create: 2021-08-18
 */
@Data
public class SxMachineQueue implements Serializable {

    /**
     * 设备CID（设备唯一标识）
     */
    private String weldCid;
    /**
     * 采集盒IP地址
     */
    private String weldIp;
    /**
     * 焊接时间
     */
    private String weldTime;

}
