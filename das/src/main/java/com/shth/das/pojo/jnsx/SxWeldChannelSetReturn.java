package com.shth.das.pojo.jnsx;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 松下焊机或通道设定回复/读取回复
 * @author: Shan Long
 * @create: 2021-07-08
 */
@Data
public class SxWeldChannelSetReturn implements Serializable {

    /**
     * 焊机IP
     * 共有
     */
    private String weldIp;

    /**
     * 焊机时间
     * 共有
     */
    private String weldTime;

    /**
     * 读写标志（0：主动上传；1：读取；2：设置；3：删除）
     * 共有
     */
    private int readWriteFlag;

    /**
     * 设备类型
     * 共有
     */
    private int weldType;

    /**
     * 功能（0：解锁；1：锁定）
     * 仅读取回复有
     */
    private int function;

    /**
     * 通道选择（0：焊接通道；1：PC通道）
     * 仅读取回复有
     */
    private int channelSelect;

}
