package com.shth.das.netty;

import com.shth.das.business.JNRtDataProtocol;
import com.shth.das.pojo.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Netty解码器：ByteBuf转Java对象
 */
public class NettyDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        //创建字节数组,buffer.readableBytes可读字节长度
        byte[] b = new byte[buffer.readableBytes()];
        //复制内容到字节数组b
        buffer.readBytes(b);
        //字节数组转16进制字符串
        String str = bytesToHexString(b);
        Map<String, Object> map = new HashMap<>();
        /*
        江南版实时数据协议解析
         */
        if (str.length() == 282) {
            //存数据库
            List<JNRtDataDB> jnRtDataDBS = JNRtDataProtocol.jnRtDataAnalysis(str);
            //发送前端
            List<JNRtDataUI> jnRtDataUIS = JNRtDataProtocol.jnRtDataUiAnalysis(str);
            map.put("JNRtDataDB", jnRtDataDBS);
            map.put("JNRtDataUI", jnRtDataUIS);
        }
        /*
        下发规范返回
         */
        if (str.length() == 24) {
            JNProcessIssueReturn issueReturn = JNRtDataProtocol.jnIssueReturnAnalysis(str);
            map.put("JNProcessIssueReturn", issueReturn);
        }
        /*
        索取规范返回
         */
        if (str.length() == 112) {
            JNProcessClaimReturn claimReturn = JNRtDataProtocol.jnClaimReturnAnalysis(str);
            map.put("JNProcessClaimReturn", claimReturn);
        }
        /*
        密码返回
        控制命令返回
         */
        if (str.length() == 22) {
            //密码返回
            if ("7E".equals(str.substring(0, 2)) && "53".equals(str.substring(10, 12)) && "7D".equals(str.substring(20, 22))) {
                JNPasswordReturn passwordReturn = JNRtDataProtocol.jNPasswordReturnAnalysis(str);
                map.put("JNPasswordReturn", passwordReturn);
            }
            //控制命令返回
            if ("7E".equals(str.substring(0, 2)) && "54".equals(str.substring(10, 12)) && "7D".equals(str.substring(20, 22))) {
                JNCommandReturn commandReturn = JNRtDataProtocol.jNCommandReturnAnalysis(str);
                map.put("JNCommandReturn", commandReturn);
            }
        }
        out.add(map);
    }

    /**
     * 数组转16进制字符串
     *
     * @param bArray 数组
     * @return String
     */
    public static String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte b : bArray) {
            sTemp = Integer.toHexString(0xFF & b);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
}
