package com.shth.das.netty;

import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * netty编码器，将16进制字符串转ByteBuf
 */
public class NettyEncoder extends MessageToByteEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String str, ByteBuf byteBuf) throws Exception {
        //单台设备睡眠250毫秒后再次下发
        Thread.sleep(250);
        //InetSocketAddress insocket = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
        //String clientIp = insocket.getAddress().getHostAddress();
        //System.out.println("encode--->" + clientIp + ":" + DateTimeUtils.getNowDateTime());
        //16进制字符串转byte数组
        byte[] bytes = CommonUtils.hexStringToByte(str);
        byteBuf.writeBytes(bytes);
    }
}
