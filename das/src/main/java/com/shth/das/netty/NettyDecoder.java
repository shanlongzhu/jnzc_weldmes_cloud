package com.shth.das.netty;

import com.shth.das.business.JnRtDataProtocol;
import com.shth.das.common.ServerPort;
import com.shth.das.util.CommonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Netty解码器：ByteBuf转Java对象
 *
 * @author zsl
 */
public class NettyDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        //创建字节数组,buffer.readableBytes可读字节长度
        byte[] b = new byte[buffer.readableBytes()];
        //复制内容到字节数组b
        buffer.readBytes(b);
        //字节数组转16进制字符串
        String str = CommonUtils.bytesToHexString(b);
        InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
        //服务端端口
        int serverPort = inetSocket.getPort();
        Map<String, Object> map = new HashMap<>(8);
        //端口为otcPort，则为江南版OTC通讯协议
        if (serverPort == ServerPort.otcPort) {
            map = JnRtDataProtocol.jnRtDataDecoderManage(str);
        }
        out.add(map);
    }
}
