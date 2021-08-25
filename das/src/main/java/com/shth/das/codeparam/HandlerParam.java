package com.shth.das.codeparam;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @description: Decoder和Handler传递的参数
 * @author: Shan Long
 * @create: 2021-08-09
 */
@Data
public class HandlerParam implements Serializable {

    /**
     * 通讯协议16进制字符串长度
     */
    private int key;
    /**
     * 封装解析好的pojo对象
     */
    private Map<String,Object> value;
    /**
     * 通道
     */
    private ChannelHandlerContext ctx;

}
