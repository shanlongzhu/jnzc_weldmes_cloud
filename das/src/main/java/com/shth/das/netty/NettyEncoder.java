package com.shth.das.netty;

import com.shth.das.common.DataInitialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.codec.binary.Hex;

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
        //服务端端口
        int serverPort = inetSocket.getPort();
        //端口为port，则为江南版OTC通讯协议
        if (serverPort == DataInitialization.getOtcPort()) {
            //单台设备睡眠250毫秒后再次下发
            Thread.sleep(250);
        }
        //端口为sxPort，则为松下通讯协议
        if (serverPort == DataInitialization.getSxPort()) {
            Thread.sleep(200);
        }
        //16进制字符串转byte数组
        byte[] bytes = Hex.decodeHex(str.toCharArray());
        byteBuf.writeBytes(bytes);
        byteBuf.release();
        ctx.flush();
    }
}
