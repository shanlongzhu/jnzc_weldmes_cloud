package com.shth.das.netty;

import com.shth.das.common.HandlerParam;
import com.shth.das.business.JnOtcDecoderAnalysis;
import com.shth.das.business.JnSxDecoderAnalysis;
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

    private final JnOtcDecoderAnalysis jnOtcDecoderAnalysis = new JnOtcDecoderAnalysis();
    private final JnSxDecoderAnalysis jnSxDecoderAnalysis = new JnSxDecoderAnalysis();

    /**
     * 用来临时保留没有处理过的请求报文
     */
    private final ByteBuf tempMsg = Unpooled.buffer(1024);

    /**
     * key:包头固定值
     * value：字节长度
     */
    private final Map<String, Integer> otcMap = new HashMap<>();
    private final Map<String, Integer> sxMap = new HashMap<>();

    NettyDecoder() {
        //OTC通讯协议固定头部
        this.otcMap.put("7E", 1);
        //松下第一次握手验证
        this.sxMap.put("4E455430", 21);
        //松下第二次握手验证
        this.sxMap.put("4C4A5348", 64);
        //其他（握手成功后正常传输的通讯协议）
        this.sxMap.put("FE5AA5", 0);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        // 合并报文
        ByteBuf message = Unpooled.buffer(1024);
        int tmpMsgSize = this.tempMsg.readableBytes();
        // 如果暂存有上一次余下的请求报文，则合并
        if (tmpMsgSize > 0) {
            message.writeBytes(this.tempMsg);
            //读取完之后清空
            this.tempMsg.clear();
        }
        //将接收到的byteBuf写入到自己定义的ByteBuf(message)中
        message.writeBytes(byteBuf);
        //数据可读长度大于0才进行读取
        if (message.readableBytes() > 0) {
            InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
            //服务端端口
            int serverPort = inetSocket.getPort();
            //端口为otcPort，则为江南版OTC通讯协议
            if (serverPort == DataInitialization.getOtcPort()) {
                this.otcRecursionReadBytes(ctx, message, out);
            }
            //端口为sxPort，则为松下通讯协议
            if (serverPort == DataInitialization.getSxPort()) {
                this.sxRecursionReadBytes(ctx, message, out);
            }
        } else {
            message.clear();
            message.release();
        }
        ctx.flush();
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
        //可读长度小于等于2，先暂存，大于2则进行读取
        if (bufNum <= 2) {
            this.tempMsg.writeBytes(message);
            message.clear();
            message.release();
        } else {
            byte[] headBytes = new byte[1];
            headBytes[0] = message.getByte(0);
            //头部1个字节
            String otcHead = CommonUtils.bytesToHexString(headBytes);
            if (this.otcMap.containsKey(otcHead)) {
                //查看包长度
                byte[] lengthBytes = new byte[1];
                //查看第2字节的数据包长度
                lengthBytes[0] = message.getByte(1);
                //数据包长度(数组转16进制再转10进制)
                int otcLength = Integer.valueOf(Hex.encodeHexString(lengthBytes), 16) + 2;
                //判断可读长度是否多于数据包长度（是否是一个完整数据包）
                if (otcLength > 0 && bufNum >= otcLength) {
                    //将一个完整数据包读取到bytes数组中
                    byte[] bytes = new byte[otcLength];
                    message.readBytes(bytes);
                    //解析完整数据包成16进制
                    String str = CommonUtils.bytesToHexString(bytes);
                    HandlerParam handlerParam = this.jnOtcDecoderAnalysis.baseProtocolAnalysis(ctx, str);
                    if (null != handlerParam) {
                        out.add(handlerParam);
                    }
                    //如果还有可读字节，则继续递归读取 16578538676
                    if (message.readableBytes() > 0) {
                        this.otcRecursionReadBytes(ctx, message, out);
                    } else {
                        message.clear();
                        message.release();
                    }
                } else {
                    //剩下来的数据放到tempMsg暂存
                    this.tempMsg.writeBytes(message);
                    message.clear();
                    message.release();
                }
            } else {
                message.clear();
                message.release();
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
            this.tempMsg.writeBytes(message);
            message.clear();
            message.release();
        } else {
            //查看前4个字节
            byte[] headBytes = new byte[4];
            for (int index = 0; index < 4; index++) {
                headBytes[index] = message.getByte(index);
            }
            //头部4个字节
            String sxFourHead = CommonUtils.bytesToHexString(headBytes);
            //判断是否为两次握手验证
            if (this.sxMap.containsKey(sxFourHead)) {
                //获得数据包长度
                int length = this.sxMap.get(sxFourHead);
                //可读字节是否多于数据包长度（是否为完整包）
                if (length > 0 && bufNum >= length) {
                    byte[] bytes = new byte[length];
                    message.readBytes(bytes);
                    String str = CommonUtils.bytesToHexString(bytes);
                    final HandlerParam handlerParam = this.jnSxDecoderAnalysis.baseProtocolAnalysis(ctx, str);
                    if (null != handlerParam) {
                        out.add(handlerParam);
                    }
                    //如果还有可读字节，则继续递归读取
                    if (message.readableBytes() > 0) {
                        this.sxRecursionReadBytes(ctx, message, out);
                    } else {
                        message.clear();
                        message.release();
                    }
                } else {
                    //剩下来的数据放到tempMsg暂存
                    this.tempMsg.writeBytes(message);
                    message.clear();
                    message.release();
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
                String sxThreeHead = CommonUtils.bytesToHexString(headByte);
                if (this.sxMap.containsKey(sxThreeHead)) {
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
                        final HandlerParam handlerParam = this.jnSxDecoderAnalysis.baseProtocolAnalysis(ctx, str);
                        if (null != handlerParam) {
                            out.add(handlerParam);
                        }
                        //如果还有可读字节，则继续递归读取
                        if (message.readableBytes() > 0) {
                            this.sxRecursionReadBytes(ctx, message, out);
                        } else {
                            message.clear();
                            message.release();
                        }
                    } else {
                        //剩下来的数据放到tempMsg暂存
                        this.tempMsg.writeBytes(message);
                        message.clear();
                        message.release();
                    }
                } else {
                    message.clear();
                    message.release();
                }
            }
        }
    }
}
