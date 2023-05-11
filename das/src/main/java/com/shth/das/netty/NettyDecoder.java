package com.shth.das.netty;

import com.shth.das.business.dataup.AnalysisContext;
import com.shth.das.business.dataup.UnpackContext;
import com.shth.das.codeparam.HandlerParam;
import com.shth.das.util.CommonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

/**
 * Netty解码器：ByteBuf转Java对象
 *
 * @author zsl
 */
public class NettyDecoder extends ByteToMessageDecoder {

    /**
     * 用来临时保留没有处理过的请求报文
     */
    private final ByteBuf tempMsg = Unpooled.buffer(512);
    private final UnpackContext unpackContext = new UnpackContext();
    private final AnalysisContext analysisContext = new AnalysisContext();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        if (byteBuf.readableBytes() == 0) {
            ctx.flush();
            return;
        }
        ByteBuf message;
        if (this.tempMsg.readableBytes() == 0) {
            message = Unpooled.wrappedBuffer(byteBuf);
        } else {
            //wrappedBuffer()方法：将两个ByteBuf进行包装，实现零拷贝
            message = Unpooled.wrappedBuffer(this.tempMsg, byteBuf);
            //清空
            this.tempMsg.clear();
            this.tempMsg.setZero(0, this.tempMsg.capacity());
        }
        //读写指针清空
        byteBuf.clear();
        //内容清零（清空缓冲区）
        byteBuf.setZero(0, byteBuf.capacity());
        //数据可读长度大于0才进行读取
        if (message == null) {
            ctx.flush();
            return;
        }
        if (message.readableBytes() == 0) {
            //读写指针清空
            message.clear();
            //内容清零（清空缓冲区）
            message.setZero(0, message.capacity());
            ctx.flush();
            return;
        }
        dataDecoder(ctx, message, out);
        if (message.readableBytes() >= 0) {
            //将message拷贝到tempMsg中暂存
            this.tempMsg.writeBytes(message);
        }
    }

    private void dataDecoder(ChannelHandlerContext ctx, ByteBuf message, List<Object> out) {
        //1、数据拆包（二进制转16进制字符串）
        List<String> list = unpackContext.protocolUnpack(ctx, message);
        if (CommonUtils.isEmpty(list)) {
            return;
        }
        //2、遍历解析数据包（16进制字符串解析成pojo对象）
        for (String str : list) {
            HandlerParam handlerParam = analysisContext.protocolAnalysis(ctx, str);
            if (ObjectUtils.isNotEmpty(handlerParam)) {
                out.add(handlerParam);
            }
        }
    }

}
