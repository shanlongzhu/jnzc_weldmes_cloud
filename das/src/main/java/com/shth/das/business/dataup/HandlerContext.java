package com.shth.das.business.dataup;

import com.shth.das.business.dataup.otc.JnOtcProtocolHandle;
import com.shth.das.business.dataup.sx.JnSxProtocolHandle;
import com.shth.das.codeparam.HandlerParam;
import com.shth.das.common.CommonFunction;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.net.InetSocketAddress;

@Slf4j
public class HandlerContext {

    private BaseHandler baseHandler;

    public void dataHandler(ChannelHandlerContext ctx, HandlerParam param) {
        InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
        this.baseHandler = getBaseHandler(inetSocket.getPort());
        if (ObjectUtils.isEmpty(this.baseHandler)) {
            return;
        }
        this.baseHandler.dataHandler(param);
    }

    public void shutdownHandle(ChannelHandlerContext ctx) {
        InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
        this.baseHandler = getBaseHandler(inetSocket.getPort());
        if (ObjectUtils.isEmpty(this.baseHandler)) {
            return;
        }
        this.baseHandler.shutdownHandler(ctx);
    }

    private BaseHandler getBaseHandler(int serverPort) {
        if (ObjectUtils.isNotEmpty(this.baseHandler)) {
            return this.baseHandler;
        }
        if (serverPort == CommonFunction.getOtcPort()) {
            return new JnOtcProtocolHandle();
        }
        //端口为sxPort，则为松下通讯协议
        else if (serverPort == CommonFunction.getSxPort()) {
            return new JnSxProtocolHandle();
        }
        return null;
    }

}
