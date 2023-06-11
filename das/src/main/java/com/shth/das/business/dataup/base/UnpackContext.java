package com.shth.das.business.dataup.base;

import com.google.common.collect.Lists;
import com.shth.das.business.dataup.base.BaseUnpack;
import com.shth.das.business.dataup.otc.JnOtcDecoderUnpack;
import com.shth.das.business.dataup.sx.JnSxDecoderUnpack;
import com.shth.das.common.CommonFunction;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.ObjectUtils;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 策略模式，选择具体的协议解析类
 */
public class UnpackContext {

    private BaseUnpack baseUnpack;

    /**
     * 数据拆包，并解析成16进制字符串
     *
     * @param ctx
     * @param byteBuf
     */
    public List<String> protocolUnpack(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
        this.baseUnpack = getBaseUnpack(inetSocket.getPort());
        if (ObjectUtils.isEmpty(this.baseUnpack)) {
            return Lists.newArrayList();
        }
        return this.baseUnpack.dataUnpack(byteBuf);
    }


    private BaseUnpack getBaseUnpack(int serverPort) {
        if (ObjectUtils.isNotEmpty(this.baseUnpack)) {
            return this.baseUnpack;
        }
        if (serverPort == CommonFunction.getOtcPort()) {
            return new JnOtcDecoderUnpack();
        }
        //端口为sxPort，则为松下通讯协议
        else if (serverPort == CommonFunction.getSxPort()) {
            return new JnSxDecoderUnpack();
        }
        return null;
    }

}
