package com.shth.das.common;

import com.shth.das.codeparam.HandlerParam;
import io.netty.channel.ChannelHandlerContext;

/**
 * @description: 基类解析方法
 * @author: Shan Long
 * @create: 2021-08-08
 */
public interface BaseDecoder {

    /**
     * @param ctx 通道
     * @param str 16进制字符串
     * @return 对pojo对象封装
     */
    HandlerParam baseProtocolAnalysis(ChannelHandlerContext ctx, String str);

}
