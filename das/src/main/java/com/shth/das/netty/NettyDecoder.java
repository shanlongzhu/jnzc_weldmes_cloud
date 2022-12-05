package com.shth.das.netty;

import com.shth.das.business.JnOtcDecoderAnalysis;
import com.shth.das.business.JnSxDecoderAnalysis;
import com.shth.das.codeparam.HandlerParam;
import com.shth.das.common.CommonFunction;
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

    /**
     * 用来临时保留没有处理过的请求报文
     */
    private final ByteBuf tempMsg = Unpooled.buffer(1024);

    private JnOtcDecoderAnalysis jnOtcDecoderAnalysis;
    private JnSxDecoderAnalysis jnSxDecoderAnalysis;

    /**
     * key:包头固定值
     * value：字节长度
     */
    private final Map<String, Integer> otcMap = new HashMap<>();
    private final Map<String, Integer> sxMap = new HashMap<>();

    public NettyDecoder() {
        init();
    }

    private void init() {
        //OTC通讯协议固定头部
        this.otcMap.put("7E", 1);
        //松下第一次握手验证
        this.sxMap.put("4E455430", 21);
        //松下第二次握手验证
        this.sxMap.put("4C4A5348", 64);
        //其他（握手成功后正常传输的通讯协议）
        this.sxMap.put("FE5AA5", 0);
        //OTC协议解析对象
        this.jnOtcDecoderAnalysis = new JnOtcDecoderAnalysis();
        //松下协议解析对象
        this.jnSxDecoderAnalysis = new JnSxDecoderAnalysis();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        if (byteBuf.readableBytes() == 0) {
            ctx.flush();
            return;
        }
        ByteBuf message;
        if (this.tempMsg == null || this.tempMsg.readableBytes() == 0) {
            message = Unpooled.wrappedBuffer(byteBuf);
        } else {
            //wrappedBuffer()方法：将两个ByteBuf进行包装，实现零拷贝
            message = Unpooled.wrappedBuffer(this.tempMsg, byteBuf);
            //清空
            this.tempMsg.clear();
        }
        //数据可读长度大于0才进行读取
        if (message != null && message.readableBytes() > 0) {
            InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
            //服务端端口
            int serverPort = inetSocket.getPort();
            //端口为otcPort，则为江南版OTC通讯协议
            if (serverPort == CommonFunction.getOtcPort()) {
                this.otcRecursionReadBytes(ctx, message, out);
            }
            //端口为sxPort，则为松下通讯协议
            if (serverPort == CommonFunction.getSxPort()) {
                this.sxRecursionReadBytes(ctx, message, out);
            }
            //读写指针清空
            message.clear();
            //内容清零（清空缓冲区）
            message.setZero(0, message.capacity());
        }
        //读写指针清空
        byteBuf.clear();
        //内容清零（清空缓冲区）
        byteBuf.setZero(0, byteBuf.capacity());
        ctx.flush();
    }

    /**
     * 读取ByteBuf，将字节数组解析成对象，封装到HandlerParam中传输
     *
     * @param ctx     通道
     * @param message ByteBuf
     * @param out     List
     */
    private void otcRecursionReadBytes(ChannelHandlerContext ctx, ByteBuf message, List<Object> out) {
        if (message.readableBytes() == 0) {
            return;
        }
        while (message.readableBytes() > 2) {
            byte[] headBytes = new byte[1];
            headBytes[0] = message.getByte(0);
            //头部1个字节
            String otcHead = CommonUtils.bytesToHexString(headBytes);
            if (!this.otcMap.containsKey(otcHead)) {
                return;
            }
            //查看包长度
            byte[] lengthBytes = new byte[1];
            //查看第2字节的数据包长度
            lengthBytes[0] = message.getByte(1);
            //数据包长度(数组转16进制再转10进制)
            int otcLength = Integer.valueOf(Hex.encodeHexString(lengthBytes), 16) + 2;
            //判断可读长度是否多于数据包长度（是否是一个完整数据包）
            if (message.readableBytes() < otcLength) {
                break;
            }
            //将一个完整数据包读取到bytes数组中
            byte[] bytes = new byte[otcLength];
            message.readBytes(bytes);
            //解析完整数据包成16进制
            String str = CommonUtils.bytesToHexString(bytes);
            HandlerParam handlerParam = jnOtcDecoderAnalysis.baseProtocolAnalysis(ctx, str);
            if (null != handlerParam) {
                out.add(handlerParam);
            }
        }
        //将message拷贝到tempMsg中暂存
        this.tempMsg.writeBytes(message);
    }

    /**
     * 读取ByteBuf，将字节数组解析成对象，封装到HandlerParam中传输
     *
     * @param ctx     通道
     * @param message ByteBuf
     * @param out     List
     */
    private void sxRecursionReadBytes(ChannelHandlerContext ctx, ByteBuf message, List<Object> out) {
        if (message.readableBytes() == 0) {
            return;
        }
        while (message.readableBytes() > 5) {
            //查看头部4个字节
            String sxFourHead = this.getHexStringByByteBuf(message, 4);
            //查看头部3个字节（FE5AA5）
            String sxThreeHead = this.getHexStringByByteBuf(message, 3);
            //判断是否为两次握手验证
            if (this.sxMap.containsKey(sxFourHead)) {
                //获得数据包长度
                int sxFourHeadLength = this.sxMap.get(sxFourHead);
                //可读字节是否多于数据包长度（是否为完整包）
                if (message.readableBytes() < sxFourHeadLength) {
                    break;
                }
                byte[] bytes = new byte[sxFourHeadLength];
                message.readBytes(bytes);
                String str = CommonUtils.bytesToHexString(bytes);
                HandlerParam handlerParam = jnSxDecoderAnalysis.baseProtocolAnalysis(ctx, str);
                if (null != handlerParam) {
                    out.add(handlerParam);
                }
            }
            //非两次握手，正常通讯协议
            else if (this.sxMap.containsKey(sxThreeHead)) {
                //查看包长度
                byte[] lengthBytes = new byte[2];
                //查看第4、5字节的数据包长度
                lengthBytes[0] = message.getByte(3);
                lengthBytes[1] = message.getByte(4);
                //查看数据包应该有的长度(字节长度)
                int sxThreeHeadLength = Integer.valueOf(Hex.encodeHexString(lengthBytes), 16);
                //判断可读长度是否多于数据包长度（是否是一个完整数据包）
                if (message.readableBytes() < sxThreeHeadLength) {
                    break;
                }
                //将一个完整数据包读取到bytes数组中
                byte[] bytes = new byte[sxThreeHeadLength];
                message.readBytes(bytes);
                //解析完整数据包成16进制
                String str = CommonUtils.bytesToHexString(bytes);
                HandlerParam handlerParam = jnSxDecoderAnalysis.baseProtocolAnalysis(ctx, str);
                if (null != handlerParam) {
                    out.add(handlerParam);
                }
            }
            //无法识别的通讯协议
            else {
                return;
            }
        }
        //将message拷贝到tempMsg中暂存
        this.tempMsg.writeBytes(message);
    }

    /**
     * 查询指定的字节字符串内容
     *
     * @param message
     * @param num
     * @return
     */
    private String getHexStringByByteBuf(ByteBuf message, int num) {
        byte[] headBytes = new byte[num];
        for (int index = 0; index < num; index++) {
            headBytes[index] = message.getByte(index);
        }
        return CommonUtils.bytesToHexString(headBytes);
    }

}
