package com.shth.das.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

/**
 * Netty服务端
 */
@Slf4j
public class NettyServer {

    @Value("${otcNettyServer.port}")
    private Integer otcPort;
    @Value("${sxNettyServer.port}")
    private Integer sxPort;

    /**
     * boss线程池，进行客户端连接
     */
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup();
    /**
     * worker线程池，进行业务处理
     */
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();

    @PostConstruct
    public void start() {
        try {
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
                    .childHandler(new NettyChannelInitializer());
            //System.setProperty("io.netty.leakDetection.maxRecords", "1000");
            //System.setProperty("io.netty.leakDetection.acquireAndReleaseOnly", "true");
            //ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
            // 服务端绑定端口并且开始接收进来的连接请求
            ChannelFuture otcChannelFuture = server.bind(otcPort).sync();
            ChannelFuture sxChannelFuture = server.bind(sxPort).sync();
            // 查看一下操作是不是成功结束了
            if (otcChannelFuture.isSuccess()) {
                //如果没有成功结束就处理一些事情,结束了就执行关闭服务端等操作
                log.info("Netty服务端启动成功,监听端口是：" + otcPort);
            }
            // 查看一下操作是不是成功结束了
            if (sxChannelFuture.isSuccess()) {
                //如果没有成功结束就处理一些事情,结束了就执行关闭服务端等操作
                log.info("Netty服务端启动成功,监听端口是：" + sxPort);
            }
        } catch (Exception e) {
            log.error("服务端启动异常：{}", e.getMessage());
            closeNettyServer();
        }
    }

    public static void closeNettyServer() {
        bossGroup.shutdownGracefully().syncUninterruptibly();
        workerGroup.shutdownGracefully().syncUninterruptibly();
        log.info("Netty服务端已关闭!");
    }

}
