package com.shth.das.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @description: 通道初始化
 * @author: Shan Long
 * @create: 2021-07-31
 */
public class NettyChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
        //每一个通道都有自己的pipeline，里面有各种处理器
        ChannelPipeline pipeline = nioSocketChannel.pipeline();
        //获取通道注册的服务端端口
        //int serverPort = socketChannel.localAddress().getPort();
        //心跳检测
        //pipeline.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));

        //自定义协议解码器
        pipeline.addLast("decoder", new NettyDecoder());
        //自定义协议编码器
        pipeline.addLast("encoder", new NettyEncoder());
        //业务处理
        pipeline.addLast("handler", NettyServerHandler.INSTANCE);
    }

}
