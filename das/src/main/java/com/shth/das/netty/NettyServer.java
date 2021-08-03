package com.shth.das.netty;

import com.shth.das.common.CommonDbData;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

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
                            //TCP数据接收缓冲区大小
                            .option(ChannelOption.SO_RCVBUF, 1024)
                            //设置采用Nio的通道方式来建立请求连接
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG, 128)  //阻塞队列数量
                            .childOption(ChannelOption.SO_KEEPALIVE, true)  //心跳保持
                            .childHandler(new NettyChannelInitializer());
                    //System.setProperty("io.netty.leakDetection.maxRecords", "1000");
                    //System.setProperty("io.netty.leakDetection.acquireAndReleaseOnly", "true");
                    //ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
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
}
