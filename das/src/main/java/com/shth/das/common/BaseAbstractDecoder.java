package com.shth.das.common;

import com.shth.das.common.HandlerParam;
import io.netty.channel.ChannelHandlerContext;

/**
 * @description: 基类抽象方法
 * @author: Shan Long
 * @create: 2021-08-08
 */
public abstract class BaseAbstractDecoder {

    /**
     * 抽象方法
     *
     * @return Map
     */
    public abstract HandlerParam baseProtocolAnalysis(ChannelHandlerContext ctx, String str);

}
