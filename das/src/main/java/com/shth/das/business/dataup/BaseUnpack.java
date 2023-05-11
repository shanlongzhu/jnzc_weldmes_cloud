package com.shth.das.business.dataup;

import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * 数据拆包的超类
 */
public abstract class BaseUnpack {

    /**
     * 数据拆包
     *
     * @param message ByteBuf
     * @return 16进制字符串
     */
    protected abstract List<String> dataUnpack(ByteBuf message);

}
