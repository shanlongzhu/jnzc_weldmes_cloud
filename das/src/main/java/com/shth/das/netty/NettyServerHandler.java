package com.shth.das.netty;

import com.shth.das.business.JnOtcRtDataProtocol;
import com.shth.das.business.JnSxRtDataProtocol;
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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Netty服务端处理器
 * ChannelInboundHandlerAdapter：channelRead 不会自动释放，需要手动释放
 * SimpleChannelInboundHandler:继承了ChannelInboundHandlerAdapter，
 * channelRead0 获取消息会自动释放资源，获取的消息必须是指定的泛型。
 */
@Slf4j
@Component
public class NettyServerHandler extends SimpleChannelInboundHandler<HandlerParam> {

    /**
     * 保存字符串长度和方法映射关系
     * Consumer<T>,T:入参，不返回
     */
    private final Map<Integer, Consumer<HandlerParam>> otcHandlerMapping = new HashMap<>();
    private final Map<Integer, Consumer<HandlerParam>> sxHandlerMapping = new HashMap<>();

    private JnOtcRtDataProtocol jnOtcRtDataProtocol;
    private JnSxRtDataProtocol jnSxRtDataProtocol;

    public NettyServerHandler() {
        init();
    }

    private void init() {
        setOtcHandlerMapping();
        setSxHandlerMapping();
        this.jnOtcRtDataProtocol = new JnOtcRtDataProtocol();
        this.jnSxRtDataProtocol = new JnSxRtDataProtocol();
    }

    private void setOtcHandlerMapping() {
        //OTC（1.0）实时数据解析
        this.otcHandlerMapping.put(282, JnOtcRtDataProtocol::jnRtDataManage);
        //OTC（1.0）工艺下发返回解析
        this.otcHandlerMapping.put(24, JnOtcRtDataProtocol::otcIssueReturnManage);
        //OTC（1.0）索取返回协议解析
        this.otcHandlerMapping.put(112, JnOtcRtDataProtocol::otcClaimReturnManage);
        //OTC（1.0）密码返回和控制命令返回[新增程序包路径下发返回]
        this.otcHandlerMapping.put(22, JnOtcRtDataProtocol::otcPwdCmdReturnManage);
    }

    private void setSxHandlerMapping() {
        //松下焊机【GL5、FR2、AT3】第二次握手验证
        this.sxHandlerMapping.put(128, JnSxRtDataProtocol::jnSxSecondVerify);
        //松下焊机GL5系列软硬件参数【刷卡解锁焊机】
        this.sxHandlerMapping.put(180, JnSxRtDataProtocol::jnSxGl5SoftHardParam);
        //松下焊机GL5系列CO2实时数据
        this.sxHandlerMapping.put(206, JnSxRtDataProtocol::jnSxGl5RtDataManage);
        //松下焊机GL5系列CO2状态信息
        this.sxHandlerMapping.put(246, JnSxRtDataProtocol::jnSxGl5StatusManage);
        //松下焊机GL5系列【工艺下发返回、工艺索取返回(无数据)、工艺删除返回、通道设定返回、通道读取返回】
        this.sxHandlerMapping.put(106, JnSxRtDataProtocol::jnSxGl5ProcessWeldSet);
        //松下焊机GL5系列CO2工艺索取返回（有数据）
        this.sxHandlerMapping.put(406, JnSxRtDataProtocol::jnSxCo2ProcessClaimReturn);
        //松下焊机GL5系列TIG工艺索取返回（有数据）
        this.sxHandlerMapping.put(446, JnSxRtDataProtocol::jnSxGl5TigProcessClaimReturn);
        //松下焊机【FR2、AT3】系列软硬件参数【刷卡解锁焊机】
        this.sxHandlerMapping.put(154, JnSxRtDataProtocol::jnSxFr2At3SoftHardParam);
        //松下焊机FR2系列CO2实时数据
        this.sxHandlerMapping.put(112, JnSxRtDataProtocol::jnSxFr2Co2RtDataDbManage);
        //松下焊机FR2系列TIG实时数据
        this.sxHandlerMapping.put(118, JnSxRtDataProtocol::jnSxFr2TigRtDataDbManage);
        //松下焊机FR2系列【CO2和TIG】的状态信息
        this.sxHandlerMapping.put(156, JnSxRtDataProtocol::jnSxFr2StatusUiManage);
        //松下焊机【FR2、AT3】系列通道参数【查询回复（无参数）、下载回复、删除回复】
        this.sxHandlerMapping.put(52, JnSxRtDataProtocol::jnSxChannelParamReply);
        //松下焊机FR2系列通道参数【查询回复（有参数）】
        this.sxHandlerMapping.put(220, JnSxRtDataProtocol::jnSxFr2ChannelParamReplyHave);
        //松下焊机AT3系列【查询回复（有参数）】
        this.sxHandlerMapping.put(92, JnSxRtDataProtocol::jnSxAt3ParamQueryReturn);
    }

    /**
     * 服务端收到消息执行的方法
     *
     * @param ctx：通道
     * @param param：数据
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, HandlerParam param) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        //客户端IP地址
        String clientIp = insocket.getAddress().getHostAddress();
        //客户端端口
        int clientPort = insocket.getPort();
        InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
        //服务端端口
        int serverPort = inetSocket.getPort();
        if (param == null) {
            log.info("客户端响应空的消息");
            ctx.flush();
            return;
        }
        //通道赋值
        param.setCtx(ctx);
        //端口为otcPort，则为江南版OTC通讯协议
        if (serverPort == CommonFunction.getOtcPort()) {
            if (this.otcHandlerMapping.containsKey(param.getKey())) {
                this.otcHandlerMapping.get(param.getKey()).accept(param);
            }
        }
        //端口为sxPort，则为松下通讯协议
        if (serverPort == CommonFunction.getSxPort()) {
            if (this.sxHandlerMapping.containsKey(param.getKey())) {
                this.sxHandlerMapping.get(param.getKey()).accept(param);
            }
        }
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
                log.warn("OTC存在连接：" + clientAddress + "--->连接通道数量: " + CommonMap.OTC_CHANNEL_MAP.size());
            } else {
                //保存连接
                CommonMap.OTC_CHANNEL_MAP.put(clientAddress, ctx);
                log.info("OTC新增连接:" + clientAddress + "--->连接通道数量: " + CommonMap.OTC_CHANNEL_MAP.size());
            }
        }
        if (serverPort == CommonFunction.getSxPort()) {
            //如果map中不包含此连接，就保存连接
            if (CommonMap.SX_CHANNEL_MAP.containsKey(clientAddress)) {
                log.warn("SX存在连接：" + clientAddress + "--->连接通道数量: " + CommonMap.SX_CHANNEL_MAP.size());
            } else {
                //保存连接
                CommonMap.SX_CHANNEL_MAP.put(clientAddress, ctx);
                log.info("SX新增连接:" + clientAddress + "--->连接通道数量: " + CommonMap.SX_CHANNEL_MAP.size());
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
            jnOtcRtDataProtocol.jnWeldOffDataManage(ctx, clientIp);
        }
        //端口为sxPort，则为松下通讯协议
        if (serverPort == CommonFunction.getSxPort()) {
            //包含此客户端才去删除
            if (CommonMap.SX_CHANNEL_MAP.containsKey(clientAddress)) {
                //删除连接
                CommonMap.SX_CHANNEL_MAP.remove(clientAddress);
                log.info("SX终止连接:" + clientAddress + "--->连接通道数量: " + CommonMap.SX_CHANNEL_MAP.size());
            }
            jnSxRtDataProtocol.sxWeldOffDataManage(ctx, clientIp);
        }
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