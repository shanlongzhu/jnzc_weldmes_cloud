package com.shth.das.business.dataup.otc;

import com.google.common.collect.Lists;
import com.shth.das.business.dataup.base.BaseUnpack;
import com.shth.das.util.CommonUtils;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JnOtcDecoderUnpack extends BaseUnpack {

    public JnOtcDecoderUnpack() {
        init();
    }

    private final Map<String, Integer> otcMap = new HashMap<>();

    //存储头部
    private final byte[] headBytes = new byte[1];
    //查看包长度
    private final byte[] lengthBytes = new byte[1];

    private void init() {
        //OTC通讯协议固定头部
        this.otcMap.put("7E", 1);
    }

    @Override
    public List<String> dataUnpack(ByteBuf message) {
        List<String> list = Lists.newArrayList();
        if (message.readableBytes() <= 2) {
            return list;
        }
        while (message.readableBytes() > 2) {
            try {
                this.headBytes[0] = message.getByte(0);
                //头部1个字节
                String otcHead = CommonUtils.bytesToHexString(this.headBytes);
                if (!this.otcMap.containsKey(otcHead)) {
                    clearByteBuf(message);
                    break;
                }
                //查看第2个字节的数据包长度
                this.lengthBytes[0] = message.getByte(1);
                //数据包长度(数组转16进制再转10进制)
                int otcLength = Integer.valueOf(Hex.encodeHexString(this.lengthBytes), 16) + 2;
                //判断可读长度是否多于数据包长度（是否是一个完整数据包）
                if (message.readableBytes() < otcLength) {
                    break;
                }
                //将一个完整数据包读取到bytes数组中
                byte[] bytes = new byte[otcLength];
                message.readBytes(bytes);
                //解析完整数据包成16进制
                String str = CommonUtils.bytesToHexString(bytes);
                list.add(str);
            } catch (Exception e) {
                log.error("OTC数据拆包异常：{}", e.getMessage());
                clearByteBuf(message);
            }
        }
        return list;
    }

    private void clearByteBuf(ByteBuf... byteBuf) {
        for (ByteBuf buf : byteBuf) {
            if (buf != null && buf.readableBytes() != 0) {
                //清空
                buf.clear();
                buf.setZero(0, buf.capacity());
            }
        }
    }

}
