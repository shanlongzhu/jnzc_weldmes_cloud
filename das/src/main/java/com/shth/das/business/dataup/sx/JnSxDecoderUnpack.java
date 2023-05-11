package com.shth.das.business.dataup.sx;

import com.google.common.collect.Lists;
import com.shth.das.business.dataup.BaseUnpack;
import com.shth.das.util.CommonUtils;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JnSxDecoderUnpack extends BaseUnpack {

    private final Map<String, Integer> sxMap = new HashMap<>();

    private final byte[] lengthBytes = new byte[2];

    public JnSxDecoderUnpack() {
        init();
    }

    private void init() {
        //松下第一次握手验证
        this.sxMap.put("4E455430", 21);
        //松下第二次握手验证
        this.sxMap.put("4C4A5348", 64);
        //其他（握手成功后正常传输的通讯协议）
        this.sxMap.put("FE5AA5", 0);
    }

    public List<String> dataUnpack(ByteBuf message) {
        List<String> list = Lists.newArrayList();
        if (message.readableBytes() <= 5) {
            return list;
        }
        while (message.readableBytes() > 5) {
            try {
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
                    list.add(str);
                }
                //非两次握手，正常通讯协议
                else if (this.sxMap.containsKey(sxThreeHead)) {
                    //查看第4、5个字节的数据包长度
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
                    list.add(str);
                }
                //无法识别的通讯协议
                else {
                    message.clear();
                    message.setZero(0, message.capacity());
                    break;
                }
            } catch (Exception e) {
                log.error("SX数据拆包异常：{}", e.getMessage());
                message.clear();
                message.setZero(0, message.capacity());
            }
        }
        return list;
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
