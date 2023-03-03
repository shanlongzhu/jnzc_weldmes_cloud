package com.shth.das.pojo.jnsx;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 松下GL5系列焊机或焊接通道设定（锁定或解锁），和下行-读取公用此类
 * @author: Shan Long
 * @create: 2021-07-08
 */
@Data
public class SxWeldChannelSetting implements Serializable {

    /**
     * 设备CID
     */
    private String weldCid;

    /**
     * 读写标志（0：主动上传；1：读取；2：设置；3：删除）
     * 共有
     */
    private Integer readWriteFlag;

    /**
     * 功能（0：解锁；1：锁定）
     * 仅通道设定有
     */
    private Integer function;

    /**
     * 通道选择（0：焊接通道；1：PC通道）
     * 仅通道设定有
     */
    private Integer channelSelect;
}
