package com.shth.das.netty;

import com.shth.das.business.JnRtDataProtocol;
import com.shth.das.business.SxRtDataProtocol;
import com.shth.das.common.DataInitialization;
import com.shth.das.util.CommonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.codec.binary.Hex;

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

    private final JnRtDataProtocol jnRtDataProtocol = new JnRtDataProtocol();
    private final SxRtDataProtocol sxRtDataProtocol = new SxRtDataProtocol();

    /**
     * 用来临时保留没有处理过的请求报文
     */
    private final ByteBuf tempMsg = Unpooled.buffer();

    /**
     * key:包头固定值
     * value：字节长度
     */
    private final Map<String, Integer> sxMap = new HashMap<>();

    NettyDecoder() {
        //第一次验证
        this.sxMap.put("4E455430", 21);
        //第二次验证
        this.sxMap.put("4C4A5348", 128);
        //其他
        this.sxMap.put("FE5AA5", 0);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        // 合并报文
        ByteBuf message = null;
        int tmpMsgSize = this.tempMsg.readableBytes();
        // 如果暂存有上一次余下的请求报文，则合并
        if (tmpMsgSize > 0) {
            message = Unpooled.buffer();
            message.writeBytes(this.tempMsg);
            message.writeBytes(byteBuf);
            //读取完之后清空
            this.tempMsg.clear();
        } else {
            message = byteBuf;
        }
        //数据可读长度大于0才进行读取
        if (message.readableBytes() > 0) {
            InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
            //服务端端口
            int serverPort = inetSocket.getPort();
            //端口为otcPort，则为江南版OTC通讯协议
            if (serverPort == DataInitialization.otcPort) {
                otcRecursionReadBytes(ctx, message, out);
            }
            //端口为sxPort，则为松下通讯协议
            if (serverPort == DataInitialization.sxPort) {
                sxRecursionReadBytes(ctx, message, out);
            }
        }
    }

    /**
     * 递归读取ByteBuf
     *
     * @param ctx     通道
     * @param message ByteBuf
     * @param out     List
     */
    private void otcRecursionReadBytes(ChannelHandlerContext ctx, ByteBuf message, List<Object> out) {
        int bufNum = message.readableBytes();
        //可读长度小于2，先暂存，大于2则进行读取
        if (bufNum < 2) {
            this.tempMsg.writeBytes(message.readBytes(bufNum));
        } else {
            byte[] headBytes = new byte[1];
            headBytes[0] = message.getByte(0);
            //头部1个字节
            String header = CommonUtils.bytesToHexString(headBytes);
            if ("7E".equals(header)) {
                //查看包长度
                byte[] lengthBytes = new byte[1];
                //查看第2字节的数据包长度
                lengthBytes[0] = message.getByte(1);
                //数据包长度(数组转16进制再转10进制)
                int length = Integer.valueOf(Hex.encodeHexString(lengthBytes), 16) + 2;
                //判断可读长度是否多于数据包长度（是否是一个完整数据包）
                if (length > 0 && bufNum >= length) {
                    //将一个完整数据包读取到bytes数组中
                    byte[] bytes = new byte[length];
                    message.readBytes(bytes);
                    //解析完整数据包成16进制
                    String str = CommonUtils.bytesToHexString(bytes);
                    Map<String, Object> map = this.jnRtDataProtocol.jnRtDataDecoderManage(ctx, str);
                    if (map.size() > 0) {
                        out.add(map);
                    }
                    this.otcRecursionReadBytes(ctx, message, out);
                } else {
                    //剩下来的数据放到tempMsg暂存
                    this.tempMsg.writeBytes(message.readBytes(bufNum));
                }
            }
        }
    }

    /**
     * 递归读取ByteBuf
     *
     * @param ctx     通道
     * @param message ByteBuf
     * @param out     List
     */
    private void sxRecursionReadBytes(ChannelHandlerContext ctx, ByteBuf message, List<Object> out) {
        int bufNum = message.readableBytes();
        //判断可读字节数小于5，直接存储，下次读取
        if (bufNum <= 5) {
            this.tempMsg.writeBytes(message.readBytes(bufNum));
        } else {
            //查看前4个字节
            byte[] headBytes = new byte[4];
            for (int index = 0; index < 4; index++) {
                headBytes[index] = message.getByte(index);
            }
            //头部4个字节
            String header = CommonUtils.bytesToHexString(headBytes);
            //判断是否为两次握手验证
            if (this.sxMap.containsKey(header)) {
                //获得数据包长度
                Integer length = this.sxMap.get(header);
                //可读字节是否多于数据包长度（是否为完整包）
                if (length > 0 && bufNum >= length) {
                    byte[] bytes = new byte[length];
                    message.readBytes(bytes);
                    String str = CommonUtils.bytesToHexString(bytes);
                    Map<String, Object> map = this.sxRtDataProtocol.sxRtDataDecoderManage(ctx, str);
                    if (map.size() > 0) {
                        out.add(map);
                    }
                    this.sxRecursionReadBytes(ctx, message, out);
                } else {
                    this.tempMsg.writeBytes(message.readBytes(bufNum));
                }
            }
            //非两次握手，正常通讯协议
            else {
                //查看前3个字节
                byte[] headByte = new byte[3];
                for (int index = 0; index < 3; index++) {
                    headByte[index] = message.getByte(index);
                }
                //头部3个字节
                String head = CommonUtils.bytesToHexString(headByte);
                if (this.sxMap.containsKey(head)) {
                    //查看包长度
                    byte[] lengthBytes = new byte[2];
                    //查看第4、5字节的数据包长度
                    lengthBytes[0] = message.getByte(3);
                    lengthBytes[1] = message.getByte(4);
                    //数据包长度(字节长度)
                    int length = Integer.valueOf(Hex.encodeHexString(lengthBytes), 16);
                    //判断可读长度是否多于数据包长度（是否是一个完整数据包）
                    if (length > 0 && bufNum >= length) {
                        //将一个完整数据包读取到bytes数组中
                        byte[] bytes = new byte[length];
                        message.readBytes(bytes);
                        //解析完整数据包成16进制
                        String str = CommonUtils.bytesToHexString(bytes);
                        Map<String, Object> map = this.sxRtDataProtocol.sxRtDataDecoderManage(ctx, str);
                        if (map.size() > 0) {
                            out.add(map);
                        }
                        this.sxRecursionReadBytes(ctx, message, out);
                    } else {
                        //不是完整包，进行暂存
                        this.tempMsg.writeBytes(message.readBytes(bufNum));
                    }
                }
            }
        }
    }
}
