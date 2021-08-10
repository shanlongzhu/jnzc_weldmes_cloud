package com.shth.das.common;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @description: 松下输入参数实体类
 * @author: Shan Long
 * @create: 2021-08-09
 */
@Data
public class JnSxDecoderParam {

    /**
     * 16进制字符串
     */
    private String str;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 通道环境
     */
    private ChannelHandlerContext ctx;

}
