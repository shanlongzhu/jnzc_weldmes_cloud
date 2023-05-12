package com.shth.das.business.dataup.otc;

import com.alibaba.fastjson2.JSON;
import com.shth.das.business.dataup.BaseHandler;
import com.shth.das.codeparam.HandlerParam;
import com.shth.das.common.*;
import com.shth.das.mqtt.EmqMqttClient;
import com.shth.das.pojo.db.OtcMachineQueue;
import com.shth.das.pojo.jnotc.*;
import com.shth.das.processdb.DBCreateMethod;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 江南项目数据解析类
 */
@Slf4j
public class JnOtcProtocolHandle extends BaseHandler {

    public JnOtcProtocolHandle() {
        init();
    }

    private final Map<Integer, Consumer<HandlerParam>> otcHandlerMapping = new HashMap<>();

    private void init() {
        //OTC（1.0）实时数据解析
        this.otcHandlerMapping.put(282, this::jnRtDataManage);
        //OTC（1.0）工艺下发返回解析
        this.otcHandlerMapping.put(24, this::otcIssueReturnManage);
        //OTC（1.0）索取返回协议解析
        this.otcHandlerMapping.put(112, this::otcClaimReturnManage);
        //OTC（1.0）密码返回和控制命令返回[新增程序包路径下发返回]
        this.otcHandlerMapping.put(22, this::otcPwdCmdReturnManage);
    }

    @Override
    protected void dataHandler(HandlerParam param) {
        if (this.otcHandlerMapping.containsKey(param.getKey())) {
            this.otcHandlerMapping.get(param.getKey()).accept(param);
        }
    }

    @Override
    protected void shutdownHandler(ChannelHandlerContext ctx) {
        otcShutdownHandler(ctx);
    }

    /**
     * 江南OTC设备关机数据处理
     */
    private void otcShutdownHandler(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        //客户端IP地址
        String clientIp = insocket.getAddress().getHostAddress();
        //有客户端终止连接则发送关机数据到mq，刷新实时界面
        if (CommonMap.OTC_CTX_GATHER_NO_MAP.containsKey(ctx)) {
            //采集编号
            String gatherNo = CommonMap.OTC_CTX_GATHER_NO_MAP.get(ctx);
            //OTC设备关机数据添加到阻塞队列
            try {
                OtcMachineQueue otcOnMachineQueue = new OtcMachineQueue();
                otcOnMachineQueue.setGatherNo(gatherNo);
                otcOnMachineQueue.setWeldIp(clientIp);
                otcOnMachineQueue.setWeldTime(DateTimeUtils.getNowDateTime());
                CommonQueue.OTC_OFF_MACHINE_QUEUES.put(otcOnMachineQueue);
            } catch (InterruptedException e) {
                log.error("OTC设备关机数据添加到阻塞队列异常：", e);
            }
            //关机数据发送到mq
            List<JNRtDataUI> dataList = new ArrayList<>();
            JNRtDataUI jnRtDataUi = new JNRtDataUI();
            jnRtDataUi.setGatherNo(gatherNo);
            //-1 为关机
            jnRtDataUi.setWeldStatus(-1);
            jnRtDataUi.setElectricity(BigDecimal.ZERO);
            jnRtDataUi.setVoltage(BigDecimal.ZERO);
            jnRtDataUi.setWeldIp(clientIp);
            jnRtDataUi.setWeldTime(DateTimeUtils.getNowDateTime());
            dataList.add(jnRtDataUi);
            String dataArray = JSON.toJSONString(dataList);
            publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1RtData), dataArray);
            CommonMap.OTC_GATHER_NO_CTX_MAP.remove(gatherNo);
            CommonMap.OTC_CTX_GATHER_NO_MAP.remove(ctx);
        }
    }

    /**
     * OTC设备刷卡启用设备功能
     */
    private void slotCardEnableDevice(ChannelHandlerContext ctx, String gatherNo) {
        //进制转换，长度拼接
        String gatherno = CommonUtils.lengthJoint(gatherNo, 4);
        Channel channel = ctx.channel();
        //是否开启（true：开启）
        if (CommonFunction.isSlotCardEnableDevice()) {
            try {
                //判断当前焊机是否已经刷卡（true：刷过卡），刷卡后解锁焊机
                if (!CommonMap.OTC_TASK_CLAIM_MAP.isEmpty() && CommonMap.OTC_TASK_CLAIM_MAP.containsKey(gatherNo)) {
                    //总长度：24（解锁焊机指令）
                    String str = "007E0A01010119" + gatherno + "00007D";
                    //判断该焊机通道是否打开、是否活跃、是否可写
                    if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                        channel.writeAndFlush(str);
                    }
                }
                //没有刷卡，锁定焊机
                else {
                    //总长度：24（锁焊机指令）
                    String str = "007E0A01010118" + gatherno + "00007D";
                    //判断该焊机通道是否打开、是否活跃、是否可写
                    if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                        channel.writeAndFlush(str);
                    }
                }
            } catch (Exception e) {
                log.error("OTC设备刷卡启用设备功能异常：", e);
            }
        } else {
            try {
                //总长度：24（解锁焊机指令）
                String str = "007E0A01010119" + gatherno + "00007D";
                //判断该焊机通道是否打开、是否活跃、是否可写
                if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                    channel.writeAndFlush(str);
                }
            } catch (Exception e) {
                log.error("解锁焊机指令异常：", e);
            }
        }
    }

    /**
     * 采集盒IP地址盒采集编号绑定
     */
    private void gatherNoIpBinding(String clientIp, ChannelHandlerContext ctx, String gatherNo) {
        if (CommonUtils.isEmpty(gatherNo) || ObjectUtils.isEmpty(ctx)) {
            return;
        }
        try {
            //判断map集合是否有，没有则新增
            if (!CommonMap.OTC_GATHER_NO_CTX_MAP.containsKey(gatherNo)) {
                CommonMap.OTC_GATHER_NO_CTX_MAP.put(gatherNo, ctx);
                //刷卡领任务功能判断
                slotCardEnableDevice(ctx, gatherNo);
                try {
                    OtcMachineQueue otcMachineSaveQueue = new OtcMachineQueue();
                    otcMachineSaveQueue.setWeldTime(DateTimeUtils.getNowDateTime());
                    otcMachineSaveQueue.setGatherNo(gatherNo);
                    otcMachineSaveQueue.setWeldIp(clientIp);
                    //加入到OTC设备阻塞队列临时存储（put：如果阻塞队列已满，则进行等待）
                    CommonQueue.OTC_ON_MACHINE_QUEUES.put(otcMachineSaveQueue);
                } catch (InterruptedException e) {
                    log.error("OTC开机设备阻塞队列添加：", e);
                }
            }
            if (!CommonMap.OTC_CTX_GATHER_NO_MAP.containsKey(ctx)) {
                CommonMap.OTC_CTX_GATHER_NO_MAP.put(ctx, gatherNo);
            }
        } catch (Exception e) {
            log.error("采集盒IP地址盒采集编号绑定异常：{}", e.getMessage());
        }
    }

    /**
     * OTC1.0实时数据处理
     *
     * @param handlerParam 入参
     */
    private void jnRtDataManage(HandlerParam handlerParam) {
        if (ObjectUtils.isEmpty(handlerParam)) {
            return;
        }
        Map<String, String> map = handlerParam.getValue();
        try {
            //实时数据发送到前端
            if (map.containsKey(JNRtDataUI.class.getSimpleName())) {
                List<JNRtDataUI> jnRtDataUis = JSON.parseArray(map.get(JNRtDataUI.class.getSimpleName()), JNRtDataUI.class);
                if (CommonUtils.isNotEmpty(jnRtDataUis)) {
                    String gatherNo = jnRtDataUis.get(0).getGatherNo();
                    String clientIp = jnRtDataUis.get(0).getWeldIp();
                    //采集盒IP地址盒采集编号绑定
                    gatherNoIpBinding(clientIp, handlerParam.getCtx(), gatherNo);
                    //集合转字符串[消除对同一对象的循环引用]
                    String message = JSON.toJSONString(jnRtDataUis);
                    //通过mqtt发送到服务端
                    publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1RtData), message);
                }
            }
        } catch (Exception e) {
            log.error("OTC1.0实时数据发前端处理异常：", e);
        }
        try {
            //实时数据存数据库
            if (map.containsKey(JNRtDataDB.class.getSimpleName())) {
                List<JNRtDataDB> list = JSON.parseArray(map.get(JNRtDataDB.class.getSimpleName()), JNRtDataDB.class);
                if (CommonUtils.isNotEmpty(list)) {
                    //添加到OTC阻塞队列（通过定时任务存储）;offer:当阻塞队列满了，则不再增加
                    list.forEach(CommonQueue.OTC_LINKED_BLOCKING_QUEUE::offer);
                    //判断是否启用ProcessDB，true：添加到队列中
                    if (CommonFunction.isEnableProcessDB()) {
                        //添加到实时数据库的阻塞队列
                        list.forEach(DBCreateMethod::addOtcRtDataToProcessDbQueue);
                    }
                }
            }
        } catch (Exception e) {
            log.error("OTC1.0实时数据存数据库处理异常：", e);
        }
    }

    /**
     * 工艺下发返回解析
     *
     * @param handlerParam
     */
    private void otcIssueReturnManage(HandlerParam handlerParam) {
        if (null != handlerParam) {
            Map<String, String> map = handlerParam.getValue();
            //工艺下发返回
            if (map.containsKey(JNProcessIssueReturn.class.getSimpleName())) {
                String message = map.get(JNProcessIssueReturn.class.getSimpleName());
                //通过mqtt发送到服务端
                publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1ProcessIssueReturn), message);
            }
        }
    }

    /**
     * 索取返回协议解析
     *
     * @param handlerParam
     */
    private void otcClaimReturnManage(HandlerParam handlerParam) {
        if (null != handlerParam) {
            Map<String, String> map = handlerParam.getValue();
            //工艺索取返回
            if (map.containsKey(JNProcessClaimReturn.class.getSimpleName())) {
                String message = map.get(JNProcessClaimReturn.class.getSimpleName());
                //通过mqtt发送到服务端
                publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1ProcessClaimReturn), message);
            }
        }
    }

    /**
     * 密码返回和控制命令返回
     *
     * @param handlerParam 入参
     */
    private void otcPwdCmdReturnManage(HandlerParam handlerParam) {
        if (null != handlerParam) {
            Map<String, String> map = handlerParam.getValue();
            ChannelHandlerContext ctx = handlerParam.getCtx();
            //密码返回
            if (map.containsKey(JNPasswordReturn.class.getSimpleName())) {
                String message = map.get(JNPasswordReturn.class.getSimpleName());
                //通过mqtt发送到服务端
                publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1PasswordReturn), message);
            }
            //控制命令返回
            if (map.containsKey(JNCommandReturn.class.getSimpleName())) {
                String message = map.get(JNCommandReturn.class.getSimpleName());
                //通过mqtt发送到服务端
                publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1CommandReturn), message);
            }
            //锁焊机或者解锁焊机返回
            if (map.containsKey(JnLockMachineReturn.class.getSimpleName())) {
                JnLockMachineReturn jnLockMachineReturn = JSON.parseObject(map.get(JnLockMachineReturn.class.getSimpleName()), JnLockMachineReturn.class);
                if (null != jnLockMachineReturn) {
                    try {
                        //采集编号
                        String gatherNo = jnLockMachineReturn.getGatherNo();
                        //控制命令：（18：锁焊机，19：解锁焊机）
                        int command = jnLockMachineReturn.getCommand();
                        //接收结果:0 成功（如果成功，删除重试次数）
                        if (jnLockMachineReturn.getResult() == 0) {
                            if (CommonMap.OTC_LOCK_FAIL_RETRY_MAP.containsKey(gatherNo)) {
                                CommonMap.OTC_LOCK_FAIL_RETRY_MAP.get(gatherNo).remove(command);
                            }
                        }
                        //接收结果:1 失败（失败进行重试）
                        else if (jnLockMachineReturn.getResult() == 1) {
                            //判断是否有当前设备（true：增加重试次数）
                            if (CommonMap.OTC_LOCK_FAIL_RETRY_MAP.containsKey(gatherNo)) {
                                Map<Integer, Integer> otcLockMap = CommonMap.OTC_LOCK_FAIL_RETRY_MAP.get(gatherNo);
                                if (otcLockMap.containsKey(command)) {
                                    //得到重试次数
                                    Integer numOfRetries = otcLockMap.get(command);
                                    if (numOfRetries < 3) {
                                        numOfRetries++;
                                        otcLockMap.put(command, numOfRetries);
                                        //锁焊机或解锁焊机再次下行
                                        otcLockMachineRetries(gatherNo, String.valueOf(command), ctx.channel());
                                    }
                                }
                                //没有直接新增一个
                                else {
                                    otcLockMap.put(command, 1);
                                    CommonMap.OTC_LOCK_FAIL_RETRY_MAP.put(gatherNo, otcLockMap);
                                    //锁焊机或解锁焊机再次下行
                                    otcLockMachineRetries(gatherNo, String.valueOf(command), ctx.channel());
                                }
                            }
                            //没有直接新增一个
                            else {
                                Map<Integer, Integer> otcLockMap = new HashMap<>();
                                otcLockMap.put(command, 1);
                                CommonMap.OTC_LOCK_FAIL_RETRY_MAP.put(gatherNo, otcLockMap);
                                //锁焊机或解锁焊机再次下行
                                otcLockMachineRetries(gatherNo, String.valueOf(command), ctx.channel());
                            }
                        }
                    } catch (Exception e) {
                        log.error("锁焊机或者解锁焊机返回异常：", e);
                    }
                }
            }
            //程序包路径下发返回
            if (map.containsKey(OtcV1ProgramPathIssueReturn.class.getSimpleName())) {
                String message = map.get(OtcV1ProgramPathIssueReturn.class.getSimpleName());
                //通过mqtt发送到服务端
                publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1ProgramPathIssueReturn), message);
            }
        }
    }

    /**
     * 锁焊机或者解锁焊机重试
     *
     * @param gatherNo 采集编号(10进制)
     * @param command  控制命令
     * @param channel  通道
     */
    private void otcLockMachineRetries(String gatherNo, String command, Channel channel) {
        try {
            gatherNo = CommonUtils.lengthJoint(gatherNo, 4);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("007E0A010101").append(command).append(gatherNo).append("00007D");
            //总长度：24（解锁焊机指令）
            //String str = "007E0A010101" + command + gatherNo + "00007D";
            //判断该焊机通道是否打开、是否活跃、是否可写
            if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                channel.writeAndFlush(stringBuilder.toString());
            }
        } catch (Exception e) {
            log.error("锁焊机或者解锁焊机重试异常：", e);
        }
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
