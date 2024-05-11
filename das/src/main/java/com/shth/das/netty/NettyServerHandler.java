package com.shth.das.netty;

import com.shth.das.business.dataup.base.HandlerContext;
import com.shth.das.codeparam.HandlerParam;
import com.shth.das.common.CommonFunction;
import com.shth.das.common.CommonMap;
import com.shth.das.util.CommonUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Netty服务端处理器
 * ChannelInboundHandlerAdapter：channelRead 不会自动释放，需要手动释放
 * SimpleChannelInboundHandler:继承了ChannelInboundHandlerAdapter，
 * channelRead0 获取消息会自动释放资源，获取的消息必须是指定的泛型。
 */
@Slf4j
@ChannelHandler.Sharable
//只在bootstrap中创建一个实例，它就可以被添加到一或多个pipeline中且不存在竞争，这样可以减少同一类handler的new和GC，节省资源，提高效率(注意这个ChannelHandler必须是无成员变量的)
public class NettyServerHandler extends SimpleChannelInboundHandler<HandlerParam> {


    private NettyServerHandler() {
    }

    private final AtomicInteger otcAtomicInteger = new AtomicInteger(0);
    private final AtomicInteger sxAtomicInteger = new AtomicInteger(0);

    public static final NettyServerHandler INSTANCE = new NettyServerHandler();

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
        //赋值通道环境，注意：decoder的ctx和handler的ctx不是同一个
        param.setCtx(ctx);
        this.handlerContext.dataHandler(ctx, param);
        ctx.flush();
    }

    /**
     * 有客户端新增连接服务器会触发此函数
     *
     * @param ctx 通道
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
        //服务端端口
        int serverPort = inetSocket.getPort();
        String clientAddress = CommonUtils.getClientAddress(ctx);
        if (serverPort == CommonFunction.getOtcPort()) {
            otcAtomicInteger.incrementAndGet();
            //如果map中不包含此连接，就保存连接
            if (CommonMap.OTC_CHANNEL_MAP.containsKey(clientAddress)) {
                log.warn("OTC存在连接：{}--->连接通道数量：{},---->otcAtomicInteger:{}", clientAddress, CommonMap.OTC_CHANNEL_MAP.size(), otcAtomicInteger.get());
            } else {
                //保存连接
                CommonMap.OTC_CHANNEL_MAP.put(clientAddress, ctx);
                log.info("OTC新增连接：{}--->连接通道数量：{},---->otcAtomicInteger:{}", clientAddress, CommonMap.OTC_CHANNEL_MAP.size(), otcAtomicInteger.get());
            }
        }
        if (serverPort == CommonFunction.getSxPort()) {
            sxAtomicInteger.incrementAndGet();
            //如果map中不包含此连接，就保存连接
            if (CommonMap.SX_CHANNEL_MAP.containsKey(clientAddress)) {
                log.warn("SX存在连接：{}--->连接通道数量：{},---->sxAtomicInteger:{}", clientAddress, CommonMap.SX_CHANNEL_MAP.size(), sxAtomicInteger.get());
            } else {
                //保存连接
                CommonMap.SX_CHANNEL_MAP.put(clientAddress, ctx);
                log.info("SX新增连接：{}--->连接通道数量：{},---->sxAtomicInteger:{}", clientAddress, CommonMap.SX_CHANNEL_MAP.size(), sxAtomicInteger.get());
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
        InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
        //服务端端口
        int serverPort = inetSocket.getPort();
        String clientAddress = CommonUtils.getClientAddress(ctx);
        //端口为otcPort，则为江南版OTC通讯协议
        if (serverPort == CommonFunction.getOtcPort()) {
            otcAtomicInteger.decrementAndGet();
            //包含此客户端才去删除
            if (CommonMap.OTC_CHANNEL_MAP.containsKey(clientAddress)) {
                //删除连接
                CommonMap.OTC_CHANNEL_MAP.remove(clientAddress);
                log.info("OTC终止连接：{}--->连接通道数量：{},---->otcAtomicInteger:{}", clientAddress, CommonMap.OTC_CHANNEL_MAP.size(), otcAtomicInteger.get());
            }
        }
        //端口为sxPort，则为松下通讯协议
        if (serverPort == CommonFunction.getSxPort()) {
            sxAtomicInteger.decrementAndGet();
            //包含此客户端才去删除
            if (CommonMap.SX_CHANNEL_MAP.containsKey(clientAddress)) {
                //删除连接
                CommonMap.SX_CHANNEL_MAP.remove(clientAddress);
                log.info("SX终止连接：{}--->连接通道数量：{},---->sxAtomicInteger:{}", clientAddress, CommonMap.SX_CHANNEL_MAP.size(), sxAtomicInteger.get());
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