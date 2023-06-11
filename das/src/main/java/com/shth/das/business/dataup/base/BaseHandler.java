package com.shth.das.business.dataup.base;

import com.shth.das.codeparam.HandlerParam;
import io.netty.channel.ChannelHandlerContext;

public abstract class BaseHandler {

    /**
     * 数据处理
     *
     * @param param
     */
    protected abstract void dataHandler(HandlerParam param);

    /**
     * 设备关机处理
     *
     * @param ctx
     */
    protected abstract void shutdownHandler(ChannelHandlerContext ctx);

}
