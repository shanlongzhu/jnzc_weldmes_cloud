package com.shth.das.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Netty服务端
 */
@Slf4j
@Configuration
public class NettyServer {

    @Value("${nettyServer.port}")
    private int port;

    public void start() {
        new Thread(new Runnable() {
            // Netty 客户端连接监听事件处理线程池
            final EventLoopGroup bossGroup = new NioEventLoopGroup();
            // Netty i/o 处理事件的线程池
            final EventLoopGroup workerGroup = new NioEventLoopGroup();
            @Override
            public void run() {
                try {
                    // 服务端启动引导器
                    final ServerBootstrap server = new ServerBootstrap();
                    server.group(bossGroup, workerGroup)
                            //设置采用Nio的通道方式来建立请求连接
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG, 128)  //阻塞队列数量
                            .childOption(ChannelOption.SO_KEEPALIVE, true)  //心跳保持
                            .childHandler(getChannelInitializer());
                    // 服务端绑定端口并且开始接收进来的连接请求
                    ChannelFuture channelFuture = server.bind(port).sync();
                    // 查看一下操作是不是成功结束了
                    if (channelFuture.isSuccess()) {
                        //如果没有成功结束就处理一些事情,结束了就执行关闭服务端等操作
                        log.info("服务端启动成功,监听端口是：" + port);
                    }
                    channelFuture.channel().closeFuture().sync();
                } catch (Exception e) {
                    log.error("Netty服务端启动异常：{}", e.getMessage());
                } finally {
                    // 关闭事件处理组
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                    log.info("服务端已关闭!");
                }
            }
        }).start();
    }

    public static ChannelHandler getChannelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                //心跳检测
                pipeline.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                //自定义协议解码器
                pipeline.addLast("decoder", new NettyDecoder());
                //自定义协议编码器
                pipeline.addLast("encoder", new NettyEncoder());
                //业务处理
                pipeline.addLast(new NettyServerHandler());
            }
        };
    }
}
