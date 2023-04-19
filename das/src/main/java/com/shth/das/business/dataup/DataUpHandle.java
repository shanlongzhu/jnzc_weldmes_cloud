package com.shth.das.business.dataup;

import com.shth.das.codeparam.HandlerParam;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * 数据上行处理
 */
public interface DataUpHandle {

    /**
     * 自定义数据拆包逻辑
     */
    String unpack(Channel channel, ByteBuf byteBuf, List<Object> out);

    /**
     * 协议判定
     */
    String protocolDecide(String str);

    /**
     * 自定义协议解析逻辑
     *
     * @return
     */
    HandlerParam protocolAnalysis(ChannelHandlerContext ctx, String str);

    /**
     * 自定义业务处理逻辑
     */
    void protocolHandle(HandlerParam handlerParam);

}
