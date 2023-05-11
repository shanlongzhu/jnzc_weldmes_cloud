package com.shth.das.business.dataup;

import com.shth.das.codeparam.HandlerParam;
import io.netty.channel.ChannelHandlerContext;

/**
 * @description: 基类解析方法
 */
public abstract class BaseAnalysis {

    /**
     * 协议解析
     *
     * @param ctx 通道
     * @param str 16进制字符串
     * @return 对pojo对象封装
     */
    protected abstract HandlerParam protocolAnalysis(ChannelHandlerContext ctx, String str);

}
