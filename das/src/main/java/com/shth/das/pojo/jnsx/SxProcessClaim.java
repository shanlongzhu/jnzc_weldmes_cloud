package com.shth.das.pojo.jnsx;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 松下GL5系列工艺索取/工艺删除实体类
 * @author: Shan Long
 * @create: 2021-07-12
 */
@Data
public class SxProcessClaim implements Serializable {

    /**
     * 下发的焊机IP
     */
    private String weldIp;

    /**
     * 读写标志（0：主动上传；1：读取；2：设置；3：删除）
     */
    private int readWriteFlag;

    /**
     * 通道编号
     */
    private String channelNo;

}
