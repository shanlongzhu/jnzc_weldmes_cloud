package com.shth.das.netty;

import com.shth.das.common.CommonThreadPool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Netty服务端
 */
@Slf4j
@Configuration
public class NettyServer {

    @Value("${otcNettyServer.port}")
    private Integer otcPort;
    @Value("${sxNettyServer.port}")
    private Integer sxPort;

    @PostConstruct
    public void start() {
        CommonThreadPool.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            // Netty 客户端连接监听事件处理线程池
            final EventLoopGroup bossGroup = new NioEventLoopGroup();
            // Netty i/o 处理事件的线程池
            final EventLoopGroup workerGroup = new NioEventLoopGroup();
            //业务线程池
            final EventLoopGroup handlerGroup = new NioEventLoopGroup();

            @Override
            public void run() {
                try {
                    //睡眠1秒，等待系统任务执行完成
                    Thread.sleep(1000);
                    // 服务端启动引导器
                    final ServerBootstrap server = new ServerBootstrap();
                    server.group(bossGroup, workerGroup)
                            //TCP数据接收缓冲区大小
                            .option(ChannelOption.SO_RCVBUF, 1024)
                            //设置采用Nio的通道方式来建立请求连接
                            .channel(NioServerSocketChannel.class)
                            //阻塞队列数量
                            .option(ChannelOption.SO_BACKLOG, 128)
                            //心跳保持
                            .childOption(ChannelOption.SO_KEEPALIVE, true)
                            //通道初始化
                            .childHandler(new NettyChannelInitializer(handlerGroup));
                    //System.setProperty("io.netty.leakDetection.maxRecords", "1000");
                    //System.setProperty("io.netty.leakDetection.acquireAndReleaseOnly", "true");
                    //ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
                    // 服务端绑定端口并且开始接收进来的连接请求
                    ChannelFuture otccChannelFuture = server.bind(otcPort).sync();
                    ChannelFuture sxChannelFuture = server.bind(sxPort).sync();
                    // 查看一下操作是不是成功结束了
                    if (otccChannelFuture.isSuccess()) {
                        //如果没有成功结束就处理一些事情,结束了就执行关闭服务端等操作
                        log.info("Netty服务端启动成功,监听端口是：" + otcPort);
                    }
                    // 查看一下操作是不是成功结束了
                    if (sxChannelFuture.isSuccess()) {
                        //如果没有成功结束就处理一些事情,结束了就执行关闭服务端等操作
                        log.info("Netty服务端启动成功,监听端口是：" + sxPort);
                    }
                    otccChannelFuture.channel().closeFuture().sync();
                    sxChannelFuture.channel().closeFuture().sync();
                } catch (Exception e) {
                    log.error("服务端启动异常：{}", e.getMessage());
                } finally {
                    // 关闭事件处理组
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                    log.error("Netty服务端已关闭!");
                }
            }
        });
    }
}
