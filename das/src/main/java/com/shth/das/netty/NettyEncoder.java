package com.shth.das.netty;

import com.shth.das.common.ServerPort;
import com.shth.das.util.CommonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.net.InetSocketAddress;

/**
 * netty编码器，将16进制字符串转ByteBuf
 *
 * @author zsl
 */
public class NettyEncoder extends MessageToByteEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext ctx, String str, ByteBuf byteBuf) throws Exception {
        InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
        int serverPort = inetSocket.getPort();//服务端端口
        //端口为port，则为江南版OTC通讯协议
        if (serverPort == ServerPort.otcPort) {
            //单台设备睡眠250毫秒后再次下发
            Thread.sleep(250);
        }
        //16进制字符串转byte数组
        byte[] bytes = CommonUtils.hexStringToByte(str);
        byteBuf.writeBytes(bytes);
    }
}
