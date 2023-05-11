package com.shth.das.netty;

import com.shth.das.business.dataup.HandlerContext;
import com.shth.das.codeparam.HandlerParam;
import com.shth.das.common.CommonFunction;
import com.shth.das.common.CommonMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * Netty服务端处理器
 * ChannelInboundHandlerAdapter：channelRead 不会自动释放，需要手动释放
 * SimpleChannelInboundHandler:继承了ChannelInboundHandlerAdapter，
 * channelRead0 获取消息会自动释放资源，获取的消息必须是指定的泛型。
 */
@Slf4j
@Component
public class NettyServerHandler extends SimpleChannelInboundHandler<HandlerParam> {

    private final HandlerContext handlerContext = new HandlerContext();

    /**
     * 服务端收到消息执行的方法
     *
     * @param ctx：通道
     * @param param：数据
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, HandlerParam param) {
        if (param == null) {
            log.info("客户端响应空的消息");
            ctx.flush();
            return;
        }
        handlerContext.dataHandler(ctx, param);
        ctx.flush();
    }

    /**
     * 有客户端新增连接服务器会触发此函数
     *
     * @param ctx 通道
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
        //客户端IP
        String clientIp = insocket.getAddress().getHostAddress();
        //客户端端口
        int clientPort = insocket.getPort();
        //服务端端口
        int serverPort = inetSocket.getPort();
        String clientAddress = clientIp + ":" + clientPort;
        if (serverPort == CommonFunction.getOtcPort()) {
            //如果map中不包含此连接，就保存连接
            if (CommonMap.OTC_CHANNEL_MAP.containsKey(clientAddress)) {
                log.warn("OTC存在连接：{}--->连接通道数量：{}", clientAddress, CommonMap.OTC_CHANNEL_MAP.size());
            } else {
                //保存连接
                CommonMap.OTC_CHANNEL_MAP.put(clientAddress, ctx);
                log.info("OTC新增连接：{}--->连接通道数量：{}", clientAddress, CommonMap.OTC_CHANNEL_MAP.size());
            }
        }
        if (serverPort == CommonFunction.getSxPort()) {
            //如果map中不包含此连接，就保存连接
            if (CommonMap.SX_CHANNEL_MAP.containsKey(clientAddress)) {
                log.warn("SX存在连接：{}--->连接通道数量：{}", clientAddress, CommonMap.SX_CHANNEL_MAP.size());
            } else {
                //保存连接
                CommonMap.SX_CHANNEL_MAP.put(clientAddress, ctx);
                log.info("SX新增连接：{}--->连接通道数量：{}", clientAddress, CommonMap.SX_CHANNEL_MAP.size());
            }
        }
        ctx.flush();
    }

    /**
     * 有客户端终止连接服务器会触发此函数
     *
     * @param ctx 通道
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
        //客户端IP地址
        String clientIp = insocket.getAddress().getHostAddress();
        //客户端端口
        int clientPort = insocket.getPort();
        //服务端端口
        int serverPort = inetSocket.getPort();
        String clientAddress = clientIp + ":" + clientPort;
        //端口为otcPort，则为江南版OTC通讯协议
        if (serverPort == CommonFunction.getOtcPort()) {
            //包含此客户端才去删除
            if (CommonMap.OTC_CHANNEL_MAP.containsKey(clientAddress)) {
                //删除连接
                CommonMap.OTC_CHANNEL_MAP.remove(clientAddress);
                log.info("OTC终止连接:" + clientAddress + "--->连接通道数量: " + CommonMap.OTC_CHANNEL_MAP.size());
            }
        }
        //端口为sxPort，则为松下通讯协议
        if (serverPort == CommonFunction.getSxPort()) {
            //包含此客户端才去删除
            if (CommonMap.SX_CHANNEL_MAP.containsKey(clientAddress)) {
                //删除连接
                CommonMap.SX_CHANNEL_MAP.remove(clientAddress);
                log.info("SX终止连接:" + clientAddress + "--->连接通道数量: " + CommonMap.SX_CHANNEL_MAP.size());
            }
        }
        handlerContext.shutdownHandle(ctx);
        ctx.flush();
        ctx.channel().close();
        ctx.close();
    }

    /**
     * channelReadComplete channel 通道 Read 读取 Complete 完成
     * 在通道读取完成后会在这个方法里通知，对应可以做刷新操作 ctx.flush()
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 异常捕捉
     *
     * @param ctx   通道
     * @param cause 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("异常捕捉：{}", cause.getMessage());
        ctx.flush();
        if (ctx.channel().isActive()) {
            ctx.channel().close();
            ctx.close();
        }
    }

    /**
     * 在规定时间内未收到客户端的任何数据包, 将主动断开该连接
     *
     * @param ctx 通道
     * @param evt 心跳检测实例
     * @throws Exception 异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        ctx.flush();
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            //判断读超时，则关闭通道
            if (IdleState.READER_IDLE.equals((event.state()))) {
                ctx.channel().close();
                ctx.close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}