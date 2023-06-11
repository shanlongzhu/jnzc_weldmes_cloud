package com.shth.das.business.dataup.sx;

import com.alibaba.fastjson2.JSON;
import com.shth.das.business.datadown.sx.SxProtocolJoint;
import com.shth.das.business.dataup.base.BaseHandler;
import com.shth.das.codeparam.HandlerParam;
import com.shth.das.common.*;
import com.shth.das.mqtt.EmqMqttClient;
import com.shth.das.pojo.db.SxMachineQueue;
import com.shth.das.pojo.db.SxWeldModel;
import com.shth.das.pojo.db.TaskClaimIssue;
import com.shth.das.pojo.jnsx.*;
import com.shth.das.processdb.DBCreateMethod;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class JnSxProtocolHandle extends BaseHandler {

    private final Map<Integer, Consumer<HandlerParam>> sxHandlerMapping = new HashMap<>();

    private final SxProtocolJoint sxProtocolJoint = new SxProtocolJoint();

    public JnSxProtocolHandle() {
        init();
    }

    private void init() {
        //松下焊机【GL5、FR2、AT3】第二次握手验证
        this.sxHandlerMapping.put(128, this::jnSxSecondVerify);
        //松下焊机GL5系列软硬件参数【刷卡解锁焊机】
        this.sxHandlerMapping.put(180, this::jnSxGl5SoftHardParam);
        //松下焊机GL5系列CO2实时数据
        this.sxHandlerMapping.put(206, this::jnSxGl5RtDataManage);
        //松下焊机GL5系列CO2状态信息
        this.sxHandlerMapping.put(246, this::jnSxGl5StatusManage);
        //松下焊机GL5系列【工艺下发返回、工艺索取返回(无数据)、工艺删除返回、通道设定返回、通道读取返回】
        this.sxHandlerMapping.put(106, this::jnSxGl5ProcessWeldSet);
        //松下焊机GL5系列CO2工艺索取返回（有数据）
        this.sxHandlerMapping.put(406, this::jnSxCo2ProcessClaimReturn);
        //松下焊机GL5系列TIG工艺索取返回（有数据）
        this.sxHandlerMapping.put(446, this::jnSxGl5TigProcessClaimReturn);
        //松下焊机【FR2、AT3】系列软硬件参数【刷卡解锁焊机】
        this.sxHandlerMapping.put(154, this::jnSxFr2At3SoftHardParam);
        //松下焊机FR2系列CO2实时数据
        this.sxHandlerMapping.put(112, this::jnSxFr2Co2RtDataDbManage);
        //松下焊机FR2系列TIG实时数据
        this.sxHandlerMapping.put(118, this::jnSxFr2TigRtDataDbManage);
        //松下焊机FR2系列【CO2和TIG】的状态信息
        this.sxHandlerMapping.put(156, this::jnSxFr2StatusUiManage);
        //松下焊机【FR2、AT3】系列通道参数【查询回复（无参数）、下载回复、删除回复】
        this.sxHandlerMapping.put(52, this::jnSxChannelParamReply);
        //松下焊机FR2系列通道参数【查询回复（有参数）】
        this.sxHandlerMapping.put(220, this::jnSxFr2ChannelParamReplyHave);
        //松下焊机AT3系列【查询回复（有参数）】
        this.sxHandlerMapping.put(92, this::jnSxAt3ParamQueryReturn);
    }

    @Override
    protected void dataHandler(HandlerParam param) {
        if (this.sxHandlerMapping.containsKey(param.getKey())) {
            this.sxHandlerMapping.get(param.getKey()).accept(param);
        }
    }

    @Override
    protected void shutdownHandler(ChannelHandlerContext ctx) {
        sxShutdownHandler(ctx);
    }

    /**
     * 松下设备信息绑定到通道（设备开机）
     *
     * @param sxWeldModel 焊机信息实体类
     */
    private void sxWeldDataBinding(ChannelHandlerContext ctx, SxWeldModel sxWeldModel) {
        if (ObjectUtils.isEmpty(sxWeldModel)) {
            return;
        }
        String clientAddress = CommonUtils.getClientAddress(ctx);
        //根据通道获取设备CID
        if (CommonMap.SX_CTX_WELD_CID_MAP.containsKey(clientAddress)) {
            String weldCid = CommonMap.SX_CTX_WELD_CID_MAP.get(clientAddress);
            sxWeldModel.setWeldCid(weldCid);
        }
        //将松下设备信息与通道进行绑定
        if (!CommonMap.SX_CTX_WELD_INFO_MAP.containsKey(clientAddress)) {
            CommonMap.SX_CTX_WELD_INFO_MAP.put(clientAddress, sxWeldModel);
        }
        //判断新连接的松下设备是否已经刷卡领任务（true：已经刷过卡则解锁）
        if (CommonFunction.isSlotCardEnableDevice() && CommonUtils.isNotEmpty(sxWeldModel.getWeldCid())) {
            //true:已经刷过卡的设备（解锁该焊机）
            if (CommonMap.SX_TASK_CLAIM_MAP.containsKey(sxWeldModel.getWeldCid())) {
                //0:解锁
                sxChannelSetLock(sxWeldModel.getWeldCid(), 0);
            }
            //没有刷卡，则锁定该焊机
            else {
                //1:锁定
                sxChannelSetLock(sxWeldModel.getWeldCid(), 1);
            }
        }
        //设备CID不为空，则分表存储到新增设备和开机设备的阻塞队列中
        if (StringUtils.isNotBlank(sxWeldModel.getWeldCid())) {
            //添加到松下设备阻塞队列，存储到数据库
            try {
                CommonQueue.SX_ADD_MACHINE_QUEUES.put(sxWeldModel);
            } catch (InterruptedException e) {
                log.error("松下新增设备添加到阻塞队列异常：", e);
            }
            //设备存储到松下开机阻塞队列
            try {
                if (CommonUtils.isNotEmpty(sxWeldModel.getWeldCid())) {
                    SxMachineQueue sxMachineQueue = new SxMachineQueue();
                    sxMachineQueue.setWeldCid(sxWeldModel.getWeldCid());
                    sxMachineQueue.setWeldIp(sxWeldModel.getWeldIp());
                    sxMachineQueue.setWeldTime(DateTimeUtils.getNowDateTime());
                    CommonQueue.SX_ON_MACHINE_QUEUES.put(sxMachineQueue);
                }
            } catch (InterruptedException e) {
                log.error("松下开机设备添加到阻塞队列异常：", e);
            }
        }
    }

    /**
     * 松下焊接通道设定（锁定或解锁）
     *
     * @param weldCid  设备CID
     * @param function 0：解锁，1：锁定
     */
    private void sxChannelSetLock(String weldCid, int function) {
        try {
            SxWeldChannelSetting sxWeldChannelSetting = new SxWeldChannelSetting();
            sxWeldChannelSetting.setWeldCid(weldCid);
            sxWeldChannelSetting.setFunction(function);
            sxWeldChannelSetting.setReadWriteFlag(2);
            sxWeldChannelSetting.setChannelSelect(0);
            String str = sxProtocolJoint.sxWeldChannelSetProtocol(sxWeldChannelSetting);
            if (CommonUtils.isNotEmpty(weldCid) && CommonUtils.isNotEmpty(str)) {
                if (!CommonMap.SX_WELD_CID_CTX_MAP.isEmpty() && CommonMap.SX_WELD_CID_CTX_MAP.containsKey(weldCid)) {
                    Channel channel = CommonMap.SX_WELD_CID_CTX_MAP.get(weldCid).channel();
                    //判断该焊机通道是否打开、是否活跃、是否可写
                    if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                        channel.writeAndFlush(str);
                    }
                }
            }
        } catch (Exception e) {
            log.error("松下焊接通道设定异常：", e);
        }
    }

    /**
     * 松下焊机第二次握手验证
     *
     * @param param 入参
     */
    private void jnSxSecondVerify(HandlerParam param) {
        if (null != param) {
            ChannelHandlerContext ctx = param.getCtx();
            Map<String, String> map = param.getValue();
            if (map.containsKey("weldCid")) {
                String weldCid = JSON.toJSONString(map.get("weldCid"));
                //保存设备CID和通道对应关系
                CommonMap.SX_WELD_CID_CTX_MAP.put(weldCid, ctx);
                String clientAddress = CommonUtils.getClientAddress(ctx);
                CommonMap.SX_CTX_WELD_CID_MAP.put(clientAddress, weldCid);
            }
        }
    }

    /**
     * 松下焊机GL5软硬件参数存数据库
     *
     * @param param
     */
    private void jnSxGl5SoftHardParam(HandlerParam param) {
        if (null != param) {
            Map<String, String> map = param.getValue();
            ChannelHandlerContext ctx = param.getCtx();
            //松下焊机GL5软硬件参数存数据库
            if (map.containsKey(SxWeldModel.class.getSimpleName())) {
                SxWeldModel sxWeldModel = JSON.parseObject(map.get(SxWeldModel.class.getSimpleName()), SxWeldModel.class);
                //松下参数绑定[刷卡会解锁焊机]
                sxWeldDataBinding(ctx, sxWeldModel);
            }
        }
    }

    /**
     * 松下焊机GL5实时数据处理
     *
     * @param param
     */
    private void jnSxGl5RtDataManage(HandlerParam param) {
        if (null != param) {
            jnSxRtdManage(param);
        }
    }

    /**
     * 松下焊机GL5系列CO2状态信息
     *
     * @param param
     */
    private void jnSxGl5StatusManage(HandlerParam param) {
        if (null != param) {
            Map<String, String> map = param.getValue();
            ChannelHandlerContext ctx = param.getCtx();
            //松下焊机GL5状态信息发送到mq
            if (map.containsKey(SxStatusDataUI.class.getSimpleName())) {
                SxStatusDataUI sxStatusDataUi = JSON.parseObject(map.get(SxStatusDataUI.class.getSimpleName()), SxStatusDataUI.class);
                if (ObjectUtils.isEmpty(sxStatusDataUi)) {
                    return;
                }
                String clientAddress = CommonUtils.getClientAddress(ctx);
                if (CommonMap.SX_CTX_WELD_CID_MAP.containsKey(clientAddress)) {
                    String weldCid = CommonMap.SX_CTX_WELD_CID_MAP.get(clientAddress);
                    sxStatusDataUi.setWeldCid(weldCid);
                }
                //实体类转JSON字符串
                String message = JSON.toJSONString(sxStatusDataUi);
                //通过mqtt发送到服务端
                publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.SxStatusData), message);
            }
        }
    }

    /**
     * 松下GL5系列工艺信息和焊接通道设定
     *
     * @param param
     */
    private void jnSxGl5ProcessWeldSet(HandlerParam param) {
        if (null != param) {
            Map<String, String> map = param.getValue();
            ChannelHandlerContext ctx = param.getCtx();
            String clientAddress = CommonUtils.getClientAddress(ctx);
            String weldCid = "";
            if (CommonMap.SX_CTX_WELD_CID_MAP.containsKey(clientAddress)) {
                weldCid = CommonMap.SX_CTX_WELD_CID_MAP.get(clientAddress);
            }
            //松下GL5系列工艺索取返回(无数据)
            if (map.containsKey(SxProcessClaimReturn.class.getSimpleName())) {
                SxProcessClaimReturn sxProcessClaimReturn = JSON.parseObject(map.get(SxProcessClaimReturn.class.getSimpleName()), SxProcessClaimReturn.class);
                if (null != sxProcessClaimReturn) {
                    sxProcessClaimReturn.setWeldCid(weldCid);
                    //实体类转JSON字符串
                    String message = JSON.toJSONString(sxProcessClaimReturn);
                    //通过mqtt发送到服务端
                    publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.SxGL5ProcessClaimReturn), message);
                }
            }
            //松下GL5系列工艺下发返回
            if (map.containsKey(SxProcessReturn.class.getSimpleName())) {
                SxProcessReturn sxProcessReturn = JSON.parseObject(map.get(SxProcessReturn.class.getSimpleName()), SxProcessReturn.class);
                if (null != sxProcessReturn) {
                    sxProcessReturn.setWeldCid(weldCid);
                    //实体类转JSON字符串
                    String message = JSON.toJSONString(sxProcessReturn);
                    //通过mqtt发送到服务端
                    publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.SxGL5ProcessIssueReturn), message);
                }
            }
            //松下GL5系列工艺删除返回
            if (map.containsKey(SxProcessDeleteReturn.class.getSimpleName())) {
                SxProcessDeleteReturn sxProcessDeleteReturn = JSON.parseObject(map.get(SxProcessDeleteReturn.class.getSimpleName()), SxProcessDeleteReturn.class);
                if (null != sxProcessDeleteReturn) {
                    sxProcessDeleteReturn.setWeldCid(weldCid);
                    //实体类转JSON字符串
                    String message = JSON.toJSONString(sxProcessDeleteReturn);
                    //通过mqtt发送到服务端
                    publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.SxGL5ProcessDeleteReturn), message);
                }
            }
            //松下GL5系列焊机通道【通道设定返回、通道读取返回】
            if (map.containsKey(SxWeldChannelSetReturn.class.getSimpleName())) {
                SxWeldChannelSetReturn sxWeldChannelSetReturn = JSON.parseObject(map.get(SxWeldChannelSetReturn.class.getSimpleName()), SxWeldChannelSetReturn.class);
                if (null != sxWeldChannelSetReturn) {
                    sxWeldChannelSetReturn.setWeldCid(weldCid);
                    //实体类转JSON字符串
                    String message = JSON.toJSONString(sxWeldChannelSetReturn);
                    //通过mqtt发送到服务端
                    publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.SxGL5WeldChannelSetOrReadReturn), message);
                }
            }
        }
    }

    /**
     * 松下焊机GL5系列CO2工艺索取返回（有数据）
     *
     * @param param
     */
    private void jnSxCo2ProcessClaimReturn(HandlerParam param) {
        if (null != param) {
            Map<String, String> map = param.getValue();
            ChannelHandlerContext ctx = param.getCtx();
            //松下CO2工艺索取返回
            if (map.containsKey(SxCO2ProcessClaimReturn.class.getSimpleName())) {
                SxCO2ProcessClaimReturn sxCO2ProcessClaimReturn = JSON.parseObject(map.get(SxCO2ProcessClaimReturn.class.getSimpleName()), SxCO2ProcessClaimReturn.class);
                if (ObjectUtils.isEmpty(sxCO2ProcessClaimReturn)) {
                    return;
                }
                String clientAddress = CommonUtils.getClientAddress(ctx);
                if (CommonMap.SX_CTX_WELD_CID_MAP.containsKey(clientAddress)) {
                    String weldCid = CommonMap.SX_CTX_WELD_CID_MAP.get(clientAddress);
                    sxCO2ProcessClaimReturn.setWeldCid(weldCid);
                }
                //实体类转JSON字符串
                String message = JSON.toJSONString(sxCO2ProcessClaimReturn);
                //通过mqtt发送到服务端
                publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.SxGL5CO2ProcessClaimReturn), message);
            }
        }
    }

    /**
     * 松下焊机GL5系列TIG工艺索取返回（有数据）
     *
     * @param param
     */
    private void jnSxGl5TigProcessClaimReturn(HandlerParam param) {
        if (null != param) {
            Map<String, String> map = param.getValue();
            ChannelHandlerContext ctx = param.getCtx();
            //松下焊机GL5系列TIG工艺索取返回（有数据）
            if (map.containsKey(SxTIGProcessClaimReturn.class.getSimpleName())) {
                SxTIGProcessClaimReturn sxTIGProcessClaimReturn = JSON.parseObject(map.get(SxTIGProcessClaimReturn.class.getSimpleName()), SxTIGProcessClaimReturn.class);
                if (null != sxTIGProcessClaimReturn) {
                    String clientAddress = CommonUtils.getClientAddress(ctx);
                    if (CommonMap.SX_CTX_WELD_CID_MAP.containsKey(clientAddress)) {
                        String weldCid = CommonMap.SX_CTX_WELD_CID_MAP.get(clientAddress);
                        sxTIGProcessClaimReturn.setWeldCid(weldCid);
                    }
                    //实体类转JSON字符串
                    String message = JSON.toJSONString(sxTIGProcessClaimReturn);
                    //通过mqtt发送到服务端
                    publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.SxGL5TIGProcessClaimReturn), message);
                }
            }
        }
    }

    /**
     * 松下焊机【FR2、AT3】系列软硬件参数存数据库
     *
     * @param param
     */
    private void jnSxFr2At3SoftHardParam(HandlerParam param) {
        if (null != param) {
            Map<String, String> map = param.getValue();
            ChannelHandlerContext ctx = param.getCtx();
            //松下焊机GL5软硬件参数存数据库
            if (map.containsKey(SxWeldModel.class.getSimpleName())) {
                SxWeldModel sxWeldModel = JSON.parseObject(map.get(SxWeldModel.class.getSimpleName()), SxWeldModel.class);
                //松下参数绑定【刷卡会解锁焊机】
                sxWeldDataBinding(ctx, sxWeldModel);
            }
        }
    }

    /**
     * 松下焊机Fr2实时数据
     *
     * @param param
     */
    private void jnSxFr2Co2RtDataDbManage(HandlerParam param) {
        if (null != param) {
            jnSxRtdManage(param);
        }
    }

    /**
     * 松下FR2系列TIG实时数据
     *
     * @param param
     */
    private void jnSxFr2TigRtDataDbManage(HandlerParam param) {
        if (null != param) {
            jnSxRtdManage(param);
        }
    }

    /**
     * 松下焊机FR2状态信息发送到mq
     *
     * @param param
     */
    private void jnSxFr2StatusUiManage(HandlerParam param) {
        if (null != param) {
            Map<String, String> map = param.getValue();
            ChannelHandlerContext ctx = param.getCtx();
            //松下焊机FR2状态信息发送到mq
            if (map.containsKey(SxStatusDataUI.class.getSimpleName())) {
                SxStatusDataUI sxStatusDataUi = JSON.parseObject(map.get(SxStatusDataUI.class.getSimpleName()), SxStatusDataUI.class);
                if (ObjectUtils.isEmpty(sxStatusDataUi)) {
                    return;
                }
                String clientAddress = CommonUtils.getClientAddress(ctx);
                if (CommonMap.SX_CTX_WELD_CID_MAP.containsKey(clientAddress)) {
                    String weldCid = CommonMap.SX_CTX_WELD_CID_MAP.get(clientAddress);
                    sxStatusDataUi.setWeldCid(weldCid);
                }
                //实体类转JSON字符串
                String message = JSON.toJSONString(sxStatusDataUi);
                //通过mqtt发送到服务端
                publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.SxStatusData), message);
            }
        }
    }

    /**
     * 松下焊机【FR2、AT3】系列通道参数【查询回复（无参数）、下载回复、删除回复】
     *
     * @param param
     */
    private void jnSxChannelParamReply(HandlerParam param) {
        if (null != param) {
            Map<String, String> map = param.getValue();
            ChannelHandlerContext ctx = param.getCtx();
            //松下焊机【FR2、AT3】系列通道参数【查询回复（无参数）、下载回复、删除回复】
            if (map.containsKey(SxChannelParamReply.class.getSimpleName())) {
                SxChannelParamReply sxChannelParamReply = JSON.parseObject(map.get(SxChannelParamReply.class.getSimpleName()), SxChannelParamReply.class);
                if (null != sxChannelParamReply) {
                    String clientAddress = CommonUtils.getClientAddress(ctx);
                    if (CommonMap.SX_CTX_WELD_CID_MAP.containsKey(clientAddress)) {
                        String weldCid = CommonMap.SX_CTX_WELD_CID_MAP.get(clientAddress);
                        sxChannelParamReply.setWeldCid(weldCid);
                    }
                    //实体类转JSON字符串
                    String message = JSON.toJSONString(sxChannelParamReply);
                    //通过mqtt发送到服务端
                    publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.SxFR2OrAT3ChannelParamReply), message);
                }
            }
        }
    }

    /**
     * 松下焊机FR2系列通道参数【查询回复（有参数）】
     *
     * @param param
     */
    private void jnSxFr2ChannelParamReplyHave(HandlerParam param) {
        if (null != param) {
            Map<String, String> map = param.getValue();
            //松下焊机FR2系列通道参数【查询回复（有参数）】
            if (map.containsKey(SxChannelParamReplyHave.class.getSimpleName())) {
                SxChannelParamReplyHave sxChannelParamReplyHave = JSON.parseObject(map.get(SxChannelParamReplyHave.class.getSimpleName()), SxChannelParamReplyHave.class);
                if (null != sxChannelParamReplyHave) {
                    //实体类转JSON字符串
                    String message = JSON.toJSONString(sxChannelParamReplyHave);
                    //通过mqtt发送到服务端
                    publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.SxFR2ChannelParamReplyHave), message);
                }
            }
        }
    }

    /**
     * 松下AT3系列查询回复（有参数）
     *
     * @param param
     */
    private void jnSxAt3ParamQueryReturn(HandlerParam param) {
        if (null != param) {
            Map<String, String> map = param.getValue();
            ChannelHandlerContext ctx = param.getCtx();
            //松下AT3系列查询回复（有参数）
            if (map.containsKey(At3ParamQueryReturn.class.getSimpleName())) {
                At3ParamQueryReturn at3ParamQueryReturn = JSON.parseObject(map.get(At3ParamQueryReturn.class.getSimpleName()), At3ParamQueryReturn.class);
                if (null != at3ParamQueryReturn) {
                    String clientAddress = CommonUtils.getClientAddress(ctx);
                    if (CommonMap.SX_CTX_WELD_CID_MAP.containsKey(clientAddress)) {
                        String weldCid = CommonMap.SX_CTX_WELD_CID_MAP.get(clientAddress);
                        at3ParamQueryReturn.setWeldCid(weldCid);
                    }
                    //实体类转JSON字符串
                    String message = JSON.toJSONString(at3ParamQueryReturn);
                    //通过mqtt发送到服务端
                    publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.SxAT3ParamQueryReturn), message);
                }
            }
        }
    }

    /**
     * 松下实时数据统一处理
     *
     * @param param 入参
     */
    private void jnSxRtdManage(HandlerParam param) {
        Map<String, String> map = param.getValue();
        ChannelHandlerContext ctx = param.getCtx();
        String clientAddress = CommonUtils.getClientAddress(ctx);
        String weldCid = "";
        SxWeldModel sxWeldModel = null;
        if (CommonMap.SX_CTX_WELD_CID_MAP.containsKey(clientAddress)) {
            weldCid = CommonMap.SX_CTX_WELD_CID_MAP.get(clientAddress);
        }
        if (CommonMap.SX_CTX_WELD_INFO_MAP.containsKey(clientAddress)) {
            sxWeldModel = CommonMap.SX_CTX_WELD_INFO_MAP.get(clientAddress);
        }
        try {
            //松下焊机实时数据发送到mq
            if (map.containsKey(SxRtDataUi.class.getSimpleName())) {
                SxRtDataUi sxRtDataUi = JSON.parseObject(map.get(SxRtDataUi.class.getSimpleName()), SxRtDataUi.class);
                sxRtDataPublishWeb(weldCid, sxRtDataUi, sxWeldModel);
            }
        } catch (Exception e) {
            log.error("松下焊机实时数据发送到mq异常：{}", e.getMessage());
        }
        try {
            //松下焊机实时数据存数据库
            if (map.containsKey(SxRtDataDb.class.getSimpleName())) {
                //如果找不到通道的CID，则不能增加设备实时数据
                if (CommonUtils.isEmpty(weldCid)) {
                    log.warn("[jnSxRtdManage]当前通道找不到CID：-----> {}", clientAddress);
                    return;
                }
                SxRtDataDb sxRtDataDb = JSON.parseObject(map.get(SxRtDataDb.class.getSimpleName()), SxRtDataDb.class);
                sxRtDataAddDb(weldCid, sxWeldModel, sxRtDataDb);
            }
        } catch (Exception e) {
            log.error("松下焊机实时数据存数据库异常：{}", e.getMessage());
        }
    }

    private void sxRtDataPublishWeb(String weldCid, SxRtDataUi sxRtDataUi, SxWeldModel sxWeldModel) {
        if (null != sxRtDataUi) {
            sxRtDataUi.setWeldCid(weldCid);
            if (null != sxWeldModel) {
                //设备编码
                sxRtDataUi.setWeldCode(sxWeldModel.getWeldCode());
                //设备机型
                sxRtDataUi.setWeldModel(sxWeldModel.getWeldModel());
            }
            //实体类转JSON字符串
            String message = JSON.toJSONString(sxRtDataUi);
            //通过mqtt发送到服务端
            publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.SxRtData), message);
        }
    }

    private void sxRtDataAddDb(String weldCid, SxWeldModel sxWeldModel, SxRtDataDb sxRtDataDb) {
        if (null != sxRtDataDb) {
            sxRtDataDb.setWeldCid(weldCid);
            //刷卡领取任务后进行数据绑定
            Map<String, TaskClaimIssue> sxTaskClaimMap = CommonMap.SX_TASK_CLAIM_MAP;
            if (!sxTaskClaimMap.isEmpty() && sxTaskClaimMap.containsKey(weldCid)) {
                TaskClaimIssue taskClaimIssue = sxTaskClaimMap.get(weldCid);
                if (null != taskClaimIssue) {
                    sxRtDataDb.setWelderId(taskClaimIssue.getWelderId());
                    sxRtDataDb.setWelderName(taskClaimIssue.getWelderName());
                    sxRtDataDb.setWelderDeptId(taskClaimIssue.getWelderDeptId());
                    sxRtDataDb.setTaskId(taskClaimIssue.getTaskId());
                    sxRtDataDb.setTaskName(taskClaimIssue.getTaskName());
                    sxRtDataDb.setTaskNo(taskClaimIssue.getTaskNo());
                }
            }
            if (null != sxWeldModel) {
                //设备编码
                sxRtDataDb.setWeldCode(sxWeldModel.getWeldCode());
                //设备机型
                sxRtDataDb.setWeldModel(sxWeldModel.getWeldModel());
            }
            //如果待机数据不存储。则根据起弧、收弧各存储一条待机数据
            if (!CommonFunction.isSxStandbySave()) {
                //初期焊接（起弧），增加一条待机数据
                if (sxRtDataDb.getWeldStatus() == 5) {
                    SxRtDataDb sxdata = new SxRtDataDb();
                    sxdata.setWeldStatus(0);
                    LocalDateTime parse = LocalDateTime.parse(sxdata.getWeldTime(), DateTimeUtils.DEFAULT_DATETIME);
                    //减去1秒
                    String weldTime = parse.minusSeconds(1).format(DateTimeUtils.DEFAULT_DATETIME);
                    sxdata.setWeldTime(weldTime);
                    sxdata.setRealityWeldEle(BigDecimal.ZERO);
                    sxdata.setRealityWeldVol(BigDecimal.ZERO);
                    CommonQueue.SX_LINKED_BLOCKING_QUEUE.offer(sxdata);
                }
                //收弧焊接（收弧），增加一条待机数据
                else if (sxRtDataDb.getWeldStatus() == 10) {
                    SxRtDataDb sxdata = new SxRtDataDb();
                    sxdata.setWeldStatus(0);
                    LocalDateTime parse = LocalDateTime.parse(sxdata.getWeldTime(), DateTimeUtils.DEFAULT_DATETIME);
                    //加上1秒
                    String weldTime = parse.plusSeconds(1).format(DateTimeUtils.DEFAULT_DATETIME);
                    sxdata.setWeldTime(weldTime);
                    sxdata.setRealityWeldEle(BigDecimal.ZERO);
                    sxdata.setRealityWeldVol(BigDecimal.ZERO);
                    CommonQueue.SX_LINKED_BLOCKING_QUEUE.offer(sxdata);
                }
            }
            //添加到松下阻塞队列（通过定时任务定时存储）,offer：如果队列已满，则不再添加
            CommonQueue.SX_LINKED_BLOCKING_QUEUE.offer(sxRtDataDb);
            //添加实时数据到松下的队列，并定时存储到ProcessDB
            DBCreateMethod.addSxRtDataToProcessDbQueue(sxRtDataDb);
        }
    }

    /**
     * 松下设备关机数据处理
     */
    private void sxShutdownHandler(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        //客户端IP地址
        String clientIp = insocket.getAddress().getHostAddress();
        SxRtDataUi sxRtDataUi = new SxRtDataUi();
        String clientAddress = CommonUtils.getClientAddress(ctx);
        if (CommonMap.SX_CTX_WELD_INFO_MAP.containsKey(clientAddress)) {
            SxWeldModel sxWeld = CommonMap.SX_CTX_WELD_INFO_MAP.get(clientAddress);
            if (null != sxWeld) {
                sxRtDataUi.setWeldCode(sxWeld.getWeldCode());
                sxRtDataUi.setWeldModel(sxWeld.getWeldModel());
            }
            CommonMap.SX_CTX_WELD_INFO_MAP.remove(clientAddress);
        }
        if (CommonMap.SX_CTX_WELD_CID_MAP.containsKey(clientAddress)) {
            String weldCid = CommonMap.SX_CTX_WELD_CID_MAP.get(clientAddress);
            sxRtDataUi.setWeldCid(weldCid);
            CommonMap.SX_CTX_WELD_CID_MAP.remove(clientAddress);
            CommonMap.SX_WELD_CID_CTX_MAP.remove(weldCid);
            try {
                SxMachineQueue sxMachineQueue = new SxMachineQueue();
                sxMachineQueue.setWeldCid(weldCid);
                sxMachineQueue.setWeldIp(clientIp);
                sxMachineQueue.setWeldTime(DateTimeUtils.getNowDateTime());
                CommonQueue.SX_OFF_MACHINE_QUEUES.put(sxMachineQueue);
            } catch (InterruptedException e) {
                log.error("松下设备关机添加到阻塞队列异常：", e);
            }
        }
        sxRtDataUi.setWeldIp(clientIp);
        //-1 为关机
        sxRtDataUi.setWeldStatus(-1);
        //实体类转JSON字符串
        String message = JSON.toJSONString(sxRtDataUi);
        //通过mqtt发送到服务端
        publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.SxRtData), message);
    }

    /**
     * 发布消息
     *
     * @param topic
     * @param message
     */
    private void publishMessageToMqtt(String topic, String message) {
        EmqMqttClient.publishMessage(topic, message, 0);
    }

}
