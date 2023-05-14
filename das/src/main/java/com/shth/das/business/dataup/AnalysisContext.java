package com.shth.das.business.dataup;


import com.shth.das.business.dataup.otc.JnOtcProtocolAnalysis;
import com.shth.das.business.dataup.sx.JnSxProtocolAnalysis;
import com.shth.das.codeparam.HandlerParam;
import com.shth.das.common.CommonFunction;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.ObjectUtils;

import java.net.InetSocketAddress;

public class AnalysisContext {

    private BaseAnalysis baseAnalysis;

    public HandlerParam protocolAnalysis(ChannelHandlerContext ctx, String str) {
        InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
        this.baseAnalysis = getBaseAnalysis(inetSocket.getPort());
        if (ObjectUtils.isEmpty(this.baseAnalysis)) {
            return null;
        }
        return this.baseAnalysis.protocolAnalysis(ctx, str);
    }

    private BaseAnalysis getBaseAnalysis(int serverPort) {
        if (ObjectUtils.isNotEmpty(this.baseAnalysis)) {
            return this.baseAnalysis;
        }
        if (serverPort == CommonFunction.getOtcPort()) {
            return new JnOtcProtocolAnalysis();
        }
        //端口为sxPort，则为松下通讯协议
        else if (serverPort == CommonFunction.getSxPort()) {
            return new JnSxProtocolAnalysis();
        }
        return null;
    }

}
