package com.shth.das.netty;

import com.shth.das.common.CommonDbData;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Netty服务端
 */
@Slf4j
@Configuration
public class NettyServer {

    public void start(int port) {
        CommonDbData.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            // Netty 客户端连接监听事件处理线程池
            final EventLoopGroup bossGroup = new NioEventLoopGroup();
            // Netty i/o 处理事件的线程池
            final EventLoopGroup workerGroup = new NioEventLoopGroup();
            ChannelFuture channelFuture = null;

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
                    channelFuture = server.bind(port).sync();
                    //channelFuture = server.bind(port).sync();
                    // 查看一下操作是不是成功结束了
                    if (channelFuture.isSuccess()) {
                        //如果没有成功结束就处理一些事情,结束了就执行关闭服务端等操作
                        log.info("Netty服务端启动成功,监听端口是：" + port);
                    }
                    channelFuture.channel().closeFuture().sync();
                } catch (Exception e) {
                    log.error("服务端启动异常：{}", e.getMessage());
                } finally {
                    // 关闭事件处理组
                    channelFuture.channel().close().addListener(ChannelFutureListener.CLOSE);
                    channelFuture.awaitUninterruptibly(); //阻塞
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                    log.info("Netty服务端已关闭!");
                }
            }
        });
    }

    /**
     * 1.从消息开头偏移lengthFieldOffset长度, 到达A位置
     * 2.再从A位置读取lengthFieldLength长度, 到达B位置, 内容是d
     * 3.再从B位置读取(d+lengthAdjustment)长度, 达到D位置
     * 4.从消息开头跳过initialBytesToStrip长度到达C位置
     * 5.将C位置-D位置之间的内容传送给接下来的处理器进行后续处理
     */
    protected ChannelHandler getChannelInitializer() {
        final NettyServerHandler nettyServerHandler = new NettyServerHandler();
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                //获取通道注册的服务端端口
                //int serverPort = socketChannel.localAddress().getPort();
                //心跳检测
                //自定义协议解码器，根据端口动态的选择解码器
                pipeline.addLast("decoder", new NettyDecoder());
                //自定义协议编码器
                pipeline.addLast("encoder", new NettyEncoder());
                //业务处理(实现Handler共享)
                pipeline.addLast(nettyServerHandler);
            }
        };
    }
}
