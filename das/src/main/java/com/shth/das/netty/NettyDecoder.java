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
        //将数据读取tempMsg
        this.tempMsg.writeBytes(byteBuf);
        dataDecoder(ctx, this.tempMsg, out);
    }

    private void dataDecoder(ChannelHandlerContext ctx, ByteBuf message, List<Object> out) {
        //1、数据拆包（二进制转16进制字符串）
        List<String> list = this.unpackContext.protocolUnpack(ctx, message);
        if (CommonUtils.isEmpty(list)) {
            return;
        }
        //2、遍历解析数据包（16进制字符串解析成pojo对象）
        for (String str : list) {
            HandlerParam handlerParam = this.analysisContext.protocolAnalysis(ctx, str);
            if (ObjectUtils.isNotEmpty(handlerParam)) {
                out.add(handlerParam);
            }
        }
    }

}
