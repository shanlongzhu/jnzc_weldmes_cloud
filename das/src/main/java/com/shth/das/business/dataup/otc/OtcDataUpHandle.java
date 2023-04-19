package com.shth.das.business.dataup.otc;

import com.shth.das.business.dataup.DataUpHandle;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

public interface OtcDataUpHandle extends DataUpHandle {

    /**
     * 自定义OTC数据拆包逻辑
     */
    @Override
    default String unpack(Channel channel, ByteBuf byteBuf, List<Object> out) {
        return null;
    }

}
