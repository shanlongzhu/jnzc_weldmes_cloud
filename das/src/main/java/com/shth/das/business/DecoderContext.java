package com.shth.das.business;

import com.shth.das.codeparam.HandlerParam;
import com.shth.das.common.BaseDecoder;
import io.netty.channel.ChannelHandlerContext;

/**
 * 策略模式，选择具体的协议解析类
 */
public class DecoderContext {

    private final BaseDecoder baseDecoder;

    public DecoderContext(BaseDecoder baseDecoder) {
        this.baseDecoder = baseDecoder;
    }

    public HandlerParam protocolAnalysisSelector(ChannelHandlerContext ctx, String str) {
        return this.baseDecoder.baseProtocolAnalysis(ctx, str);
    }

}
