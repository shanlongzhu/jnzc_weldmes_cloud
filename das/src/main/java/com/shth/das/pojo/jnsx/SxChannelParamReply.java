package com.shth.das.pojo.jnsx;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 松下FR2系列（AT3）通道参数查询（无参数）、下载、删除回复
 * @author: Shan Long
 * @create: 2021-07-22
 */
@Data
public class SxChannelParamReply implements Serializable {

    /**
     * 焊机IP
     */
    private String weldIp;
    /**
     * 控制（1.查询，2.下载，3.删除）
     */
    private int command;
    /**
     * 通道号
     */
    private String channel;

}
