package com.shth.das.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.shth.das.codeparam.HandlerParam;
import com.shth.das.common.*;
import com.shth.das.mqtt.EmqMqttClient;
import com.shth.das.pojo.db.GatherModel;
import com.shth.das.pojo.db.OtcMachineQueue;
import com.shth.das.pojo.db.TaskClaimIssue;
import com.shth.das.pojo.db.WeldModel;
import com.shth.das.pojo.jnotc.*;
import com.shth.das.processdb.DBCreateMethod;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 江南项目数据解析类
 */
@Component
@Slf4j
public class JnOtcRtDataProtocol {

    /**
     * OTC设备刷卡启用设备功能
     */
    private void slotCardEnableDevice(ChannelHandlerContext ctx, String gatherNo) {
        //进制转换，长度拼接
        final String gatherno = CommonUtils.lengthJoint(gatherNo, 4);
        final Channel channel = ctx.channel();
        //是否开启（true：开启）
        if (CommonFunction.isSlotCardEnableDevice()) {
            try {
                //判断当前焊机是否已经刷卡（true：刷过卡），刷卡后解锁焊机
                if (!CommonMap.OTC_TASK_CLAIM_MAP.isEmpty() && CommonMap.OTC_TASK_CLAIM_MAP.containsKey(gatherNo)) {
                    //总长度：24（解锁焊机指令）
                    final String str = "007E0A01010119" + gatherno + "00007D";
                    //判断该焊机通道是否打开、是否活跃、是否可写
                    if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                        channel.writeAndFlush(str).sync();
                    }
                }
                //没有刷卡，锁定焊机
                else {
                    //总长度：24（锁焊机指令）
                    final String str = "007E0A01010118" + gatherno + "00007D";
                    //判断该焊机通道是否打开、是否活跃、是否可写
                    if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                        channel.writeAndFlush(str).sync();
                    }
                }
            } catch (InterruptedException e) {
                log.error("OTC设备刷卡启用设备功能异常：", e);
            }
        } else {
            try {
                //总长度：24（解锁焊机指令）
                final String str = "007E0A01010119" + gatherno + "00007D";
                //判断该焊机通道是否打开、是否活跃、是否可写
                if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                    channel.writeAndFlush(str).sync();
                }
            } catch (InterruptedException e) {
                log.error("解锁焊机指令异常：", e);
            }
        }
    }

    /**
     * 采集盒IP地址盒采集编号绑定
     */
    private void gatherNoIpBinding(String clientIp, ChannelHandlerContext ctx, String gatherNo) {
        //判断map集合是否有，没有则新增
        if (!CommonMap.OTC_GATHER_NO_CTX_MAP.containsKey(gatherNo)) {
            CommonMap.OTC_GATHER_NO_CTX_MAP.put(gatherNo, ctx);
            //刷卡领任务功能判断
            this.slotCardEnableDevice(ctx, gatherNo);
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
    }

    /**
     * OTC1.0实时数据处理
     *
     * @param handlerParam 入参
     */
    @SuppressWarnings("unchecked")
    public void jnRtDataManage(HandlerParam handlerParam) {
        if (null != handlerParam) {
            Map<String, Object> map = handlerParam.getValue();
            //实时数据发送到前端
            if (map.containsKey("JNRtDataUI")) {
                List<JNRtDataUI> jnRtDataUis = (List<JNRtDataUI>) map.get("JNRtDataUI");
                if (CommonUtils.isNotEmpty(jnRtDataUis)) {
                    String gatherNo = jnRtDataUis.get(0).getGatherNo();
                    String clientIp = jnRtDataUis.get(0).getWeldIp();
                    //采集盒IP地址盒采集编号绑定
                    this.gatherNoIpBinding(clientIp, handlerParam.getCtx(), gatherNo);
                    //集合转字符串[消除对同一对象的循环引用]
                    String message = JSON.toJSONString(jnRtDataUis, SerializerFeature.DisableCircularReferenceDetect);
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1RtData), message, 0);
                }
            }
            //实时数据存数据库
            if (map.containsKey("JNRtDataDB")) {
                List<JNRtDataDB> list = (List<JNRtDataDB>) map.get("JNRtDataDB");
                if (CommonUtils.isNotEmpty(list)) {
                    //添加到OTC阻塞队列（通过定时任务存储）;offer:当阻塞队列满了，则不再增加
                    list.forEach(CommonQueue.OTC_LINKED_BLOCKING_QUEUE::offer);
                    //判断是否启用ProcessDB，true：添加到队列中
                    if (CommonFunction.isEnableProcessDB()) {
                        //添加到实时数据库的阻塞队列
                        list.forEach(new DBCreateMethod()::addOtcRtDataToProcessDbQueue);
                    }
                }
            }
        }
    }

    /**
     * 工艺下发返回解析
     *
     * @param handlerParam
     */
    public void otcIssueReturnManage(HandlerParam handlerParam) {
        if (null != handlerParam) {
            Map<String, Object> map = handlerParam.getValue();
            //工艺下发返回
            if (map.containsKey("JNProcessIssueReturn")) {
                JNProcessIssueReturn processIssueReturn = (JNProcessIssueReturn) map.get("JNProcessIssueReturn");
                if (null != processIssueReturn) {
                    //Java类转JSON字符串
                    String message = JSON.toJSONString(processIssueReturn);
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1ProcessIssueReturn), message, 0);
                }
            }
        }
    }

    /**
     * 索取返回协议解析
     *
     * @param handlerParam
     */
    public void otcClaimReturnManage(HandlerParam handlerParam) {
        if (null != handlerParam) {
            Map<String, Object> map = handlerParam.getValue();
            //工艺索取返回
            if (map.containsKey("JNProcessClaimReturn")) {
                JNProcessClaimReturn processClaimReturn = (JNProcessClaimReturn) map.get("JNProcessClaimReturn");
                if (null != processClaimReturn) {
                    String message = JSON.toJSONString(processClaimReturn);
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1ProcessClaimReturn), message, 0);
                }
            }
        }
    }

    /**
     * 密码返回和控制命令返回
     *
     * @param handlerParam 入参
     */
    public void otcPwdCmdReturnManage(HandlerParam handlerParam) {
        if (null != handlerParam) {
            final Map<String, Object> map = handlerParam.getValue();
            final ChannelHandlerContext ctx = handlerParam.getCtx();
            //密码返回
            if (map.containsKey("JNPasswordReturn")) {
                JNPasswordReturn passwordReturn = (JNPasswordReturn) map.get("JNPasswordReturn");
                if (null != passwordReturn) {
                    String message = JSON.toJSONString(passwordReturn);
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1PasswordReturn), message, 0);
                }
            }
            //控制命令返回
            if (map.containsKey("JNCommandReturn")) {
                JNCommandReturn commandReturn = (JNCommandReturn) map.get("JNCommandReturn");
                if (null != commandReturn) {
                    String message = JSON.toJSONString(commandReturn);
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1CommandReturn), message, 0);
                }
            }
            //锁焊机或者解锁焊机返回
            if (map.containsKey("JnLockMachineReturn")) {
                final JnLockMachineReturn jnLockMachineReturn = (JnLockMachineReturn) map.get("JnLockMachineReturn");
                if (null != jnLockMachineReturn) {
                    try {
                        //采集编号
                        final String gatherNo = jnLockMachineReturn.getGatherNo();
                        //控制命令：（18：锁焊机，19：解锁焊机）
                        final int command = jnLockMachineReturn.getCommand();
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
                                final Map<Integer, Integer> otcLockMap = CommonMap.OTC_LOCK_FAIL_RETRY_MAP.get(gatherNo);
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
            //总长度：24（解锁焊机指令）
            final String str = "007E0A010101" + command + gatherNo + "00007D";
            //判断该焊机通道是否打开、是否活跃、是否可写
            if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                channel.writeAndFlush(str).sync();
            }
        } catch (InterruptedException e) {
            log.error("锁焊机或者解锁焊机重试异常：", e);
        }
    }

    /**
     * 实时数据解析通过mq发前端
     *
     * @param str 需要解析的16进制字符串
     * @return 返回一个集合对象
     */
    public List<JNRtDataUI> jnRtDataUiAnalysis(String clientIp, String str) {
        try {
            if (CommonUtils.isNotEmpty(str) && str.length() == 282) {
                str = str.toUpperCase();
                if ("7E".equals(str.substring(0, 2)) && "22".equals(str.substring(10, 12)) && "7D".equals(str.substring(280, 282))) {
                    List<JNRtDataUI> rtData = new ArrayList<>();
                    JNRtDataUI data = new JNRtDataUI();
                    Map<String, TaskClaimIssue> otcTaskClaimMap = CommonMap.OTC_TASK_CLAIM_MAP;
                    //采集模块编号
                    data.setGatherNo(Integer.valueOf(str.substring(14, 18), 16).toString());
                    if (otcTaskClaimMap.size() > 0 && otcTaskClaimMap.containsKey(data.getGatherNo())) {
                        TaskClaimIssue taskClaimIssue = otcTaskClaimMap.get(data.getGatherNo());
                        if (null != taskClaimIssue) {
                            data.setWelderId(taskClaimIssue.getWelderId());
                            data.setWelderName(taskClaimIssue.getWelderName());
                            data.setWelderNo(taskClaimIssue.getWelderNo());
                            data.setWelderDeptId(taskClaimIssue.getWelderDeptId());
                            data.setTaskId(taskClaimIssue.getTaskId());
                            data.setTaskName(taskClaimIssue.getTaskName());
                            data.setTaskNo(taskClaimIssue.getTaskNo());
                            data.setMachineId(taskClaimIssue.getMachineId());
                            data.setMachineNo(taskClaimIssue.getMachineNo());
                            data.setMachineDeptId(taskClaimIssue.getMachineDeptId());
                            data.setWeldType(taskClaimIssue.getWeldType());
                        }
                    }
                    //当前系统时间 yyyy-MM-dd HH:mm:ss
                    String nowDateTime = DateTimeUtils.getNowDateTime();
                    for (int a = 0; a < 239; a += 80) {
                        String year = CommonUtils.hexToDecLengthJoint(str.substring(38 + a, 40 + a), 2);
                        String month = CommonUtils.hexToDecLengthJoint(str.substring(40 + a, 42 + a), 2);
                        String day = CommonUtils.hexToDecLengthJoint(str.substring(42 + a, 44 + a), 2);
                        String hour = CommonUtils.hexToDecLengthJoint(str.substring(44 + a, 46 + a), 2);
                        String minute = CommonUtils.hexToDecLengthJoint(str.substring(46 + a, 48 + a), 2);
                        String second = CommonUtils.hexToDecLengthJoint(str.substring(48 + a, 50 + a), 2);
                        String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        try {
                            String weldDateTime = LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME);
                            data.setWeldTime(weldDateTime);
                        } catch (Exception e) {
                            log.error("OTC1.0实时数据时间格式异常:{}", e.getMessage());
                            data.setWeldTime(nowDateTime);
                        }
                        data.setWeldIp(clientIp);
                        //电流
                        data.setElectricity(BigDecimal.valueOf(Integer.valueOf(str.substring(50 + a, 54 + a), 16)));
                        //电压
                        data.setVoltage(BigDecimal.valueOf(Integer.valueOf(str.substring(54 + a, 58 + a), 16))
                                .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));
                        data.setWireFeedRate(BigDecimal.valueOf(Integer.valueOf(str.substring(58 + a, 62 + a), 16))
                                .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//送丝速度
                        //给定电流(焊机面板上的值)
                        data.setPresetEle(BigDecimal.valueOf(Integer.valueOf(str.substring(62 + a, 66 + a), 16)));
                        //给定电压(焊机面板上的值)
                        data.setPresetVol(BigDecimal.valueOf(Integer.valueOf(str.substring(66 + a, 70 + a), 16)));
                        //焊机状态
                        data.setWeldStatus(Integer.valueOf(str.substring(78 + a, 80 + a), 16));
                        //焊接电流
                        data.setWeldElectricity(BigDecimal.valueOf(Integer.valueOf(str.substring(84 + a, 88 + a), 16)));
                        //焊接电压
                        data.setWeldVoltage(BigDecimal.valueOf(Integer.valueOf(str.substring(88 + a, 92 + a), 16)));
                        //焊接电流微调
                        data.setWeldEleAdjust(BigDecimal.valueOf(Integer.valueOf(str.substring(92 + a, 94 + a), 16)));
                        //焊接电压微调
                        data.setWeldVolAdjust(BigDecimal.valueOf(Integer.valueOf(str.substring(94 + a, 96 + a), 16)));
                        data.setChannelNo(Integer.valueOf(str.substring(94 + a, 96 + a), 16).toString());
                        rtData.add((JNRtDataUI) data.clone());
                    }
                    return rtData;
                }
            }
        } catch (Exception e) {
            log.error("OTC1.0实时数据解析发MQTT异常：", e);
        }
        return null;
    }

    /**
     * 实时数据1.0协议解析存数据库
     *
     * @param str 需要解析的16进制字符串
     * @return 返回一个集合对象
     */
    public List<JNRtDataDB> jnRtDataDbAnalysis(String str) {
        try {
            if (CommonUtils.isNotEmpty(str) && str.length() == 282) {
                str = str.toUpperCase();
                if ("7E".equals(str.substring(0, 2)) && "7D".equals(str.substring(280, 282))) {
                    List<JNRtDataDB> rtdata = new ArrayList<>();
                    //采集模块信息
                    List<GatherModel> gatherList = CommonList.getGatherList();
                    //焊机设备信息
                    List<WeldModel> weldList = CommonList.getWeldList();
                    //刷卡领取任务后进行数据绑定
                    Map<String, TaskClaimIssue> otcTaskClaimMap = CommonMap.OTC_TASK_CLAIM_MAP;
                    for (int a = 0; a < 239; a += 80) {
                        //判断OTC待机数据是否存储,如果不存储，则取出待机状态判断
                        if (!CommonFunction.isOtcStandbySave()) {
                            Integer otcStandby = Integer.valueOf(str.substring(78 + a, 80 + a), 16);
                            //焊接状态为0表示待机，则直接进入下一次循环
                            if (otcStandby == 0) {
                                continue;
                            }
                        }
                        JNRtDataDB data = new JNRtDataDB();
                        //焊机型号
                        data.setWeldModel(Integer.valueOf(str.substring(12, 14), 16));
                        //采集模块编号
                        data.setGatherNo(Integer.valueOf(str.substring(14, 18), 16).toString());
                        //焊工号
                        //data.setWelderNo(Integer.valueOf(str.substring(34, 38), 16).toString());
                        //当前日期：yyyy-MM-dd
                        String nowDate = DateTimeUtils.getNowDate();
                        //当前系统时间 yyyy-MM-dd HH:mm:ss
                        String nowDateTime = DateTimeUtils.getNowDateTime();
                        //创建时间
                        data.setCreateTime(nowDateTime);
                        //采集模块信息判断并赋值
                        if (CommonUtils.isNotEmpty(gatherList)) {
                            for (GatherModel gather : gatherList) {
                                if (Integer.valueOf(data.getGatherNo()).equals(Integer.valueOf(gather.getGatherNo()))) {
                                    data.setGatherId(gather.getId());
                                    data.setGatherDeptId(gather.getDeptId());
                                    break;
                                }
                            }
                        }
                        //焊工、任务、焊机等信息判断并赋值
                        if (otcTaskClaimMap.size() > 0 && otcTaskClaimMap.containsKey(data.getGatherNo())) {
                            TaskClaimIssue taskClaimIssue = otcTaskClaimMap.get(data.getGatherNo());
                            if (null != taskClaimIssue) {
                                data.setWelderId(taskClaimIssue.getWelderId());
                                data.setWelderNo(taskClaimIssue.getWelderNo());
                                data.setWelderName(taskClaimIssue.getWelderName());
                                data.setWelderDeptId(taskClaimIssue.getWelderDeptId());
                                data.setTaskId(taskClaimIssue.getTaskId());
                                data.setTaskName(taskClaimIssue.getTaskName());
                                data.setTaskNo(taskClaimIssue.getTaskNo());
                                data.setMachineId(taskClaimIssue.getMachineId());
                                data.setMachineNo(taskClaimIssue.getMachineNo());
                                data.setMachineDeptId(taskClaimIssue.getMachineDeptId());
                            }
                        } else {
                            //焊机信息查询并绑定（如果设备未刷卡，则存储数据库设备id）
                            if (CommonUtils.isNotEmpty(weldList) && CommonUtils.isNotEmpty(data.getGatherNo())) {
                                for (WeldModel weld : weldList) {
                                    //if (CommonUtils.isNotEmpty(weld.getGatherNo()) && Integer.valueOf(data.getGatherNo()).equals(Integer.valueOf(weld.getGatherNo()))) {
                                    final List<String> gatherNoList = Arrays.stream(weld.getGatherNo().split(",")).
                                            map(gatherNo -> CommonUtils.stringLengthJoint(gatherNo, 4)).collect(Collectors.toList());
                                    if (CommonUtils.isNotEmpty(weld.getGatherNo()) && gatherNoList.contains(CommonUtils.stringLengthJoint(data.getGatherNo(), 4))) {
                                        data.setMachineId(weld.getId());
                                        data.setMachineNo(weld.getMachineNo());
                                        data.setMachineDeptId(weld.getDeptId());
                                        break;
                                    }
                                }
                            }
                        }
                        String year = CommonUtils.hexToDecLengthJoint(str.substring(38 + a, 40 + a), 2);
                        String month = CommonUtils.hexToDecLengthJoint(str.substring(40 + a, 42 + a), 2);
                        String day = CommonUtils.hexToDecLengthJoint(str.substring(42 + a, 44 + a), 2);
                        String hour = CommonUtils.hexToDecLengthJoint(str.substring(44 + a, 46 + a), 2);
                        String minute = CommonUtils.hexToDecLengthJoint(str.substring(46 + a, 48 + a), 2);
                        String second = CommonUtils.hexToDecLengthJoint(str.substring(48 + a, 50 + a), 2);
                        String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        try {
                            String weldDate = LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.TODAY_DATE);
                            String weldDateTime = LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME);
                            //判断实时数据是否当天数据，true：保存，false；赋值系统时间
                            if (weldDate.equals(nowDate)) {
                                data.setWeldTime(weldDateTime);
                            } else {
                                //焊机时间
                                data.setWeldTime(nowDateTime);
                            }
                        } catch (Exception e) {
                            log.error("OTC1.0实时数据时间格式异常:{}", e.getMessage());
                            data.setWeldTime(nowDateTime);
                        }
                        data.setElectricity(BigDecimal.valueOf(Integer.valueOf(str.substring(50 + a, 54 + a), 16)));//电流
                        data.setVoltage(BigDecimal.valueOf(Integer.valueOf(str.substring(54 + a, 58 + a), 16))
                                .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));  //电压
                        data.setWireFeedRate(BigDecimal.valueOf(Integer.valueOf(str.substring(58 + a, 62 + a), 16))
                                .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//送丝速度
                        data.setPresetEle(BigDecimal.valueOf(Integer.valueOf(str.substring(62 + a, 66 + a), 16)));//预置(给定)电流
                        data.setPresetVol(BigDecimal.valueOf(Integer.valueOf(str.substring(66 + a, 70 + a), 16))
                                .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//预置(给定)电压
                        data.setWeldedJunctionNo(Integer.valueOf(str.substring(70 + a, 78 + a), 16).toString());//焊口号
                        data.setWeldStatus(Integer.valueOf(str.substring(78 + a, 80 + a), 16));//焊机状态
                        data.setWireDiameter(BigDecimal.valueOf(Integer.valueOf(str.substring(80 + a, 82 + a), 16)));//焊丝直径
                        data.setWireMaterialsGases(BigDecimal.valueOf(Integer.valueOf(str.substring(82 + a, 84 + a), 16)));//焊丝材料和保护气体
                        data.setWeldElectricity(BigDecimal.valueOf(Integer.valueOf(str.substring(84 + a, 88 + a), 16)));//焊接电流（中位数）
                        data.setWeldVoltage(BigDecimal.valueOf(Integer.valueOf(str.substring(88 + a, 92 + a), 16))
                                .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//焊接电压（中位数）
                        data.setWeldEleAdjust(BigDecimal.valueOf(Integer.valueOf(str.substring(92 + a, 94 + a), 16)));//焊接电流微调
                        data.setWeldVolAdjust(BigDecimal.valueOf(Integer.valueOf(str.substring(94 + a, 96 + a), 16))
                                .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//焊接电压微调
                        data.setChannelNo(Integer.valueOf(str.substring(100 + a, 102 + a), 16).toString());//当前通道号
                        data.setAlarmsEleMax(BigDecimal.valueOf(Integer.valueOf(str.substring(102 + a, 106 + a), 16)));//报警电流上限
                        data.setAlarmsVolMax(BigDecimal.valueOf(Integer.valueOf(str.substring(106 + a, 110 + a), 16))
                                .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//报警电压上限
                        data.setAlarmsEleMin(BigDecimal.valueOf(Integer.valueOf(str.substring(110 + a, 114 + a), 16)));//报警电流下限
                        data.setAlarmsVolMin(BigDecimal.valueOf(Integer.valueOf(str.substring(114 + a, 118 + a), 16))
                                .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//报警电压下限
                        //待机数据不存储，则针对起弧、收弧各存储一条待机数据
                        if (!CommonFunction.isOtcStandbySave()) {
                            //起弧（增加一条待机数据）
                            if (data.getWeldStatus() == 7) {
                                JNRtDataDB jnRtDataDb = (JNRtDataDB) data.clone();
                                jnRtDataDb.setWeldStatus(0);
                                final LocalDateTime parse = LocalDateTime.parse(jnRtDataDb.getWeldTime(), DateTimeUtils.DEFAULT_DATETIME);
                                //减去1秒
                                final String weldTime = parse.minusSeconds(1).format(DateTimeUtils.DEFAULT_DATETIME);
                                jnRtDataDb.setWeldTime(weldTime);
                                jnRtDataDb.setElectricity(BigDecimal.ZERO);
                                jnRtDataDb.setVoltage(BigDecimal.ZERO);
                                rtdata.add(jnRtDataDb);
                            }
                            //收弧（增加一条待机数据）
                            else if (data.getWeldStatus() == 5) {
                                JNRtDataDB jnRtDataDb = (JNRtDataDB) data.clone();
                                jnRtDataDb.setWeldStatus(0);
                                final LocalDateTime parse = LocalDateTime.parse(jnRtDataDb.getWeldTime(), DateTimeUtils.DEFAULT_DATETIME);
                                //加上1秒
                                final String weldTime = parse.plusSeconds(1).format(DateTimeUtils.DEFAULT_DATETIME);
                                jnRtDataDb.setWeldTime(weldTime);
                                jnRtDataDb.setElectricity(BigDecimal.ZERO);
                                jnRtDataDb.setVoltage(BigDecimal.ZERO);
                                rtdata.add(jnRtDataDb);
                            }
                        }
                        rtdata.add(data);
                    }
                    return rtdata;
                }
            }
        } catch (Exception e) {
            log.error("OTC1.0实时数据解析存DB异常：", e);
        }
        return null;
    }

    /**
     * 下发规范字符串拼接
     *
     * @param model 下发规范实体类
     * @return 返回解析完成的16进制字符串
     */
    public static String jnIssueProtocol(JNProcessIssue model) {
        if (null != model) {
            String head = JNProcessIssue.Flag.HEAD.value;
            String order = JNProcessIssue.Flag.ORDER.value;
            String foot = JNProcessIssue.Flag.FOOT.value;
            String gatherNo = CommonUtils.lengthJoint(model.getGatherNo(), 4);//采集编号：0001
            String channelNo = CommonUtils.lengthJoint(model.getChannelNo(), 2);//通道号：01
            String spotWeldTime = CommonUtils.lengthJoint(model.getSpotWeldTime().intValue(), 4);//点焊时间：30
            String preflowTime = CommonUtils.lengthJoint(model.getPreflowTime().intValue(), 4);//提前送气时间：1
            String initialEle = CommonUtils.lengthJoint(model.getInitialEle().intValue(), 4);//初期电流:100
            String initialVol = CommonUtils.lengthJoint(model.getInitialVol().intValue(), 4);//初期电压:190
            String initialVolUnitary = CommonUtils.lengthJoint(model.getInitialVolUnitary().intValue(), 4);//初期电压一元:0
            String weldElectricity = CommonUtils.lengthJoint(model.getWeldElectricity().intValue(), 4);//焊接电流:100
            String weldVoltage = CommonUtils.lengthJoint(model.getWeldVoltage().intValue(), 4);//焊接电压:190
            String weldVoltageUnitary = CommonUtils.lengthJoint(model.getWeldVoltageUnitary().intValue(), 4);//焊接电压一元:0
            String extinguishArcEle = CommonUtils.lengthJoint(model.getExtinguishArcEle().intValue(), 4);//收弧电流:100
            String extinguishArcVol = CommonUtils.lengthJoint(model.getExtinguishArcVol().intValue(), 4);//收弧电压:190
            String extinguishArcVolUnitary = CommonUtils.lengthJoint(model.getExtinguishArcVolUnitary().intValue(), 4);//收弧电压一元:0
            String hysteresisAspirated = CommonUtils.lengthJoint(model.getHysteresisAspirated().intValue(), 4);//滞后送气:1
            String arcPeculiarity = CommonUtils.lengthJoint(model.getArcPeculiarity().intValue(), 4);//电弧特性:0
            String gases = CommonUtils.lengthJoint(model.getGases().intValue(), 2);//气体:0
            String wireDiameter = CommonUtils.lengthJoint(model.getWireDiameter().intValue(), 2);//焊丝直径:12
            String wireMaterials = CommonUtils.lengthJoint(model.getWireMaterials(), 2);//焊丝材料:0
            String weldProcess = CommonUtils.lengthJoint(model.getWeldProcess(), 2);//焊接过程:0
            String controlInfo = CommonUtils.lengthJoint(model.getControlInfo(), 4);//控制信息:229
            String weldEleAdjust = CommonUtils.lengthJoint(model.getWeldEleAdjust().intValue(), 2);//焊接电流微调:0
            String weldVolAdjust = CommonUtils.lengthJoint(model.getWeldVolAdjust().intValue(), 2);//焊接电压微调:0
            String extinguishArcEleAdjust = CommonUtils.lengthJoint(model.getExtinguishArcEleAdjust().intValue(), 2);//收弧电流微调:0
            String extinguishArcVolAdjust = CommonUtils.lengthJoint(model.getExtinguishArcVolAdjust().intValue(), 2);//收弧电压微调:0
            String alarmsElectricityMax = CommonUtils.lengthJoint(model.getAlarmsElectricityMax().intValue(), 4);//报警电流上限:0
            String alarmsElectricityMin = CommonUtils.lengthJoint(model.getAlarmsElectricityMin().intValue(), 4);//报警电流下限:0
            String alarmsVoltageMax = CommonUtils.lengthJoint(model.getAlarmsVoltageMax().intValue(), 4);//报警电压上限:0
            String alarmsVoltageMin = CommonUtils.lengthJoint(model.getAlarmsVoltageMin().intValue(), 4);//报警电压下限:0
            String str = head + order + gatherNo + channelNo + spotWeldTime + preflowTime + initialEle + initialVol + initialVolUnitary + weldElectricity +
                    weldVoltage + weldVoltageUnitary + extinguishArcEle + extinguishArcVol + extinguishArcVolUnitary + hysteresisAspirated + arcPeculiarity +
                    gases + wireDiameter + wireMaterials + weldProcess + controlInfo + weldEleAdjust + weldVolAdjust + extinguishArcEleAdjust + extinguishArcVolAdjust +
                    alarmsElectricityMax + alarmsElectricityMin + alarmsVoltageMax + alarmsVoltageMin + foot;
            str = str.toUpperCase(); //字母全部转为大写
            return str;
        }
        return null;
    }

    /**
     * 下发返回协议解析
     *
     * @param str 16进制字符串
     * @return 下发规范返回类
     */
    public JNProcessIssueReturn jnIssueReturnAnalysis(String str) {
        if (CommonUtils.isNotEmpty(str) && str.length() == 24) {
            if ("7E".equals(str.substring(0, 2)) && "52".equals(str.substring(10, 12)) && "7D".equals(str.substring(22, 24))) {
                JNProcessIssueReturn issueReturn = new JNProcessIssueReturn();
                issueReturn.setGatherNo(Integer.valueOf(str.substring(12, 16), 16).toString());
                issueReturn.setChannelNo(Integer.valueOf(str.substring(16, 18), 16).toString());
                issueReturn.setFlag(Integer.valueOf(str.substring(18, 20), 16));
                return issueReturn;
            }
        }
        return null;
    }

    /**
     * 索取规范字符串拼接
     *
     * @param claimModel 索取规范实体类
     * @return 返回16进制字符串
     */
    public static String jnClaimProtocol(JNProcessClaim claimModel) {
        if (null != claimModel) {
            String head = JNProcessClaim.Flag.HEAD.value;
            String foot = JNProcessClaim.Flag.FOOT.value;
            String order = JNProcessClaim.Flag.ORDER.value;
            //采集编号：0001
            String gatherNo = CommonUtils.lengthJoint(claimModel.getGatherNo(), 4);
            //通道号：01
            String channelNo = CommonUtils.lengthJoint(claimModel.getChannelNo(), 2);
            String str = head + order + gatherNo + channelNo + foot;
            str = str.toUpperCase();
            return str;
        }
        return null;
    }

    /**
     * 索取返回协议解析
     *
     * @param str 16进制字符串
     * @return 索取返回实体类
     */
    public JNProcessClaimReturn jnClaimReturnAnalysis(String str) {
        if (CommonUtils.isNotEmpty(str) && str.length() == 112) {
            if ("7E".equals(str.substring(0, 2)) && "56".equals(str.substring(10, 12)) && "7D".equals(str.substring(110, 112))) {
                JNProcessClaimReturn claim = new JNProcessClaimReturn();
                claim.setGatherNo(Integer.valueOf(str.substring(12, 16), 16).toString()); //采集模块编号
                claim.setStatus(Integer.valueOf(str.substring(16, 18), 16));
                claim.setChannelNo(Integer.valueOf(str.substring(18, 20), 16).toString());
                claim.setSpotWeldTime(BigDecimal.valueOf(Long.valueOf(str.substring(20, 24), 16))
                        .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//点焊时间
                claim.setPreflowTime(BigDecimal.valueOf(Long.valueOf(str.substring(24, 28), 16))
                        .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//提前送气时间
                claim.setInitialEle(BigDecimal.valueOf(Long.valueOf(str.substring(28, 32), 16)));//初期电流
                claim.setInitialVol(BigDecimal.valueOf(Long.valueOf(str.substring(32, 36), 16))
                        .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//初期电压
                claim.setInitialVolUnitary(BigDecimal.valueOf(Long.valueOf(str.substring(36, 40), 16)));//初期电压一元
                claim.setWeldElectricity(BigDecimal.valueOf(Long.valueOf(str.substring(40, 44), 16)));//焊接电流
                claim.setWeldVoltage(BigDecimal.valueOf(Long.valueOf(str.substring(44, 48), 16))
                        .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//焊接电压
                claim.setWeldVoltageUnitary(BigDecimal.valueOf(Long.valueOf(str.substring(48, 52), 16)));//焊接电压一元
                claim.setExtinguishArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(52, 56), 16)));//收弧电流
                claim.setExtinguishArcVol(BigDecimal.valueOf(Long.valueOf(str.substring(56, 60), 16))
                        .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//收弧电压
                claim.setExtinguishArcVolUnitary(BigDecimal.valueOf(Long.valueOf(str.substring(60, 64), 16)));//收弧电压一元
                claim.setHysteresisAspirated(BigDecimal.valueOf(Long.valueOf(str.substring(64, 68), 16))
                        .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//滞后送气
                claim.setArcPeculiarity(BigDecimal.valueOf(Long.valueOf(str.substring(68, 72), 16)));//电弧特性
                claim.setGases(BigDecimal.valueOf(Long.valueOf(str.substring(72, 74), 16)));//气体
                claim.setWireDiameter(BigDecimal.valueOf(Long.valueOf(str.substring(74, 76), 16)));//焊丝直径
                claim.setWireMaterials(Integer.valueOf(str.substring(76, 78), 16).toString());//焊丝材料
                claim.setWeldProcess(Integer.valueOf(str.substring(78, 80), 16).toString());//焊接过程
                claim.setControlInfo(Integer.valueOf(str.substring(80, 84), 16).toString());//控制信息
                claim.setWeldEleAdjust(BigDecimal.valueOf(Long.valueOf(str.substring(84, 86), 16)));//焊接电流微调
                claim.setWeldVolAdjust(BigDecimal.valueOf(Long.valueOf(str.substring(86, 88), 16))
                        .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//焊接电压微调
                claim.setExtinguishArcEleAdjust(BigDecimal.valueOf(Long.valueOf(str.substring(88, 90), 16)));//收弧电流微调
                claim.setExtinguishArcVolAdjust(BigDecimal.valueOf(Long.valueOf(str.substring(90, 92), 16))
                        .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//收弧电压微调
                claim.setAlarmsElectricityMax(BigDecimal.valueOf(Long.valueOf(str.substring(92, 96), 16)));//报警电流上限
                claim.setAlarmsVoltageMax(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16))
                        .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//报警电压上限
                claim.setAlarmsElectricityMin(BigDecimal.valueOf(Long.valueOf(str.substring(100, 104), 16)));//报警电流下限
                claim.setAlarmsVoltageMin(BigDecimal.valueOf(Long.valueOf(str.substring(104, 108), 16))
                        .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));//报警电压下限
                return claim;
            }
        }
        return null;
    }

    /**
     * 密码下发字符串拼接
     *
     * @param jnPasswordIssue 密码下发实体类
     * @return 16进制字符串
     */
    public static String jnPasswordProtocol(JNPasswordIssue jnPasswordIssue) {
        if (null != jnPasswordIssue) {
            String head = JNPasswordIssue.Flag.HEAD.value;
            String order = JNPasswordIssue.Flag.ORDER.value;
            String foot = JNPasswordIssue.Flag.FOOT.value;
            String gatherNo = CommonUtils.lengthJoint(jnPasswordIssue.getGatherNo(), 4);
            String password = CommonUtils.lengthJoint(jnPasswordIssue.getPassword(), 4);
            String str = head + order + gatherNo + password + foot;
            str = str.toUpperCase();
            return str;
        }
        return null;
    }

    /**
     * 控制命令下发字符串拼接
     *
     * @param jnCommandIssue 控制命令下发实体类
     * @return 16进制字符串
     */
    public static String jnCommandProtocol(JNCommandIssue jnCommandIssue) {
        if (null != jnCommandIssue) {
            String head = JNCommandIssue.Flag.HEAD.value;
            String order = JNCommandIssue.Flag.ORDER.value;
            String foot = JNCommandIssue.Flag.FOOT.value;
            String gatherNo = CommonUtils.lengthJoint(jnCommandIssue.getGatherNo(), 4);
            String command = CommonUtils.lengthJoint(jnCommandIssue.getCommand(), 2);
            String str = head + order + gatherNo + command + foot;
            str = str.toUpperCase();
            return str;
        }
        return null;
    }

    /**
     * 密码返回协议解析
     *
     * @param str 16进制字符串
     * @return 密码返回实体类
     */
    public JNPasswordReturn jnPasswordReturnAnalysis(String str) {
        if ("7E".equals(str.substring(0, 2)) && "53".equals(str.substring(10, 12)) && "7D".equals(str.substring(20, 22))) {
            JNPasswordReturn passwordReturn = new JNPasswordReturn();
            passwordReturn.setGatherNo(Integer.valueOf(str.substring(12, 16), 16).toString());
            passwordReturn.setFlag(Integer.valueOf(str.substring(16, 18)));
            return passwordReturn;
        }
        return null;
    }

    /**
     * 控制命令返回协议解析
     *
     * @param str 16进制字符串
     * @return 控制命令返回实体类
     */
    public JNCommandReturn jnCommandReturnAnalysis(String str) {
        if ("7E".equals(str.substring(0, 2)) && "54".equals(str.substring(10, 12)) && "7D".equals(str.substring(20, 22))) {
            JNCommandReturn commandReturn = new JNCommandReturn();
            commandReturn.setGatherNo(Integer.valueOf(str.substring(12, 16), 16).toString());
            commandReturn.setFlag(Integer.valueOf(str.substring(16, 18)));
            return commandReturn;
        }
        return null;
    }

    /**
     * 锁焊机和解锁焊机指令返回协议解析
     *
     * @param str 16进制字符串
     * @return 锁焊机和解锁焊机指令返回
     */
    public JnLockMachineReturn jnLockMachineReturnAnalysis(String str) {
        if (CommonUtils.isNotEmpty(str) && str.length() == 22) {
            try {
                JnLockMachineReturn jnLockMachineReturn = new JnLockMachineReturn();
                jnLockMachineReturn.setCommand(Integer.parseInt(str.substring(10, 12)));
                jnLockMachineReturn.setGatherNo(Integer.valueOf(str.substring(12, 16), 16).toString());
                jnLockMachineReturn.setResult(Integer.valueOf(str.substring(16, 18), 16));
                return jnLockMachineReturn;
            } catch (Exception e) {
                log.error("锁焊机和解锁焊机指令返回协议解析异常：", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 根据采集编号查询焊机ID
     *
     * @param gatherNo 采集编号
     * @return 焊机ID
     */
    public static BigInteger getMachineIdByGatherNo(String gatherNo) {
        try {
            List<WeldModel> weldList = CommonList.getWeldList();
            if (CommonUtils.isNotEmpty(weldList) && CommonUtils.isNotEmpty(gatherNo)) {
                for (WeldModel weld : weldList) {
                    if (CommonUtils.isNotEmpty(weld.getGatherNo())) {
                        final List<String> gatherNoList = Arrays.stream(weld.getGatherNo().split(",")).
                                map(string -> CommonUtils.stringLengthJoint(string, 4)).collect(Collectors.toList());
                        if (gatherNoList.contains(CommonUtils.stringLengthJoint(gatherNo, 4))) {
                            return weld.getId();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("根据采集编号查询焊机ID异常：", e);
        }
        return BigInteger.ZERO;
    }

    /**
     * 江南OTC设备关机数据处理
     */
    public void jnWeldOffDataManage(ChannelHandlerContext ctx, String clientIp) {
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
            //关机数据通过线程池发送到mq
            CommonThreadPool.THREAD_POOL_EXECUTOR.execute(() -> {
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
                String dataArray = JSONArray.toJSONString(dataList);
                EmqMqttClient.publishMessage(GainTopicName.getMqttUpTopicName(UpTopicEnum.OtcV1RtData), dataArray, 0);
            });
            CommonMap.OTC_GATHER_NO_CTX_MAP.remove(gatherNo);
            CommonMap.OTC_CTX_GATHER_NO_MAP.remove(ctx);
        }
    }

}
