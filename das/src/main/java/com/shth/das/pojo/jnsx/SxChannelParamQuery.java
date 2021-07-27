package com.shth.das.pojo.jnsx;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 松下FR2系列（At3）通道参数查询/删除
 * @author: Shan Long
 * @create: 2021-07-22
 */
@Data
public class SxChannelParamQuery implements Serializable {

    /**
     * 焊机IP
     */
    private String weldIp;
    /**
     * 控制（1.查询，2.下载，3.删除）
     */
    private int command;
    /**
     * 要查询的通道
     */
    private String channel;

}
