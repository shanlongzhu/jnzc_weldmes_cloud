package com.shth.das.business.dataup.sx;

import com.shth.das.business.dataup.DataUpHandle;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

public interface SxDataUpHandle extends DataUpHandle {

    /**
     * 自定义松下数据拆包逻辑
     */
    @Override
    default String unpack(Channel channel, ByteBuf byteBuf, List<Object> out) {
        return null;
    }

}
