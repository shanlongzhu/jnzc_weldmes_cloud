package com.shth.das.common;

import io.netty.channel.ChannelHandlerContext;

/**
 * @description: 基类抽象方法
 * @author: Shan Long
 * @create: 2021-08-08
 */
public abstract class BaseAbstractDecoder {

    /**
     * 抽象方法,由子类实现
     *
     * @param ctx 通道
     * @param str 16进制字符串
     * @return 对pojo对象封装
     */
    public abstract HandlerParam baseProtocolAnalysis(ChannelHandlerContext ctx, String str);

}
