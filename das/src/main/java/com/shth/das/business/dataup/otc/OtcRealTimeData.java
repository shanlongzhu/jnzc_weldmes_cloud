package com.shth.das.business.dataup.otc;

import com.alibaba.fastjson2.JSON;
import com.shth.das.business.JnOtcRtDataProtocol;
import com.shth.das.codeparam.HandlerParam;
import com.shth.das.common.*;
import com.shth.das.mqtt.EmqMqttClient;
import com.shth.das.pojo.db.OtcMachineQueue;
import com.shth.das.pojo.jnotc.JNRtDataDB;
import com.shth.das.pojo.jnotc.JNRtDataUI;
import com.shth.das.processdb.DBCreateMethod;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OTC实时数据
 */
@Slf4j
public class OtcRealTimeData implements OtcDataUpHandle {

    /**
     * 协议判定
     *
     * @param str
     * @return
     */
    @Override
    public String protocolDecide(String str) {
        if (CommonUtils.isEmpty(str)) {
            return null;
        }
        if (str.length() != 282) {
            return null;
        }
        if ("7E".equals(str.substring(0, 2)) && "7D".equals(str.substring(280, 282))) {
            return UpTopicEnum.OtcV1RtData.getValue();
        }
        return null;
    }

    /**
     * 协议解析
     *
     * @return
     */
    @Override
    public HandlerParam protocolAnalysis(ChannelHandlerContext ctx, String str) {
        String clientIp = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
        HandlerParam handlerParam = new HandlerParam();
        Map<String, String> map = new HashMap<>();
        //存数据库
        List<JNRtDataDB> jnRtDataDbs = JnOtcRtDataProtocol.jnRtDataDbAnalysis(str);
        //发送前端
        List<JNRtDataUI> jnRtDataUis = JnOtcRtDataProtocol.jnRtDataUiAnalysis(clientIp, str);
        if (CommonUtils.isNotEmpty(jnRtDataDbs)) {
            map.put(JNRtDataDB.class.getSimpleName(), JSON.toJSONString(jnRtDataDbs));
        }
        if (CommonUtils.isNotEmpty(jnRtDataUis)) {
            map.put(JNRtDataUI.class.getSimpleName(), JSON.toJSONString(jnRtDataUis));
        }
        handlerParam.setKey(str.length());
        handlerParam.setValue(map);
        return handlerParam;
    }

    /**
     * 业务处理
     */
    @Override
    public void protocolHandle(HandlerParam handlerParam) {
        if (ObjectUtils.isEmpty(handlerParam)) {
            return;
        }
        try {
            Map<String, String> map = handlerParam.getValue();
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
                    // publishMessageToMqtt(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1RtData), message);
                    EmqMqttClient.publishMessage(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1RtData), message, 0);
                }
            }
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
            log.error("OTC1.0实时数据处理异常：", e);
        }
    }

    /**
     * 采集盒IP地址盒采集编号绑定
     */
    private static void gatherNoIpBinding(String clientIp, ChannelHandlerContext ctx, String gatherNo) {
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
            } catch (Exception e) {
                log.error("OTC开机设备阻塞队列添加：", e);
            }
        }
        if (!CommonMap.OTC_CTX_GATHER_NO_MAP.containsKey(ctx)) {
            CommonMap.OTC_CTX_GATHER_NO_MAP.put(ctx, gatherNo);
        }
    }

    /**
     * OTC设备刷卡启用设备功能
     */
    private static void slotCardEnableDevice(ChannelHandlerContext ctx, String gatherNo) {
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

}
