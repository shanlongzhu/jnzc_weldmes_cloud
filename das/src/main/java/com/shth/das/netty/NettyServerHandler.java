package com.shth.das.netty;

import com.shth.das.business.JnRtDataProtocol;
import com.shth.das.business.SxRtDataProtocol;
import com.shth.das.common.DataInitialization;
import com.shth.das.pojo.db.SxWeldModel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Netty服务端处理器
 * ChannelInboundHandlerAdapter：channelRead 不会自动释放，需要手动释放
 * SimpleChannelInboundHandler:继承了ChannelInboundHandlerAdapter，
 * channelRead0 获取消息会自动释放资源，获取的消息必须是指定的泛型。
 */
@Slf4j
//@Sharable  //表明当前Handler是共享的，只有一个Handler实例
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {

    private final JnRtDataProtocol jnRtDataProtocol = new JnRtDataProtocol();
    private final SxRtDataProtocol sxRtDataProtocol = new SxRtDataProtocol();

    /**
     * key:客户端IP地址
     * value:连接通道
     * 保存连接进服务端的通道数量
     */
    public static final ConcurrentHashMap<String, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * key:客户端IP地址
     * value:采集编号
     * 采集盒的IP地址和采集编号对应关系(用来向前端发送关机数据)
     */
    public static final ConcurrentHashMap<String, String> CLIENT_IP_GATHER_NO_MAP = new ConcurrentHashMap<>();

    /**
     * key：客户端IP地址
     * value：松下设备信息
     * 松下焊机设备数据暂存
     */
    public static final ConcurrentHashMap<String, SxWeldModel> SX_CLIENT_IP_BIND_WELD_INFO = new ConcurrentHashMap<>();

    /**
     * 服务端收到消息执行的方法
     *
     * @param ctx：通道
     * @param msg：数据
     */
    @SuppressWarnings({"ALL"})
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        //客户端IP地址
        String clientIp = insocket.getAddress().getHostAddress();
        //客户端端口
        int clientPort = insocket.getPort();
        InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
        //服务端端口
        int serverPort = inetSocket.getPort();
        if (msg == null) {
            log.info("客户端响应空的消息");
            ctx.flush();
            return;
        }
        if (msg instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) msg;
            //端口为otcPort，则为江南版OTC通讯协议
            if (serverPort == DataInitialization.getOtcPort()) {
                this.jnRtDataProtocol.jnRtDataManage(map);
            }
            //端口为sxPort，则为松下通讯协议
            if (serverPort == DataInitialization.getSxPort()) {
                this.sxRtDataProtocol.sxRtDataManage(map);
            }
            map.clear();
            ReferenceCountUtil.release(map);
        }
        ReferenceCountUtil.release(msg);
        ctx.flush();
    }

    /**
     * 有客户端连接服务器会触发此函数
     *
     * @param ctx 通道
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();
        InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
        String serverIp = inetSocket.getAddress().getHostAddress();
        int serverPort = inetSocket.getPort();
        //如果map中不包含此连接，就保存连接
        if (CHANNEL_MAP.containsKey(clientIp)) {
            log.info("存在连接：" + clientIp + ":" + clientPort + "--->连接通道数量: " + CHANNEL_MAP.size());
        } else {
            //保存连接
            CHANNEL_MAP.put(clientIp, ctx);
            log.info("新增连接:" + clientIp + "：" + clientPort + "--->连接通道数量: " + CHANNEL_MAP.size());
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
        //包含此客户端才去删除
        if (CHANNEL_MAP.containsKey(clientIp)) {
            //删除连接
            CHANNEL_MAP.remove(clientIp);
            log.info("终止连接:" + clientIp + "：" + clientPort + "--->连接通道数量: " + CHANNEL_MAP.size());
        }
        //端口为otcPort，则为江南版OTC通讯协议
        if (serverPort == DataInitialization.getOtcPort()) {
            this.jnRtDataProtocol.jnWeldOffDataManage(clientIp);
        }
        //端口为sxPort，则为松下通讯协议
        if (serverPort == DataInitialization.getSxPort()) {
            this.sxRtDataProtocol.sxWeldOffDataManage(clientIp);
        }
        ctx.flush();
        ctx.disconnect();
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
        log.info("异常捕捉：" + cause.getMessage());
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
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            //判断读超时，则关闭通道
            if (IdleState.READER_IDLE.equals((event.state()))) {
                ctx.disconnect();
                ctx.channel().close();
                ctx.close();
            }
        }
        ctx.flush();
        super.userEventTriggered(ctx, evt);
    }
}