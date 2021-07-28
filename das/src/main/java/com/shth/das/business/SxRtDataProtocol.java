package com.shth.das.business;

import com.alibaba.fastjson.JSON;
import com.shth.das.common.CommonDbData;
import com.shth.das.common.SxVerificationCode;
import com.shth.das.common.UpTopicEnum;
import com.shth.das.mqtt.EmqMqttClient;
import com.shth.das.netty.NettyServerHandler;
import com.shth.das.pojo.db.SxWeldModel;
import com.shth.das.pojo.db.TaskClaimIssue;
import com.shth.das.pojo.db.WeldOnOffTime;
import com.shth.das.pojo.jnsx.*;
import com.shth.das.sys.weldmesdb.service.SxWeldService;
import com.shth.das.sys.weldmesdb.service.WeldOnOffTimeService;
import com.shth.das.util.BeanContext;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"ALL"})
@Slf4j
public class SxRtDataProtocol {

    /**
     * 松下实时数据累积后插入数据库
     */
    private static final List<SxRtDataDb> SX_RT_DATA_LIST = new ArrayList<>();

    /**
     * 松下实时数据解码处理（上行）
     */
    public Map<String, Object> sxRtDataDecoderManage(ChannelHandlerContext ctx, String str) {
        //客户端IP（焊机IP）
        String clientIp = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
        Map<String, Object> map = new HashMap<>(16);
        try {
            //松下焊机GL5/FR2/AT第一次验证（下行）
            if (str.length() == 42 && "4E455430".equals(str.substring(0, 8))) {
                ctx.channel().writeAndFlush(SxVerificationCode.SX_FIRST_VERIFICATION).sync();
            }
            //松下焊机GL5/FR2/AT第二次验证（下行）
            if (str.length() == 128 && "4C4A5348".equals(str.substring(0, 8))) {
                ctx.channel().writeAndFlush(SxVerificationCode.SX_SECOND_VERIFICATION).sync();
            }
            //松下焊机GL5软硬件参数
            if (str.length() == 180 && "FE5AA5005A".equals(str.substring(0, 10))) {
                SxWeldModel sxWeldModel = sxWeldAnalysis(clientIp, str);
                map.put("SxWeldModel", sxWeldModel);
            }
            //松下焊机GL5实时数据
            if (str.length() == 206 && "FE5AA50067".equals(str.substring(0, 10))) {
                SxRtDataUi sxRtDataUi = sxRtDataUiAnalysis(clientIp, str);
                SxRtDataDb sxRtDataDb = sxRtDataDbAnalysis(clientIp, str);
                map.put("SxRtDataUI", sxRtDataUi);
                map.put("SxRtDataDb", sxRtDataDb);
            }
            //松下焊机GL5系列CO2状态信息
            if (str.length() == 246 && "FE5AA5007B".equals(str.substring(0, 10))) {
                SxStatusDataUI sxStatusDataUi = sxStatusDataUiAnalysis(clientIp, str);
                map.put("SxStatusDataUI", sxStatusDataUi);
            }
            if (str.length() == 106) {
                //松下GL5工艺下发回复/索取返回
                if ("1201".equals(str.substring(40, 44))) {
                    //读写标志：0：主动上传；1：读取；2：设置；3：删除
                    if ("1".equals(Integer.valueOf(str.substring(70, 72), 16).toString()) && "FE5AA50035".equals(str.substring(0, 10))) {
                        //松下工艺索取返回（无数据）
                        SxProcessClaimReturn sxProcessClaimReturn = sxProcessClaimReturnAnalysis(clientIp, str);
                        map.put("SxProcessClaimReturn", sxProcessClaimReturn);
                    }
                    if ("2".equals(Integer.valueOf(str.substring(70, 72), 16).toString()) && "FE5AA50035".equals(str.substring(0, 10))) {
                        //松下工艺下发回复
                        SxProcessReturn sxProcessReturn = sxProcessReturnAnalysis(clientIp, str);
                        map.put("SxProcessReturn", sxProcessReturn);
                    }
                    if ("3".equals(Integer.valueOf(str.substring(70, 72), 16).toString()) && "FE5AA50028".equals(str.substring(0, 10))) {
                        //松下工艺删除回复
                        SxProcessDeleteReturn sxProcessDeleteReturn = sxProcessDeleteReturnAnalysis(clientIp, str);
                        map.put("SxProcessDeleteReturn", sxProcessDeleteReturn);
                    }
                }
                //松下GL5焊接规范通道设定回复/读取回复
                if ("1202".equals(str.substring(40, 44)) && "FE5AA50035".equals(str.substring(0, 10))) {
                    SxWeldChannelSetReturn weldChannelSetReturn = sxWeldChannelSetReturnAnalysis(clientIp, str);
                    map.put("SxWeldChannelSetReturn", weldChannelSetReturn);
                }
            }
            //松下GL5系列CO2工艺索取返回
            if (str.length() == 406 && "FE5AA500CB".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                SxCO2ProcessClaimReturn claimReturn = sxCO2ProcessClaimReturnAnalysis(clientIp, str);
                map.put("SxCO2ProcessClaimReturn", claimReturn);
            }
            //松下GL5系列TIG工艺索取返回
            if (str.length() == 446 && "FE5AA500CB".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                SxTIGProcessClaimReturn sxTIGProcessClaimReturn = sxTIGProcessClaimReturnAnalysis(clientIp, str);
                map.put("SxTIGProcessClaimReturn", sxTIGProcessClaimReturn);
            }
            //松下FR2系列CO2实时数据
            if (str.length() == 126 && "FE5AA5003F".equals(str.substring(0, 10))) {
                SxRtDataDb sxRtDataDb = fr2Co2RtDataDbAnalysis(clientIp, str);
                SxRtDataUi sxRtDataUi = fr2Co2RtDataUiAnalysis(clientIp, str);
                map.put("SxRtDataDb", sxRtDataDb);
                map.put("SxRtDataUi", sxRtDataUi);
            }
            //松下FR2系列TIG实时数据
            if (str.length() == 118 && "FE5AA5003B".equals(str.substring(0, 10))) {
                SxRtDataDb sxRtDataDb = fr2TigRtDataDbAnalysis(clientIp, str);
                SxRtDataUi sxRtDataUi = fr2TigRtDataUiAnalysis(clientIp, str);
                map.put("SxRtDataDb", sxRtDataDb);
                map.put("SxRtDataUi", sxRtDataUi);
            }
            //松下FR2系列CO2/TIG状态信息
            if (str.length() == 156 && "FE5AA5004E".equals(str.substring(0, 10))) {
                //CO2焊机
                if ("0102".equals(str.substring(40, 44)) && 1 == Integer.valueOf(str.substring(68, 70), 16)) {
                    SxStatusDataUI sxStatusDataUi = fr2Co2StatusUiAnalysis(clientIp, str);
                    map.put("SxStatusDataUI", sxStatusDataUi);
                }
                //TIG焊机
                if ("0102".equals(str.substring(40, 44)) && 4 == Integer.valueOf(str.substring(68, 70), 16)) {
                    SxStatusDataUI sxStatusDataUi = fr2TigStatusUiAnalysis(clientIp, str);
                    map.put("SxStatusDataUI", sxStatusDataUi);
                }
            }
            //松下FR2系列通道参数查询（无参数）、下载、删除回复
            if (str.length() == 52 && "FE5AA5001A".equals(str.substring(0, 10))) {
                SxChannelParamReply sxChannelParamReply = sxChannelParamReplyAnalysis(clientIp, str);
                map.put("SxChannelParamReply", sxChannelParamReply);
            }
            //松下FR2系列通道参数查询（有参数）
            if (str.length() == 218 && "FE5AA5006E".equals(str.substring(0, 10))) {
                SxChannelParamReplyHave sxChannelParamReplyHave = sxChannelParamReplyHaveAnalysis(clientIp, str);
                map.put("SxChannelParamReplyHave", sxChannelParamReplyHave);
            }
            //松下AT3系列查询回复（有参数）
            if (str.length() == 92 && "FE5AA5002E".equals(str.substring(0, 10))) {
                At3ParamQueryReturn at3ParamQueryReturn = at3ParamQueryReturnAnalysis(clientIp, str);
                map.put("At3ParamQueryReturn", at3ParamQueryReturn);
            }
        } catch (Exception e) {
            log.error("sxRtDataDecoderManage Exception:" + e.getMessage());
        }
        return map;
    }

    /**
     * 松下设备信息绑定到IP地址
     *
     * @param sxWeldModel 焊机信息实体类
     */
    public static void sxWeldDataBinding(SxWeldModel sxWeldModel) {
        if (null != sxWeldModel) {
            String weldIp = sxWeldModel.getWeldIp();
            if (CommonUtils.isNotEmpty(weldIp)) {
                if (!NettyServerHandler.SX_CLIENT_IP_BIND_WELD_INFO.containsKey(weldIp)) {
                    NettyServerHandler.SX_CLIENT_IP_BIND_WELD_INFO.put(weldIp, sxWeldModel);
                }
            }
            //调用接口，数据存入数据库
            SxWeldService sxWeldService = BeanContext.getBean(SxWeldService.class);
            sxWeldService.insertSxWeld(sxWeldModel);
        }
    }

    /**
     * 松下设备实时数据处理（上行）
     */
    public void sxRtDataManage(Object msg) {
        Map<String, Object> map = (Map<String, Object>) msg;
        if (map.size() > 0) {
            //松下焊机GL5软硬件参数存数据库
            if (map.containsKey("SxWeldModel")) {
                SxWeldModel sxWeldModel = (SxWeldModel) map.get("SxWeldModel");
                //松下参数绑定
                sxWeldDataBinding(sxWeldModel);
            }
            //松下焊机GL5实时数据发送到mq
            if (map.containsKey("SxRtDataUi")) {
                SxRtDataUi sxRtDataUi = (SxRtDataUi) map.get("SxRtDataUi");
                //焊机IP地址
                String clientIp = sxRtDataUi.getWeldIp();
                if (CommonUtils.isNotEmpty(clientIp) && NettyServerHandler.SX_CLIENT_IP_BIND_WELD_INFO.containsKey(clientIp)) {
                    SxWeldModel sxWeldModel = NettyServerHandler.SX_CLIENT_IP_BIND_WELD_INFO.get(clientIp);
                    if (null != sxWeldModel) {
                        //设备编码
                        sxRtDataUi.setWeldCode(sxWeldModel.getWeldCode());
                        //设备机型
                        sxRtDataUi.setWeldModel(sxWeldModel.getWeldModel());
                    }
                }
                //实体类转JSON字符串
                String message = JSON.toJSONString(sxRtDataUi);
                CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(UpTopicEnum.sxrtdata.name(), message, 0);
                });
            }
            //松下焊机GL5实时数据存数据库
            if (map.containsKey("SxRtDataDb")) {
                SxRtDataDb sxRtDataDb = (SxRtDataDb) map.get("SxRtDataDb");
                //焊机IP地址
                String clientIp = sxRtDataDb.getWeldIp();
                if (CommonUtils.isNotEmpty(clientIp) && NettyServerHandler.SX_CLIENT_IP_BIND_WELD_INFO.containsKey(clientIp)) {
                    SxWeldModel sxWeldModel = NettyServerHandler.SX_CLIENT_IP_BIND_WELD_INFO.get(clientIp);
                    if (null != sxWeldModel) {
                        sxRtDataDb.setWeldCode(sxWeldModel.getWeldCode());
                        sxRtDataDb.setWeldModel(sxWeldModel.getWeldModel());
                    }
                }
                if (null != sxRtDataDb) {
                    //添加到松下阻塞队列（通过定时任务定时存储）
                    CommonDbData.SX_LINKED_BLOCKING_QUEUE.offer(sxRtDataDb);
                }
            }
            //松下焊机GL5状态信息发送到mq
            if (map.containsKey("SxStatusDataUI")) {
                SxStatusDataUI sxStatusDataUi = (SxStatusDataUI) map.get("SxStatusDataUI");
                //实体类转JSON字符串
                String message = JSON.toJSONString(sxStatusDataUi);
                CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(UpTopicEnum.sxStatusData.name(), message, 0);
                });
            }
            //松下工艺下发回复发送到mq
            if (map.containsKey("SxProcessReturn")) {
                SxProcessReturn sxProcessReturn = (SxProcessReturn) map.get("SxProcessReturn");
                //实体类转JSON字符串
                String message = JSON.toJSONString(sxProcessReturn);
                CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(UpTopicEnum.sxProcessReturn.name(), message, 0);
                });
            }
            //松下工艺索取返回(无数据)
            if (map.containsKey("SxProcessClaimReturn")) {
                SxProcessClaimReturn sxProcessClaimReturn = (SxProcessClaimReturn) map.get("SxProcessClaimReturn");
                //实体类转JSON字符串
                String message = JSON.toJSONString(sxProcessClaimReturn);
                CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(UpTopicEnum.sxProcessClaimReturn.name(), message, 0);
                });
            }
            //松下工艺删除返回
            if (map.containsKey("SxProcessDeleteReturn")) {
                SxProcessDeleteReturn sxProcessDeleteReturn = (SxProcessDeleteReturn) map.get("SxProcessDeleteReturn");
                //实体类转JSON字符串
                String message = JSON.toJSONString(sxProcessDeleteReturn);
                CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(UpTopicEnum.sxProcessDeleteReturn.name(), message, 0);
                });
            }
            //松下CO2工艺索取返回
            if (map.containsKey("SxCO2ProcessClaimReturn")) {
                SxCO2ProcessClaimReturn sxCO2ProcessClaimReturn = (SxCO2ProcessClaimReturn) map.get("SxCO2ProcessClaimReturn");
                //实体类转JSON字符串
                String message = JSON.toJSONString(sxCO2ProcessClaimReturn);
                CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(UpTopicEnum.sxCO2ProcessClaimReturn.name(), message, 0);
                });
            }
            //松下TIG工艺索取返回
            if (map.containsKey("SxTIGProcessClaimReturn")) {
                SxTIGProcessClaimReturn sxTIGProcessClaimReturn = (SxTIGProcessClaimReturn) map.get("SxTIGProcessClaimReturn");
                //实体类转JSON字符串
                String message = JSON.toJSONString(sxTIGProcessClaimReturn);
                CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(UpTopicEnum.sxTIGProcessClaimReturn.name(), message, 0);
                });
            }
            //松下焊机通道设定回复/读取回复发mq
            if (map.containsKey("SxWeldChannelSetReturn")) {
                SxWeldChannelSetReturn sxWeldChannelSetReturn = (SxWeldChannelSetReturn) map.get("SxWeldChannelSetReturn");
                //实体类转JSON字符串
                String message = JSON.toJSONString(sxWeldChannelSetReturn);
                CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(UpTopicEnum.sxWeldChannelSetReturn.name(), message, 0);
                });
            }
            //松下FR2通道参数查询回复（无参数）、下载回复、删除回复
            if (map.containsKey("SxChannelParamReply")) {
                SxChannelParamReply sxChannelParamReply = (SxChannelParamReply) map.get("SxChannelParamReply");
                //实体类转JSON字符串
                String message = JSON.toJSONString(sxChannelParamReply);
                CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(UpTopicEnum.sxChannelParamReply.name(), message, 0);
                });
            }
            //松下FR2通道参数查询回复（有参数）
            if (map.containsKey("SxChannelParamReplyHave")) {
                SxChannelParamReplyHave sxChannelParamReplyHave = (SxChannelParamReplyHave) map.get("SxChannelParamReplyHave");
                //实体类转JSON字符串
                String message = JSON.toJSONString(sxChannelParamReplyHave);
                CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(UpTopicEnum.sxChannelParamReplyHave.name(), message, 0);
                });
            }
            //松下AT3系列查询回复（有参数）
            if (map.containsKey("At3ParamQueryReturn")) {
                At3ParamQueryReturn at3ParamQueryReturn = (At3ParamQueryReturn) map.get("At3ParamQueryReturn");
                //实体类转JSON字符串
                String message = JSON.toJSONString(at3ParamQueryReturn);
                CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(UpTopicEnum.sxAt3ParamQueryReturn.name(), message, 0);
                });
            }
        }
    }

    /**
     * 松下GL5软硬件参数解析
     */
    public SxWeldModel sxWeldAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 180 && "FE5AA5005A".equals(str.substring(0, 10))) {
                SxWeldModel sxWeldModel = new SxWeldModel();
                sxWeldModel.setWeldIp(clientIp);//客户端IP地址
                sxWeldModel.setWeldModel(CommonUtils.convertHexToString(str.substring(44, 62))); //设备机型
                sxWeldModel.setPowerSupply(Integer.valueOf(str.substring(62, 64), 16));//电源类型
                sxWeldModel.setWireFeederModel(Integer.valueOf(str.substring(64, 66), 16));//送丝机类型
                sxWeldModel.setWeldKind(Integer.valueOf(str.substring(70, 72), 16));//设备种类
                sxWeldModel.setWeldCode(str.substring(72, 82)); //设备编码
                sxWeldModel.setWeldCpuNum(Integer.valueOf(str.substring(82, 84), 16));//焊机CPU个数
                sxWeldModel.setCpu1No(Integer.valueOf(str.substring(84, 86), 16).toString());//cpu1编号
                sxWeldModel.setCpu1Model(Integer.valueOf(str.substring(86, 90), 16));
                sxWeldModel.setCpu1Version(CommonUtils.convertHexToString(str.substring(90, 114))); //cpu1软件版本
                sxWeldModel.setCpu2No(Integer.valueOf(str.substring(114, 116), 16).toString());
                sxWeldModel.setCpu2Model(Integer.valueOf(str.substring(116, 120), 16));
                sxWeldModel.setCpu2Version(CommonUtils.convertHexToString(str.substring(120, 144)));
                sxWeldModel.setCpu3No(Integer.valueOf(str.substring(144, 146), 16).toString());
                sxWeldModel.setCpu3Model(Integer.valueOf(str.substring(146, 150), 16));
                sxWeldModel.setCpu3Version(CommonUtils.convertHexToString(str.substring(150, 174)));
                sxWeldModel.setWeldNo("0001");
                sxWeldModel.setCreateTime(DateTimeUtils.getNowDateTime()); //创建时间
                return sxWeldModel;
            }
        }
        return null;
    }

    /**
     * 松下GL5实时数据解析发mq
     *
     * @return SxRtDataUI
     */
    public SxRtDataUi sxRtDataUiAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 206 && "FE5AA50067".equals(str.substring(0, 10))) {
                try {
                    SxRtDataUi sxRtDataUi = new SxRtDataUi();
                    String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                    String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                    String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                    String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                    String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                    String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                    //毫秒
                    String millisecond = Integer.valueOf(str.substring(58, 62), 16).toString();
                    //焊机时间
                    String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                    sxRtDataUi.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                    sxRtDataUi.setWeldIp(clientIp);
                    sxRtDataUi.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                    sxRtDataUi.setFunctionFlag(Integer.valueOf(str.substring(76, 80), 16).toString());
                    sxRtDataUi.setWeldStatus(Integer.valueOf(str.substring(84, 88), 16));
                    sxRtDataUi.setInitialEle(BigDecimal.valueOf(Long.valueOf(str.substring(88, 92), 16)));
                    sxRtDataUi.setInitialVol(BigDecimal.valueOf(Long.valueOf(str.substring(92, 96), 16)));
                    sxRtDataUi.setInitialWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)));
                    sxRtDataUi.setWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(100, 104), 16)));
                    sxRtDataUi.setWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(104, 108), 16)));
                    sxRtDataUi.setWeldWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(108, 112), 16)));
                    sxRtDataUi.setArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(112, 116), 16)));
                    sxRtDataUi.setArcVol(BigDecimal.valueOf(Long.valueOf(str.substring(116, 120), 16)));
                    sxRtDataUi.setArcWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(120, 124), 16)));
                    sxRtDataUi.setRealityWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(124, 128), 16)));
                    sxRtDataUi.setRealityWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(128, 132), 16)));
                    sxRtDataUi.setRealityWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(132, 136), 16)));
                    sxRtDataUi.setWeldFlag(0);
                    return sxRtDataUi;
                } catch (Exception e) {
                    log.error("松下GL5实时数据解析异常：{}", e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * 松下实时数据解析存数据库
     *
     * @param str 16进制字符串
     * @return SxRtDataDB
     */
    public SxRtDataDb sxRtDataDbAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 206 && "FE5AA50067".equals(str.substring(0, 10))) {
                try {
                    SxRtDataDb sxRtDataDb = new SxRtDataDb();
                    String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                    String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                    String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                    String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                    String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                    String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                    String millisecond = Integer.valueOf(str.substring(58, 62), 16).toString();
                    String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                    sxRtDataDb.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                    sxRtDataDb.setWeldIp(clientIp);
                    sxRtDataDb.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                    sxRtDataDb.setFunctionFlag(Integer.valueOf(str.substring(76, 80), 16).toString());
                    sxRtDataDb.setWeldStatus(Integer.valueOf(str.substring(84, 88), 16));
                    sxRtDataDb.setInitialEle(BigDecimal.valueOf(Long.valueOf(str.substring(88, 92), 16)));
                    sxRtDataDb.setInitialVol(BigDecimal.valueOf(Long.valueOf(str.substring(92, 96), 16)));
                    sxRtDataDb.setInitialWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)));
                    sxRtDataDb.setWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(100, 104), 16)));
                    sxRtDataDb.setWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(104, 108), 16)));
                    sxRtDataDb.setWeldWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(108, 112), 16)));
                    sxRtDataDb.setArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(112, 116), 16)));
                    sxRtDataDb.setArcVol(BigDecimal.valueOf(Long.valueOf(str.substring(116, 120), 16)));
                    sxRtDataDb.setArcWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(120, 124), 16)));
                    sxRtDataDb.setRealityWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(124, 128), 16)));
                    sxRtDataDb.setRealityWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(128, 132), 16)));
                    sxRtDataDb.setRealityWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(132, 136), 16)));
                    sxRtDataDb.setAlarmsCode(Integer.valueOf(str.substring(140, 144), 16).toString());
                    sxRtDataDb.setCreateTime(DateTimeUtils.getNowDateTime());
                    sxRtDataDb.setWeldFlag(0);
                    //刷卡领取任务后进行数据绑定
                    ConcurrentHashMap<String, TaskClaimIssue> sxTaskClaimMap = CommonDbData.SX_TASK_CLAIM_MAP;
                    if (sxTaskClaimMap.size() > 0 && sxTaskClaimMap.containsKey(clientIp)) {
                        final TaskClaimIssue taskClaimIssue = sxTaskClaimMap.get(clientIp);
                        if (null != taskClaimIssue) {
                            sxRtDataDb.setWelderId(taskClaimIssue.getWelderId());
                            sxRtDataDb.setWelderName(taskClaimIssue.getWelderName());
                            sxRtDataDb.setWelderDeptId(taskClaimIssue.getWelderDeptId());
                            sxRtDataDb.setTaskId(taskClaimIssue.getTaskId());
                            sxRtDataDb.setTaskName(taskClaimIssue.getTaskName());
                            sxRtDataDb.setTaskNo(taskClaimIssue.getTaskNo());
                        }
                    }
                    return sxRtDataDb;
                } catch (Exception e) {
                    log.error("松下GL5实时数据解析异常：{}", e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * 松下GL5状态信息解析发mq
     *
     * @param str 16进制字符串
     * @return 松下GL5状态数据实体类
     */
    public SxStatusDataUI sxStatusDataUiAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 246 && "FE5AA5007B".equals(str.substring(0, 10))) {
                SxStatusDataUI sxStatusDataUi = new SxStatusDataUI();
                String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                sxStatusDataUi.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                sxStatusDataUi.setWeldIp(clientIp);
                sxStatusDataUi.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                sxStatusDataUi.setFunctionFlag(Integer.valueOf(str.substring(76, 80), 16).toString());
                sxStatusDataUi.setModeSelect(Integer.valueOf(str.substring(84, 86), 16));
                sxStatusDataUi.setWeldingControl(Integer.valueOf(str.substring(86, 88), 16));
                sxStatusDataUi.setWeldingManner(Integer.valueOf(str.substring(88, 90), 16));
                sxStatusDataUi.setTexture(Integer.valueOf(str.substring(90, 92), 16));
                sxStatusDataUi.setWireDiameter(Integer.valueOf(str.substring(92, 94), 16));
                sxStatusDataUi.setGases(Integer.valueOf(str.substring(94, 96), 16));
                sxStatusDataUi.setWireFeed(Integer.valueOf(str.substring(96, 98), 16));
                sxStatusDataUi.setCheckGasStatus(Integer.valueOf(str.substring(98, 100), 16));
                sxStatusDataUi.setCutStatus(Integer.valueOf(str.substring(100, 102), 16));
                sxStatusDataUi.setLockStatus(Integer.valueOf(str.substring(102, 104), 16));
                sxStatusDataUi.setEleShowSelect(Integer.valueOf(str.substring(104, 106), 16));
                sxStatusDataUi.setRev(BigDecimal.valueOf(Long.valueOf(str.substring(106, 108), 16)));
                sxStatusDataUi.setBoardThickness(BigDecimal.valueOf(Long.valueOf(str.substring(108, 110), 16)));
                sxStatusDataUi.setSpotWeldingTime(BigDecimal.valueOf(Long.valueOf(str.substring(110, 114), 16)));
                sxStatusDataUi.setVolShowSelect(Integer.valueOf(str.substring(114, 116), 16));
                sxStatusDataUi.setArcLength(BigDecimal.valueOf(Long.valueOf(str.substring(116, 118), 16)));
                sxStatusDataUi.setArcCharacter(Integer.valueOf(str.substring(118, 120), 16));
                sxStatusDataUi.setPenetrationControl(Integer.valueOf(str.substring(120, 122), 16));
                sxStatusDataUi.setBeforeAspiratedTime(BigDecimal.valueOf(Long.valueOf(str.substring(122, 126), 16)));
                sxStatusDataUi.setAfterStopGasTime(BigDecimal.valueOf(Long.valueOf(str.substring(126, 130), 16)));
                sxStatusDataUi.setUnitaryDifference(Integer.valueOf(str.substring(130, 132), 16));
                sxStatusDataUi.setNowChannel(Integer.valueOf(str.substring(132, 134), 16));
                sxStatusDataUi.setMaxChannel(Integer.valueOf(str.substring(134, 136), 16));
                sxStatusDataUi.setWeldFlag(0);
                return sxStatusDataUi;
            }
        }
        return null;
    }

    /**
     * 松下工艺下发回复解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return 工艺返回实体类
     */
    public SxProcessReturn sxProcessReturnAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 106 && "FE5AA50035".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                SxProcessReturn sxProcessReturn = new SxProcessReturn();
                sxProcessReturn.setWeldIp(clientIp);
                String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                sxProcessReturn.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                sxProcessReturn.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                sxProcessReturn.setChannelNo(Integer.valueOf(str.substring(76, 78), 16).toString());
                return sxProcessReturn;
            }
        }
        return null;
    }

    /**
     * 松下工艺删除回复解析
     *
     * @param clientIp
     * @param str
     * @return
     */
    public SxProcessDeleteReturn sxProcessDeleteReturnAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 106 && "FE5AA50028".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                SxProcessDeleteReturn sxProcessDeleteReturn = new SxProcessDeleteReturn();
                sxProcessDeleteReturn.setWeldIp(clientIp);
                String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                sxProcessDeleteReturn.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                sxProcessDeleteReturn.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                sxProcessDeleteReturn.setChannelNo(Integer.valueOf(str.substring(76, 78), 16).toString());
                return sxProcessDeleteReturn;
            }
        }
        return null;
    }

    /**
     * 松下工艺索取回复解析（无数据）
     *
     * @param clientIp
     * @param str
     * @return
     */
    public SxProcessClaimReturn sxProcessClaimReturnAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            SxProcessClaimReturn sxProcessClaimReturn = new SxProcessClaimReturn();
            sxProcessClaimReturn.setWeldIp(clientIp);
            String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
            String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
            String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
            String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
            String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
            String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
            String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            sxProcessClaimReturn.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
            sxProcessClaimReturn.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
            sxProcessClaimReturn.setChannelNo(Integer.valueOf(str.substring(76, 78), 16).toString());
            sxProcessClaimReturn.setDataFlag(Integer.valueOf(str.substring(78, 80), 16));
            return sxProcessClaimReturn;
        }
        return null;
    }

    /**
     * 松下焊机通道设定回复解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return 松下焊机通道设定回复实体类
     */
    public SxWeldChannelSetReturn sxWeldChannelSetReturnAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 106 && "FE5AA50035".equals(str.substring(0, 10)) && "1202".equals(str.substring(40, 44))) {
                SxWeldChannelSetReturn weldChannelSetReturn = new SxWeldChannelSetReturn();
                weldChannelSetReturn.setWeldIp(clientIp);
                String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                weldChannelSetReturn.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                //读写标志：0：主动上传；1：读取；2：设置；3：删除
                weldChannelSetReturn.setReadWriteFlag(Integer.valueOf(str.substring(70, 72), 16));
                weldChannelSetReturn.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                weldChannelSetReturn.setFunction(Integer.valueOf(str.substring(78, 80), 16));
                weldChannelSetReturn.setChannelSelect(Integer.valueOf(str.substring(80, 82), 16));
                return weldChannelSetReturn;
            }
        }
        return null;
    }

    /**
     * 松下CO2焊机工艺索取返回协议解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return 松下CO2焊机工艺索取返回实体类
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    public SxCO2ProcessClaimReturn sxCO2ProcessClaimReturnAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 406 && "FE5AA500CB".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                SxCO2ProcessClaimReturn claimReturn = new SxCO2ProcessClaimReturn();
                claimReturn.setWeldIp(clientIp);
                String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                claimReturn.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                claimReturn.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                claimReturn.setChannelNo(Integer.valueOf(str.substring(76, 78), 16).toString());
                claimReturn.setDataFlag(Integer.valueOf(str.substring(78, 80), 16));
                claimReturn.setChannelFlag(Integer.valueOf(str.substring(80, 82), 16));
                //预置参数
                claimReturn.setInitialEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(82, 86), 16)));
                claimReturn.setInitialEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(86, 90), 16)));
                claimReturn.setInitialVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(90, 94), 16)));
                claimReturn.setInitialVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(94, 98), 16)));
                claimReturn.setInitialWireSpeedMax(BigDecimal.valueOf(Long.valueOf(str.substring(98, 102), 16)));
                claimReturn.setInitialWireSpeedMin(BigDecimal.valueOf(Long.valueOf(str.substring(102, 106), 16)));
                claimReturn.setWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(106, 110), 16)));
                claimReturn.setWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(110, 114), 16)));
                claimReturn.setWeldVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(114, 118), 16)));
                claimReturn.setWeldVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(118, 122), 16)));
                claimReturn.setWeldWireSpeedMax(BigDecimal.valueOf(Long.valueOf(str.substring(122, 126), 16)));
                claimReturn.setWeldWireSpeedMin(BigDecimal.valueOf(Long.valueOf(str.substring(126, 130), 16)));
                claimReturn.setArcEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(130, 134), 16)));
                claimReturn.setArcEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(134, 138), 16)));
                claimReturn.setArcVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(138, 142), 16)));
                claimReturn.setArcVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(142, 146), 16)));
                claimReturn.setArcWireSpeedMax(BigDecimal.valueOf(Long.valueOf(str.substring(146, 150), 16)));
                claimReturn.setArcWireSpeedMin(BigDecimal.valueOf(Long.valueOf(str.substring(150, 154), 16)));
                //焊接条件
                claimReturn.setModeSelect(Integer.valueOf(str.substring(174, 176), 16));
                claimReturn.setWeldingControl(Integer.valueOf(str.substring(176, 178), 16));
                claimReturn.setWeldingManner(Integer.valueOf(str.substring(178, 180), 16));
                claimReturn.setTexture(Integer.valueOf(str.substring(180, 182), 16));
                claimReturn.setWireDiameter(Integer.valueOf(str.substring(182, 184), 16));
                claimReturn.setGases(Integer.valueOf(str.substring(184, 186), 16));
                claimReturn.setWireFeed(Integer.valueOf(str.substring(186, 188), 16));
                claimReturn.setCheckGasStatus(Integer.valueOf(str.substring(188, 190), 16));
                claimReturn.setCutStatus(Integer.valueOf(str.substring(190, 192), 16));
                claimReturn.setLockStatus(Integer.valueOf(str.substring(192, 194), 16));
                claimReturn.setEleShowSelect(Integer.valueOf(str.substring(194, 196), 16));
                claimReturn.setRev(BigDecimal.valueOf(Long.valueOf(str.substring(196, 198), 16)));
                claimReturn.setBoardThickness(BigDecimal.valueOf(Long.valueOf(str.substring(198, 200), 16)));
                claimReturn.setSpotWeldingTime(BigDecimal.valueOf(Long.valueOf(str.substring(200, 204), 16)));
                claimReturn.setVolShowSelect(Integer.valueOf(str.substring(204, 206), 16));
                claimReturn.setArcLength(BigDecimal.valueOf(Long.valueOf(str.substring(206, 208), 16)));
                claimReturn.setArcCharacter(Integer.valueOf(str.substring(208, 210), 16));
                claimReturn.setPenetrationControl(Integer.valueOf(str.substring(210, 212), 16));
                claimReturn.setBeforeAspiratedTime(BigDecimal.valueOf(Long.valueOf(str.substring(212, 216), 16)));
                claimReturn.setAfterStopGasTime(BigDecimal.valueOf(Long.valueOf(str.substring(216, 220), 16)));
                claimReturn.setUnitaryDifference(Integer.valueOf(str.substring(220, 222), 16));
                claimReturn.setNowChannel(Integer.valueOf(str.substring(222, 224), 16));
                //动态限流参数
                claimReturn.setDclInitialEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(244, 248), 16)));
                claimReturn.setDclInitialEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(248, 252), 16)));
                claimReturn.setDclWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(252, 256), 16)));
                claimReturn.setDclWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(256, 260), 16)));
                claimReturn.setDclArcEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(260, 264), 16)));
                claimReturn.setDclArcEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(264, 268), 16)));
                claimReturn.setStartDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(268, 270), 16)));
                claimReturn.setClAmendPeriod(BigDecimal.valueOf(Long.valueOf(str.substring(270, 272), 16)));
                //超限报警参数
                claimReturn.setOaInitialEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(292, 296), 16)));
                claimReturn.setOaInitialEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(296, 300), 16)));
                claimReturn.setOaInitialVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(300, 304), 16)));
                claimReturn.setOaInitialVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(304, 308), 16)));
                claimReturn.setOaWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(308, 312), 16)));
                claimReturn.setOaWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(312, 316), 16)));
                claimReturn.setOaWeldVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(316, 320), 16)));
                claimReturn.setOaWeldVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(320, 324), 16)));
                claimReturn.setOaArcEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(324, 328), 16)));
                claimReturn.setOaArcEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(328, 332), 16)));
                claimReturn.setOaArcVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(332, 336), 16)));
                claimReturn.setOaArcVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(336, 340), 16)));
                claimReturn.setArcDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(340, 342), 16)));
                claimReturn.setAlarmDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(342, 346), 16)));
                claimReturn.setHaltDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(346, 348), 16)));
                claimReturn.setHaltFreezeTime(BigDecimal.valueOf(Long.valueOf(str.substring(348, 350), 16)));
                return claimReturn;
            }
        }
        return null;
    }

    /**
     * 松下TIG焊机工艺索取返回协议解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return 松下TIG焊机工艺索取返回实体类
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    public SxTIGProcessClaimReturn sxTIGProcessClaimReturnAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 446 && "FE5AA500CB".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                SxTIGProcessClaimReturn claimReturn = new SxTIGProcessClaimReturn();
                claimReturn.setWeldIp(clientIp);
                String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                claimReturn.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                claimReturn.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                claimReturn.setChannelNo(Integer.valueOf(str.substring(76, 78), 16).toString());
                claimReturn.setDataFlag(Integer.valueOf(str.substring(78, 80), 16));
                claimReturn.setChannelFlag(Integer.valueOf(str.substring(80, 82), 16));
                //预置参数
                claimReturn.setInitialEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(82, 86), 16)));
                claimReturn.setInitialEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(86, 90), 16)));
                claimReturn.setInitialVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(90, 94), 16)));
                claimReturn.setInitialVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(94, 98), 16)));
                claimReturn.setFirstWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(98, 102), 16)));
                claimReturn.setFirstWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(102, 106), 16)));
                claimReturn.setFirstWeldVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(106, 110), 16)));
                claimReturn.setFirstWeldVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(110, 114), 16)));
                claimReturn.setSecondWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(114, 118), 16)));
                claimReturn.setSecondWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(118, 122), 16)));
                claimReturn.setSecondWeldVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(122, 126), 16)));
                claimReturn.setSecondWeldVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(126, 130), 16)));
                claimReturn.setArcEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(130, 134), 16)));
                claimReturn.setArcEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(134, 138), 16)));
                claimReturn.setArcVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(138, 142), 16)));
                claimReturn.setArcVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(142, 146), 16)));
                claimReturn.setPeakWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(146, 150), 16)));
                claimReturn.setPeakWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(150, 154), 16)));
                claimReturn.setPeakWeldVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(154, 158), 16)));
                claimReturn.setPeakWeldVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(158, 162), 16)));
                //焊接条件
                claimReturn.setWeldMethod(Integer.valueOf(str.substring(182, 184), 16));
                claimReturn.setArcHaveNot(Integer.valueOf(str.substring(184, 186), 16));
                claimReturn.setPulseHaveNot(Integer.valueOf(str.substring(186, 188), 16));
                claimReturn.setAcWaveform(Integer.valueOf(str.substring(188, 190), 16));
                claimReturn.setPulseRate(BigDecimal.valueOf(Long.valueOf(str.substring(190, 194), 16)));
                claimReturn.setPulseFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(194, 198), 16)));
                claimReturn.setCleanWidth(BigDecimal.valueOf(Long.valueOf(str.substring(198, 202), 16)));
                claimReturn.setAcFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(202, 206), 16)));
                claimReturn.setMixFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(206, 210), 16)));
                claimReturn.setMixAcRate(BigDecimal.valueOf(Long.valueOf(str.substring(210, 214), 16)));
                claimReturn.setPulseRadian(BigDecimal.valueOf(Long.valueOf(str.substring(214, 218), 16)));
                claimReturn.setArcStiffness(BigDecimal.valueOf(Long.valueOf(str.substring(218, 222), 16)));
                claimReturn.setHandWeldThrust(BigDecimal.valueOf(Long.valueOf(str.substring(222, 226), 16)));
                claimReturn.setBeforeAspiratedTime(BigDecimal.valueOf(Long.valueOf(str.substring(226, 230), 16)));
                claimReturn.setAfterStopGasTime(BigDecimal.valueOf(Long.valueOf(str.substring(230, 234), 16)));
                claimReturn.setMainWeldRiseTime(BigDecimal.valueOf(Long.valueOf(str.substring(234, 238), 16)));
                claimReturn.setMainWeldDeclineTime(BigDecimal.valueOf(Long.valueOf(str.substring(238, 242), 16)));
                claimReturn.setMainWeldRiseRadian(BigDecimal.valueOf(Long.valueOf(str.substring(242, 246), 16)));
                claimReturn.setMainWeldDeclineRadian(BigDecimal.valueOf(Long.valueOf(str.substring(246, 250), 16)));
                claimReturn.setSpotWeldingTime(BigDecimal.valueOf(Long.valueOf(str.substring(250, 254), 16)));
                claimReturn.setSpotWeldIntervalTime(BigDecimal.valueOf(Long.valueOf(str.substring(254, 258), 16)));
                claimReturn.setSpotWeldRiseTime(BigDecimal.valueOf(Long.valueOf(str.substring(258, 262), 16)));
                claimReturn.setSpotWeldDeclineTime(BigDecimal.valueOf(Long.valueOf(str.substring(262, 266), 16)));
                claimReturn.setSpotWeldRiseRadian(BigDecimal.valueOf(Long.valueOf(str.substring(266, 270), 16)));
                claimReturn.setSpotWeldDeclineRadian(BigDecimal.valueOf(Long.valueOf(str.substring(270, 274), 16)));
                claimReturn.setMaxChannel(Integer.valueOf(str.substring(274, 276), 16));
                claimReturn.setNowChannel(Integer.valueOf(str.substring(276, 278), 16));
                claimReturn.setHandWeldWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(278, 282), 16)));
                claimReturn.setHandWeldArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(282, 286), 16)));
                //动态限流参数
                claimReturn.setDclInitialEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(298, 302), 16)));
                claimReturn.setDclInitialEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(302, 306), 16)));
                claimReturn.setDclWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(306, 310), 16)));
                claimReturn.setDclWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(310, 314), 16)));
                claimReturn.setDclSecondWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(314, 318), 16)));
                claimReturn.setDclSecondWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(318, 322), 16)));
                claimReturn.setDclArcEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(322, 326), 16)));
                claimReturn.setDclArcEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(326, 330), 16)));
                claimReturn.setStartDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(330, 332), 16)));
                claimReturn.setClAmendPeriod(BigDecimal.valueOf(Long.valueOf(str.substring(332, 334), 16)));
                //超限报警参数
                claimReturn.setOaInitialEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(354, 358), 16)));
                claimReturn.setOaInitialEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(358, 362), 16)));
                claimReturn.setOaInitialVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(362, 366), 16)));
                claimReturn.setOaInitialVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(366, 370), 16)));
                claimReturn.setOaWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(370, 374), 16)));
                claimReturn.setOaWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(374, 378), 16)));
                claimReturn.setOaWeldVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(378, 382), 16)));
                claimReturn.setOaWeldVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(382, 386), 16)));
                claimReturn.setOaSecondWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(386, 390), 16)));
                claimReturn.setOaSecondWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(390, 394), 16)));
                claimReturn.setOaSecondWeldVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(394, 398), 16)));
                claimReturn.setOaSecondWeldVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(398, 402), 16)));
                claimReturn.setOaArcEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(402, 406), 16)));
                claimReturn.setOaArcEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(406, 410), 16)));
                claimReturn.setOaArcVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(410, 414), 16)));
                claimReturn.setOaArcVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(414, 418), 16)));
                claimReturn.setArcDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(418, 420), 16)));
                claimReturn.setAlarmDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(420, 422), 16)));
                claimReturn.setHaltDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(422, 424), 16)));
                claimReturn.setHaltFreezeTime(BigDecimal.valueOf(Long.valueOf(str.substring(424, 426), 16)));
                return claimReturn;
            }
        }
        return null;
    }

    /**
     * 松下FR2系列CO2实时数据解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制数据包
     * @return 实时表实体类
     */
    public SxRtDataDb fr2Co2RtDataDbAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            SxRtDataDb rtDataDb = new SxRtDataDb();
            rtDataDb.setWeldIp(clientIp);
            String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
            String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
            String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
            String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
            String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
            String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
            String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            rtDataDb.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
            rtDataDb.setWeldStatus(Integer.valueOf(str.substring(70, 72), 16));
            rtDataDb.setPresetEle(BigDecimal.valueOf(Long.valueOf(str.substring(72, 76), 16)));
            rtDataDb.setPresetVol(BigDecimal.valueOf(Long.valueOf(str.substring(76, 80), 16)));
            rtDataDb.setPresetInitialEle(BigDecimal.valueOf(Long.valueOf(str.substring(80, 84), 16)));
            rtDataDb.setPresetInitialVol(BigDecimal.valueOf(Long.valueOf(str.substring(84, 88), 16)));
            rtDataDb.setPresetArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(88, 92), 16)));
            rtDataDb.setPresetArcVol(BigDecimal.valueOf(Long.valueOf(str.substring(92, 96), 16)));
            rtDataDb.setRealityWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)));
            rtDataDb.setRealityWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(100, 104), 16)));
            rtDataDb.setWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(104, 108), 16)));
            rtDataDb.setAlarmsCode(Integer.valueOf(str.substring(108, 112), 16).toString());
            rtDataDb.setRunFlag(Integer.valueOf(str.substring(118, 120), 16));
            rtDataDb.setWeldFlag(1);
            //刷卡领取任务后进行数据绑定
            ConcurrentHashMap<String, TaskClaimIssue> sxTaskClaimMap = CommonDbData.SX_TASK_CLAIM_MAP;
            if (sxTaskClaimMap.size() > 0 && sxTaskClaimMap.containsKey(clientIp)) {
                final TaskClaimIssue taskClaimIssue = sxTaskClaimMap.get(clientIp);
                if (null != taskClaimIssue) {
                    rtDataDb.setWelderId(taskClaimIssue.getWelderId());
                    rtDataDb.setWelderName(taskClaimIssue.getWelderName());
                    rtDataDb.setWelderDeptId(taskClaimIssue.getWelderDeptId());
                    rtDataDb.setTaskId(taskClaimIssue.getTaskId());
                    rtDataDb.setTaskName(taskClaimIssue.getTaskName());
                    rtDataDb.setTaskNo(taskClaimIssue.getTaskNo());
                }
            }
            return rtDataDb;
        }
        return null;
    }

    /**
     * 松下FR2系列CO2实时数据解析
     *
     * @param clientIp 设备IP
     * @param str      16进制字符串
     * @return SxRtDataUI
     */
    public SxRtDataUi fr2Co2RtDataUiAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            SxRtDataUi sxRtDataUi = new SxRtDataUi();
            sxRtDataUi.setWeldIp(clientIp);
            String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
            String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
            String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
            String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
            String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
            String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
            String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            sxRtDataUi.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
            sxRtDataUi.setWeldStatus(Integer.valueOf(str.substring(70, 72), 16));
            sxRtDataUi.setPresetEle(BigDecimal.valueOf(Long.valueOf(str.substring(72, 76), 16)));
            sxRtDataUi.setPresetVol(BigDecimal.valueOf(Long.valueOf(str.substring(76, 80), 16)));
            sxRtDataUi.setPresetInitialEle(BigDecimal.valueOf(Long.valueOf(str.substring(80, 84), 16)));
            sxRtDataUi.setPresetInitialVol(BigDecimal.valueOf(Long.valueOf(str.substring(84, 88), 16)));
            sxRtDataUi.setPresetArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(88, 92), 16)));
            sxRtDataUi.setPresetArcVol(BigDecimal.valueOf(Long.valueOf(str.substring(92, 96), 16)));
            sxRtDataUi.setRealityWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)));
            sxRtDataUi.setRealityWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(100, 104), 16)));
            sxRtDataUi.setWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(104, 108), 16)));
            sxRtDataUi.setAlarmsCode(Integer.valueOf(str.substring(108, 112), 16).toString());
            sxRtDataUi.setRunFlag(Integer.valueOf(str.substring(118, 120), 16));
            sxRtDataUi.setWeldFlag(1);
            return sxRtDataUi;
        }
        return null;
    }

    /**
     * 松下FR2系列TIG实时数据解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制数据包
     * @return FR2系列TIG实体类
     */
    public SxRtDataDb fr2TigRtDataDbAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            SxRtDataDb rtDataDb = new SxRtDataDb();
            rtDataDb.setWeldIp(clientIp);
            String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
            String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
            String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
            String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
            String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
            String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
            String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            rtDataDb.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
            rtDataDb.setWeldStatus(Integer.valueOf(str.substring(70, 72), 16));
            rtDataDb.setPresetInitialEle(BigDecimal.valueOf(Long.valueOf(str.substring(72, 76), 16)));
            rtDataDb.setPresetWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(76, 80), 16)));
            rtDataDb.setPresetArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(80, 84), 16)));
            rtDataDb.setPresetMaxEle(BigDecimal.valueOf(Long.valueOf(str.substring(84, 88), 16)));
            rtDataDb.setPulseRate(BigDecimal.valueOf(Long.valueOf(str.substring(88, 90), 16)));
            rtDataDb.setPulseFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(90, 94), 16)));
            rtDataDb.setCleanWidth(BigDecimal.valueOf(Long.valueOf(str.substring(94, 96), 16)));
            rtDataDb.setAcFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)));
            rtDataDb.setMixFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(100, 104), 16)));
            rtDataDb.setMixAcRate(BigDecimal.valueOf(Long.valueOf(str.substring(104, 106), 16)));
            rtDataDb.setRealityWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(106, 110), 16)));
            rtDataDb.setRealityWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(110, 114), 16)));
            rtDataDb.setAlarmsCode(Integer.valueOf(str.substring(114, 118), 16).toString());
            rtDataDb.setWeldFlag(2);
            //刷卡领取任务后进行数据绑定
            ConcurrentHashMap<String, TaskClaimIssue> sxTaskClaimMap = CommonDbData.SX_TASK_CLAIM_MAP;
            if (sxTaskClaimMap.size() > 0 && sxTaskClaimMap.containsKey(clientIp)) {
                final TaskClaimIssue taskClaimIssue = sxTaskClaimMap.get(clientIp);
                if (null != taskClaimIssue) {
                    rtDataDb.setWelderId(taskClaimIssue.getWelderId());
                    rtDataDb.setWelderName(taskClaimIssue.getWelderName());
                    rtDataDb.setWelderDeptId(taskClaimIssue.getWelderDeptId());
                    rtDataDb.setTaskId(taskClaimIssue.getTaskId());
                    rtDataDb.setTaskName(taskClaimIssue.getTaskName());
                    rtDataDb.setTaskNo(taskClaimIssue.getTaskNo());
                }
            }
            return rtDataDb;
        }
        return null;
    }

    /**
     * 松下FR2系列TIG实时数据解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return SxRtDataUi
     */
    public SxRtDataUi fr2TigRtDataUiAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            SxRtDataUi sxRtDataUi = new SxRtDataUi();
            sxRtDataUi.setWeldIp(clientIp);
            String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
            String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
            String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
            String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
            String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
            String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
            String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            sxRtDataUi.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
            sxRtDataUi.setWeldStatus(Integer.valueOf(str.substring(70, 72), 16));
            sxRtDataUi.setPresetInitialEle(BigDecimal.valueOf(Long.valueOf(str.substring(72, 76), 16)));
            sxRtDataUi.setPresetWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(76, 80), 16)));
            sxRtDataUi.setPresetArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(80, 84), 16)));
            sxRtDataUi.setPresetMaxEle(BigDecimal.valueOf(Long.valueOf(str.substring(84, 88), 16)));
            sxRtDataUi.setPulseRate(BigDecimal.valueOf(Long.valueOf(str.substring(88, 90), 16)));
            sxRtDataUi.setPulseFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(90, 94), 16)));
            sxRtDataUi.setCleanWidth(BigDecimal.valueOf(Long.valueOf(str.substring(94, 96), 16)));
            sxRtDataUi.setAcFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)));
            sxRtDataUi.setMixFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(100, 104), 16)));
            sxRtDataUi.setMixAcRate(BigDecimal.valueOf(Long.valueOf(str.substring(104, 106), 16)));
            sxRtDataUi.setRealityWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(106, 110), 16)));
            sxRtDataUi.setRealityWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(110, 114), 16)));
            sxRtDataUi.setAlarmsCode(Integer.valueOf(str.substring(114, 118), 16).toString());
            sxRtDataUi.setWeldFlag(2);
            return sxRtDataUi;
        }
        return null;
    }

    /**
     * 松下FR2系列CO2焊机状态数据解析
     *
     * @param clientIp
     * @param str
     * @return
     */
    public SxStatusDataUI fr2Co2StatusUiAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            SxStatusDataUI sxStatusDataUi = new SxStatusDataUI();
            String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
            String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
            String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
            String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
            String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
            String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
            String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            sxStatusDataUi.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
            sxStatusDataUi.setWeldIp(clientIp);
            sxStatusDataUi.setFunctionFlag(Integer.valueOf(str.substring(66, 68), 16).toString());
            sxStatusDataUi.setWeldModel(CommonUtils.convertHexToString(str.substring(70, 88)));
            sxStatusDataUi.setWelderNo(str.substring(88, 98));
            sxStatusDataUi.setWorkpieceNoMin(str.substring(98, 108));
            sxStatusDataUi.setTexture(Integer.valueOf(str.substring(108, 110), 16));
            sxStatusDataUi.setWireDiameter(Integer.valueOf(str.substring(110, 112), 16));
            sxStatusDataUi.setGases(Integer.valueOf(str.substring(112, 114), 16));
            sxStatusDataUi.setWeldingControl(Integer.valueOf(str.substring(114, 116), 16));
            sxStatusDataUi.setPulseHaveNot(Integer.valueOf(str.substring(116, 118), 16));
            sxStatusDataUi.setSpotWeldingTime(BigDecimal.valueOf(Long.valueOf(str.substring(118, 122), 16)));
            sxStatusDataUi.setNowChannel(Integer.valueOf(str.substring(122, 124), 16));
            sxStatusDataUi.setMaxChannel(Integer.valueOf(str.substring(124, 126), 16));
            sxStatusDataUi.setUnitaryDifference(Integer.valueOf(str.substring(126, 128), 16));
            sxStatusDataUi.setDryExtendLength(Integer.valueOf(str.substring(128, 130), 16));
            sxStatusDataUi.setWorkpieceNoMax(str.substring(130, 136));
            sxStatusDataUi.setWeldFlag(1);
            return sxStatusDataUi;
        }
        return null;
    }

    /**
     * 松下FR2系列TIG焊机状态数据解析
     *
     * @param clientIp
     * @param str
     * @return
     */
    public SxStatusDataUI fr2TigStatusUiAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            SxStatusDataUI sxStatusDataUi = new SxStatusDataUI();
            String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
            String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
            String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
            String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
            String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
            String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
            String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            sxStatusDataUi.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
            sxStatusDataUi.setWeldIp(clientIp);
            sxStatusDataUi.setWeldModel(CommonUtils.convertHexToString(str.substring(70, 88)));
            sxStatusDataUi.setWelderNo(str.substring(88, 98));
            sxStatusDataUi.setWorkpieceNoMin(str.substring(98, 108));
            sxStatusDataUi.setWeldMethod(Integer.valueOf(str.substring(108, 110), 16));
            sxStatusDataUi.setArcHaveNot(Integer.valueOf(str.substring(112, 114), 16));
            sxStatusDataUi.setPulseHaveNot(Integer.valueOf(str.substring(114, 116), 16));
            sxStatusDataUi.setAcWaveform(Integer.valueOf(str.substring(116, 118), 16));
            sxStatusDataUi.setSpotWeldingTime(BigDecimal.valueOf(Long.valueOf(str.substring(118, 122), 16)));
            sxStatusDataUi.setBeforeAspiratedTime(BigDecimal.valueOf(Long.valueOf(str.substring(122, 126), 16)));
            sxStatusDataUi.setAfterStopGasTime(BigDecimal.valueOf(Long.valueOf(str.substring(126, 130), 16)));
            sxStatusDataUi.setRiseTime(BigDecimal.valueOf(Long.valueOf(str.substring(130, 134), 16)));
            sxStatusDataUi.setDeclineTime(BigDecimal.valueOf(Long.valueOf(str.substring(134, 138))));
            sxStatusDataUi.setNowChannel(Integer.valueOf(str.substring(138, 140)));
            sxStatusDataUi.setMaxChannel(Integer.valueOf(str.substring(140, 142)));
            sxStatusDataUi.setWeldFlag(2);
            return sxStatusDataUi;
        }
        return null;
    }

    /**
     * 松下FR2系列通道参数查询（无参数）、下载、删除回复
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return SxChannelParamReply
     */
    private SxChannelParamReply sxChannelParamReplyAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if ("0211".equals(str.substring(40, 44))) {
                SxChannelParamReply sxChannelParamReply = new SxChannelParamReply();
                sxChannelParamReply.setCommand(Integer.valueOf(str.substring(44, 46), 16));
                sxChannelParamReply.setChannel(Integer.valueOf(str.substring(46, 48), 16).toString());
                sxChannelParamReply.setWeldIp(clientIp);
                return sxChannelParamReply;
            }
        }
        return null;
    }

    /**
     * 松下FR2系列通道参数查询（有参数）
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return SxChannelParamReplyHave
     */
    public SxChannelParamReplyHave sxChannelParamReplyHaveAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str) && "0211".equals(str.substring(40, 44))) {
            SxChannelParamReplyHave replyHave = new SxChannelParamReplyHave();
            replyHave.setWeldIp(clientIp);
            replyHave.setCommand(Integer.valueOf(str.substring(44, 46), 16));
            replyHave.setChannel(Integer.valueOf(str.substring(46, 48), 16).toString());
            replyHave.setChannelFlag(Integer.valueOf(str.substring(50, 52), 16));
            replyHave.setPresetEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(52, 56), 16)));
            replyHave.setPresetVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(56, 60), 16)));
            replyHave.setPresetEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(60, 64), 16)));
            replyHave.setPresetVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(64, 68), 16)));
            replyHave.setInitialEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(68, 72), 16)));
            replyHave.setInitialVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(72, 76), 16)));
            replyHave.setInitialEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(76, 80), 16)));
            replyHave.setInitialVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(80, 84), 16)));
            replyHave.setArcEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(84, 88), 16)));
            replyHave.setArcVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(88, 92), 16)));
            replyHave.setArcEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(92, 96), 16)));
            replyHave.setArcVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)));
            replyHave.setTexture(Integer.valueOf(str.substring(100, 102), 16));
            replyHave.setWireDiameter(Integer.valueOf(str.substring(102, 104), 16));
            replyHave.setGases(Integer.valueOf(str.substring(104, 106), 16));
            replyHave.setWeldingControl(Integer.valueOf(str.substring(106, 108), 16));
            replyHave.setPulseHaveNot(Integer.valueOf(str.substring(108, 110), 16));
            replyHave.setSpotWeldingTime(BigDecimal.valueOf(Long.valueOf(str.substring(110, 114), 16)));
            replyHave.setUnitaryDifference(Integer.valueOf(str.substring(114, 116), 16));
            replyHave.setDryExtendLength(Integer.valueOf(str.substring(116, 118), 16));
            replyHave.setWeldMax(BigDecimal.valueOf(Long.valueOf(str.substring(124, 128), 16)));
            replyHave.setWeldMin(BigDecimal.valueOf(Long.valueOf(str.substring(128, 132), 16)));
            replyHave.setInitialMax(BigDecimal.valueOf(Long.valueOf(str.substring(132, 136), 16)));
            replyHave.setInitialMin(BigDecimal.valueOf(Long.valueOf(str.substring(136, 140), 16)));
            replyHave.setArcMax(BigDecimal.valueOf(Long.valueOf(str.substring(140, 144), 16)));
            replyHave.setArcMin(BigDecimal.valueOf(Long.valueOf(str.substring(144, 148), 16)));
            replyHave.setDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(148, 150), 16)));
            replyHave.setAmendPeriod(BigDecimal.valueOf(Long.valueOf(str.substring(150, 152), 16)));
            replyHave.setPresetEleAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(152, 156), 16)));
            replyHave.setPresetVolAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(156, 160), 16)));
            replyHave.setPresetEleAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(160, 164), 16)));
            replyHave.setPresetVolAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(164, 168), 16)));
            replyHave.setInitialEleAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(168, 172), 16)));
            replyHave.setInitialVolAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(172, 176), 16)));
            replyHave.setInitialEleAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(176, 180), 16)));
            replyHave.setInitialVolAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(180, 184), 16)));
            replyHave.setArcEleAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(184, 188), 16)));
            replyHave.setArcVolAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(188, 192), 16)));
            replyHave.setArcEleAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(192, 196), 16)));
            replyHave.setArcVolAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(196, 200), 16)));
            replyHave.setArcDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(200, 202), 16)));
            replyHave.setAlarmDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(202, 204), 16)));
            replyHave.setAlarmHaltTime(BigDecimal.valueOf(Long.valueOf(str.substring(204, 206), 16)));
            replyHave.setFlowMax(BigDecimal.valueOf(Long.valueOf(str.substring(208, 210), 16)));
            return replyHave;
        }
        return null;
    }

    /**
     * 松下AT3系列查询回复（有参数）
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return At3ParamQueryReturn
     */
    public static At3ParamQueryReturn at3ParamQueryReturnAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            At3ParamQueryReturn at3ParamQueryReturn = new At3ParamQueryReturn();
            at3ParamQueryReturn.setWeldIp(clientIp);
            at3ParamQueryReturn.setCommand(Integer.valueOf(str.substring(44, 46), 16));
            at3ParamQueryReturn.setChannel(Integer.valueOf(str.substring(46, 48), 16).toString());
            at3ParamQueryReturn.setChannelFlag(Integer.valueOf(str.substring(50, 52), 16));
            at3ParamQueryReturn.setPresetEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(52, 56), 16)));
            at3ParamQueryReturn.setPresetVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(56, 60), 16)));
            at3ParamQueryReturn.setPresetEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(60, 64), 16)));
            at3ParamQueryReturn.setPresetVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(64, 68), 16)));
            at3ParamQueryReturn.setEleAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(68, 72), 16)));
            at3ParamQueryReturn.setVolAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(72, 76), 16)));
            at3ParamQueryReturn.setEleAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(76, 80), 16)));
            at3ParamQueryReturn.setVolAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(80, 84), 16)));
            at3ParamQueryReturn.setAlarmDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(84, 86), 16)));
            at3ParamQueryReturn.setAlarmHaltTime(BigDecimal.valueOf(Long.valueOf(str.substring(86, 88), 16)));
            return at3ParamQueryReturn;
        }
        return null;
    }

    /**
     * 松下设备关机数据处理
     *
     * @param clientIp 设备IP
     */
    public static void sxWeldOffDataManage(String clientIp) {
        if (NettyServerHandler.SX_CLIENT_IP_BIND_WELD_INFO.containsKey(clientIp)) {
            SxWeldModel sxWeld = NettyServerHandler.SX_CLIENT_IP_BIND_WELD_INFO.get(clientIp);
            SxRtDataUi sxRtDataUi = new SxRtDataUi();
            sxRtDataUi.setWeldCode(sxWeld.getWeldCode());
            sxRtDataUi.setWeldIp(clientIp);
            sxRtDataUi.setWeldModel(sxWeld.getWeldModel());
            //-1 为关机
            sxRtDataUi.setWeldStatus(-1);
            //实体类转JSON字符串
            String message = JSON.toJSONString(sxRtDataUi);
            //通过mqtt发送到服务端
            EmqMqttClient.publishMessage(UpTopicEnum.sxrtdata.name(), message, 0);
            log.info("SX关机：" + "：{}", UpTopicEnum.sxrtdata.name() + ":" + message);
            NettyServerHandler.SX_CLIENT_IP_BIND_WELD_INFO.remove(clientIp);
            //根据IP地址查询松下焊机信息
            SxWeldService sxWeldService = BeanContext.getBean(SxWeldService.class);
            SxWeldModel sxWeldModel = sxWeldService.getSxWeldByWeldIp(clientIp);
            //新增设备关机时间
            WeldOnOffTimeService onOffTimeService = BeanContext.getBean(WeldOnOffTimeService.class);
            WeldOnOffTime onOffTime = new WeldOnOffTime();
            onOffTime.setEndTime(DateTimeUtils.getNowDateTime());
            if (null != sxWeldModel && null != sxWeldModel.getId()) {
                onOffTime.setMachineId(sxWeldModel.getId());
            }
            onOffTime.setMachineType(1);
            onOffTime.setWeldsxIp(clientIp);
            onOffTimeService.insertWeldOnOffTime(onOffTime);
        }
    }

    /**
     * 松下CO2焊机工艺下发参数拼接
     *
     * @param sxCO2ProcessIssue 松下CO2焊机工艺下发实体类
     * @return 16进制字符串
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    public static String sxCO2ProcessProtocol(SxCO2ProcessIssue sxCO2ProcessIssue) {
        if (null != sxCO2ProcessIssue) {
            try {
                //头部固定参数
                String head = SxVerificationCode.SX_CO2_PROCESS_ISSUE_HEAD;
                LocalDateTime localDateTime = LocalDateTime.now();
                String year = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("yy")), 2);
                String month = CommonUtils.lengthJoint(localDateTime.getMonthValue(), 2);
                String day = CommonUtils.lengthJoint(localDateTime.getDayOfMonth(), 2);
                String hour = CommonUtils.lengthJoint(localDateTime.getHour(), 2);
                String minute = CommonUtils.lengthJoint(localDateTime.getMinute(), 2);
                String second = CommonUtils.lengthJoint(localDateTime.getSecond(), 2);
                String millisecond = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("SSS")), 4);
                String datetime = year + month + day + hour + minute + second + millisecond;
                String readWriteFlag = "00000000020000";
                String channelNo = CommonUtils.lengthJoint(sxCO2ProcessIssue.getChannelNo(), 2);
                String dataFlag = "0100";
                //预置参数
                String initialEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getInitialEleMax().intValue(), 4);
                String initialEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getInitialEleMin().intValue(), 4);
                String initialVolMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getInitialVolMax().intValue(), 4);
                String initialVolMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getInitialVolMin().intValue(), 4);
                String initialWireSpeedMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getInitialWireSpeedMax().intValue(), 4);
                String initialWireSpeedMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getInitialWireSpeedMin().intValue(), 4);
                String weldEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldEleMax().intValue(), 4);
                String weldEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldEleMin().intValue(), 4);
                String weldVolMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldVolMax().intValue(), 4);
                String weldVolMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldVolMin().intValue(), 4);
                String weldWireSpeedMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldWireSpeedMax().intValue(), 4);
                String weldWireSpeedMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldWireSpeedMin().intValue(), 4);
                String arcEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcEleMax().intValue(), 4);
                String arcEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcEleMin().intValue(), 4);
                String arcVolMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcVolMax().intValue(), 4);
                String arcVolMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcVolMin().intValue(), 4);
                String arcWireSpeedMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcWireSpeedMax().intValue(), 4);
                String arcWireSpeedMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcWireSpeedMin().intValue(), 4);
                String reserved1 = "00000000000000000000";
                //焊接条件
                String modeSelect = CommonUtils.lengthJoint(sxCO2ProcessIssue.getModeSelect(), 2);
                String weldingControl = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldingControl(), 2);
                String weldingManner = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWeldingManner(), 2);
                String texture = CommonUtils.lengthJoint(sxCO2ProcessIssue.getTexture(), 2);
                String wireDiameter = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWireDiameter(), 2);
                String gases = CommonUtils.lengthJoint(sxCO2ProcessIssue.getGases(), 2);
                String wireFeed = CommonUtils.lengthJoint(sxCO2ProcessIssue.getWireFeed(), 2);
                String checkGasStatus = CommonUtils.lengthJoint(sxCO2ProcessIssue.getCheckGasStatus(), 2);
                String cutStatus = CommonUtils.lengthJoint(sxCO2ProcessIssue.getCutStatus(), 2);
                String lockStatus = CommonUtils.lengthJoint(sxCO2ProcessIssue.getLockStatus(), 2);
                String eleShowSelect = CommonUtils.lengthJoint(sxCO2ProcessIssue.getEleShowSelect(), 2);
                String rev = CommonUtils.lengthJoint(sxCO2ProcessIssue.getRev().intValue(), 2);
                String boardThickness = CommonUtils.lengthJoint(sxCO2ProcessIssue.getBoardThickness().intValue(), 2);
                String spotWeldingTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getSpotWeldingTime().intValue(), 4);
                String volShowSelect = CommonUtils.lengthJoint(sxCO2ProcessIssue.getVolShowSelect(), 2);
                String arcLength = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcLength().intValue(), 2);
                String arcCharacter = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcCharacter(), 2);
                String penetrationControl = CommonUtils.lengthJoint(sxCO2ProcessIssue.getPenetrationControl(), 2);
                String beforeAspiratedTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getBeforeAspiratedTime().intValue(), 4);
                String afterStopGasTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getAfterStopGasTime().intValue(), 4);
                String unitaryDifference = CommonUtils.lengthJoint(sxCO2ProcessIssue.getUnitaryDifference(), 2);
                String nowChannel = CommonUtils.lengthJoint(sxCO2ProcessIssue.getNowChannel(), 2);
                String reserved2 = "00000000000000000000";
                //动态限流参数
                String dclInitialEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getDclInitialEleMax().intValue(), 4);
                String dclInitialEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getDclInitialEleMin().intValue(), 4);
                String dclWeldEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getDclWeldEleMax().intValue(), 4);
                String dclWeldEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getDclWeldEleMin().intValue(), 4);
                String dclArcEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getDclArcEleMax().intValue(), 4);
                String dclArcEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getDclArcEleMin().intValue(), 4);
                String startDelayTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getStartDelayTime().intValue(), 2);
                String clAmendPeriod = CommonUtils.lengthJoint(sxCO2ProcessIssue.getClAmendPeriod().intValue(), 2);
                String reserved3 = "00000000000000000000";
                //超限报警参数
                String oaInitialEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaInitialEleMax().intValue(), 4);
                String oaInitialEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaInitialEleMin().intValue(), 4);
                String oaInitialVolMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaInitialVolMax().intValue(), 4);
                String oaInitialVolMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaInitialVolMin().intValue(), 4);
                String oaWeldEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaWeldEleMax().intValue(), 4);
                String oaWeldEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaWeldEleMin().intValue(), 4);
                String oaWeldVolMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaWeldVolMax().intValue(), 4);
                String oaWeldVolMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaWeldVolMin().intValue(), 4);
                String oaArcEleMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaArcEleMax().intValue(), 4);
                String oaArcEleMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaArcEleMin().intValue(), 4);
                String oaArcVolMax = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaArcVolMax().intValue(), 4);
                String oaArcVolMin = CommonUtils.lengthJoint(sxCO2ProcessIssue.getOaArcVolMin().intValue(), 4);
                String arcDelayTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getArcDelayTime().intValue(), 2);
                String alarmDelayTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getAlarmDelayTime().intValue(), 2);
                String haltDelayTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getHaltDelayTime().intValue(), 2);
                String haltFreezeTime = CommonUtils.lengthJoint(sxCO2ProcessIssue.getHaltFreezeTime().intValue(), 2);
                String reserved4 = "0000000000000000000000000000000000000000000000000000000000";
                String str = head + datetime + readWriteFlag + channelNo + dataFlag + initialEleMax + initialEleMin +
                        initialVolMax + initialVolMin + initialWireSpeedMax + initialWireSpeedMin + weldEleMax + weldEleMin +
                        weldVolMax + weldVolMin + weldWireSpeedMax + weldWireSpeedMin + arcEleMax + arcEleMin + arcVolMax +
                        arcVolMin + arcWireSpeedMax + arcWireSpeedMin + reserved1 + modeSelect + weldingControl + weldingManner +
                        texture + wireDiameter + gases + wireFeed + checkGasStatus + cutStatus + lockStatus + eleShowSelect +
                        rev + boardThickness + spotWeldingTime + volShowSelect + arcLength + arcCharacter + penetrationControl +
                        beforeAspiratedTime + afterStopGasTime + unitaryDifference + nowChannel + reserved2 + dclInitialEleMax +
                        dclInitialEleMin + dclWeldEleMax + dclWeldEleMin + dclArcEleMax + dclArcEleMin + startDelayTime +
                        clAmendPeriod + reserved3 + oaInitialEleMax + oaInitialEleMin + oaInitialVolMax + oaInitialVolMin +
                        oaWeldEleMax + oaWeldEleMin + oaWeldVolMax + oaWeldVolMin + oaArcEleMax + oaArcEleMin + oaArcVolMax +
                        oaArcVolMin + arcDelayTime + alarmDelayTime + haltDelayTime + haltFreezeTime + reserved4;
                str = str.toUpperCase();
                return str;
            } catch (Exception e) {
                log.error("松下CO2焊机工艺参数解析异常：" + e.getMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * 松下TIG焊机工艺下发参数拼接
     *
     * @param sxTigProcessIssue 松下TIG焊机实体类
     * @return 16进制工艺下发参数
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    public static String sxTigProcessProtocol(SxTIGProcessIssue sxTigProcessIssue) {
        if (null != sxTigProcessIssue) {
            try {
                //头部固定参数
                String head = SxVerificationCode.SX_TIG_PROCESS_ISSUE_HEAD;
                LocalDateTime localDateTime = LocalDateTime.now();
                String year = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("yy")), 2);
                String month = CommonUtils.lengthJoint(localDateTime.getMonthValue(), 2);
                String day = CommonUtils.lengthJoint(localDateTime.getDayOfMonth(), 2);
                String hour = CommonUtils.lengthJoint(localDateTime.getHour(), 2);
                String minute = CommonUtils.lengthJoint(localDateTime.getMinute(), 2);
                String second = CommonUtils.lengthJoint(localDateTime.getSecond(), 2);
                String millisecond = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("SSS")), 4);
                String datetime = year + month + day + hour + minute + second + millisecond;
                String readWriteFlag = "00000000020100";
                String channelNo = CommonUtils.lengthJoint(sxTigProcessIssue.getChannelNo(), 2);
                String dataFlag = "0100";
                //预置参数
                String initialEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getInitialEleMax().intValue(), 4);
                String initialEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getInitialEleMin().intValue(), 4);
                String initialVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getInitialVolMax().intValue(), 4);
                String initialVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getInitialVolMin().intValue(), 4);
                String firstWeldEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getFirstWeldEleMax().intValue(), 4);
                String firstWeldEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getFirstWeldEleMin().intValue(), 4);
                String firstWeldVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getFirstWeldVolMax().intValue(), 4);
                String firstWeldVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getFirstWeldVolMin().intValue(), 4);
                String secondWeldEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getSecondWeldEleMax().intValue(), 4);
                String secondWeldEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getSecondWeldEleMin().intValue(), 4);
                String secondWeldVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getSecondWeldVolMax().intValue(), 4);
                String secondWeldVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getSecondWeldVolMin().intValue(), 4);
                String arcEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getArcEleMax().intValue(), 4);
                String arcEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getArcEleMin().intValue(), 4);
                String arcVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getArcVolMax().intValue(), 4);
                String arcVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getArcVolMin().intValue(), 4);
                String peakWeldEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getPeakWeldEleMax().intValue(), 4);
                String peakWeldEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getPeakWeldEleMin().intValue(), 4);
                String peakWeldVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getPeakWeldVolMax().intValue(), 4);
                String peakWeldVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getPeakWeldVolMin().intValue(), 4);
                String reserved1 = "00000000000000000000";
                //焊接条件
                String weldMethod = CommonUtils.lengthJoint(sxTigProcessIssue.getWeldMethod(), 2);
                String arcHaveNot = CommonUtils.lengthJoint(sxTigProcessIssue.getArcHaveNot(), 2);
                String pulseHaveNot = CommonUtils.lengthJoint(sxTigProcessIssue.getPulseHaveNot(), 2);
                String acWaveform = CommonUtils.lengthJoint(sxTigProcessIssue.getAcWaveform(), 2);
                String pulseRate = CommonUtils.lengthJoint(sxTigProcessIssue.getPulseRate().intValue(), 4);
                String pulseFrequency = CommonUtils.lengthJoint(sxTigProcessIssue.getPulseFrequency().intValue(), 4);
                String cleanWidth = CommonUtils.lengthJoint(sxTigProcessIssue.getCleanWidth().intValue(), 4);
                String acFrequency = CommonUtils.lengthJoint(sxTigProcessIssue.getAcFrequency().intValue(), 4);
                String mixFrequency = CommonUtils.lengthJoint(sxTigProcessIssue.getMixFrequency().intValue(), 4);
                String mixAcRate = CommonUtils.lengthJoint(sxTigProcessIssue.getMixAcRate().intValue(), 4);
                String pulseRadian = CommonUtils.lengthJoint(sxTigProcessIssue.getPulseRadian().intValue(), 4);
                String arcStiffness = CommonUtils.lengthJoint(sxTigProcessIssue.getArcStiffness().intValue(), 4);
                String handWeldThrust = CommonUtils.lengthJoint(sxTigProcessIssue.getHandWeldThrust().intValue(), 4);
                String beforeAspiratedTime = CommonUtils.lengthJoint(sxTigProcessIssue.getBeforeAspiratedTime().intValue(), 4);
                String afterStopGasTime = CommonUtils.lengthJoint(sxTigProcessIssue.getAfterStopGasTime().intValue(), 4);
                String mainWeldRiseTime = CommonUtils.lengthJoint(sxTigProcessIssue.getMainWeldRiseTime().intValue(), 4);
                String mainWeldDeclineTime = CommonUtils.lengthJoint(sxTigProcessIssue.getMainWeldDeclineTime().intValue(), 4);
                String mainWeldRiseRadian = CommonUtils.lengthJoint(sxTigProcessIssue.getMainWeldRiseRadian().intValue(), 4);
                String mainWeldDeclineRadian = CommonUtils.lengthJoint(sxTigProcessIssue.getMainWeldDeclineRadian().intValue(), 4);
                String spotWeldingTime = CommonUtils.lengthJoint(sxTigProcessIssue.getSpotWeldingTime().intValue(), 4);
                String spotWeldIntervalTime = CommonUtils.lengthJoint(sxTigProcessIssue.getSpotWeldIntervalTime().intValue(), 4);
                String spotWeldRiseTime = CommonUtils.lengthJoint(sxTigProcessIssue.getSpotWeldRiseTime().intValue(), 4);
                String spotWeldDeclineTime = CommonUtils.lengthJoint(sxTigProcessIssue.getSpotWeldDeclineTime().intValue(), 4);
                String spotWeldRiseRadian = CommonUtils.lengthJoint(sxTigProcessIssue.getSpotWeldRiseRadian().intValue(), 4);
                String spotWeldDeclineRadian = CommonUtils.lengthJoint(sxTigProcessIssue.getSpotWeldDeclineRadian().intValue(), 4);
                String maxChannel = CommonUtils.lengthJoint(sxTigProcessIssue.getMaxChannel(), 2);
                String nowChannel = CommonUtils.lengthJoint(sxTigProcessIssue.getNowChannel(), 2);
                String handWeldWeldEle = CommonUtils.lengthJoint(sxTigProcessIssue.getHandWeldWeldEle().intValue(), 4);
                String handWeldArcEle = CommonUtils.lengthJoint(sxTigProcessIssue.getHandWeldArcEle().intValue(), 4);
                String reserved2 = "000000000000";
                //动态限流参数
                String dclInitialEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getDclInitialEleMax().intValue(), 4);
                String dclInitialEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getDclInitialEleMin().intValue(), 4);
                String dclWeldEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getDclWeldEleMax().intValue(), 4);
                String dclWeldEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getDclWeldEleMin().intValue(), 4);
                String dclSecondWeldEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getDclSecondWeldEleMax().intValue(), 4);
                String dclSecondWeldEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getDclSecondWeldEleMin().intValue(), 4);
                String dclArcEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getDclArcEleMax().intValue(), 4);
                String dclArcEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getDclArcEleMin().intValue(), 4);
                String startDelayTime = CommonUtils.lengthJoint(sxTigProcessIssue.getStartDelayTime().intValue(), 2);
                String clAmendPeriod = CommonUtils.lengthJoint(sxTigProcessIssue.getClAmendPeriod().intValue(), 2);
                String reserved3 = "00000000000000000000";
                //超限报警参数
                String oaInitialEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaInitialEleMax().intValue(), 4);
                String oaInitialEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaInitialEleMin().intValue(), 4);
                String oaInitialVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaInitialVolMax().intValue(), 4);
                String oaInitialVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaInitialVolMin().intValue(), 4);
                String oaWeldEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaWeldEleMax().intValue(), 4);
                String oaWeldEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaWeldEleMin().intValue(), 4);
                String oaWeldVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaWeldVolMax().intValue(), 4);
                String oaWeldVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaWeldVolMin().intValue(), 4);
                String oaSecondWeldEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaSecondWeldEleMax().intValue(), 4);
                String oaSecondWeldEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaSecondWeldEleMin().intValue(), 4);
                String oaSecondWeldVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaSecondWeldVolMax().intValue(), 4);
                String oaSecondWeldVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaSecondWeldVolMin().intValue(), 4);
                String oaArcEleMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaArcEleMax().intValue(), 4);
                String oaArcEleMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaArcEleMin().intValue(), 4);
                String oaArcVolMax = CommonUtils.lengthJoint(sxTigProcessIssue.getOaArcVolMax().intValue(), 4);
                String oaArcVolMin = CommonUtils.lengthJoint(sxTigProcessIssue.getOaArcVolMin().intValue(), 4);
                String arcDelayTime = CommonUtils.lengthJoint(sxTigProcessIssue.getArcDelayTime().intValue(), 2);
                String alarmDelayTime = CommonUtils.lengthJoint(sxTigProcessIssue.getAlarmDelayTime().intValue(), 2);
                String haltDelayTime = CommonUtils.lengthJoint(sxTigProcessIssue.getHaltDelayTime().intValue(), 2);
                String haltFreezeTime = CommonUtils.lengthJoint(sxTigProcessIssue.getHaltFreezeTime().intValue(), 2);
                String reserved4 = "00000000000000000000";
                String str = head + datetime + readWriteFlag + channelNo + dataFlag + initialEleMax + initialEleMin + initialVolMax +
                        initialVolMin + firstWeldEleMax + firstWeldEleMin + firstWeldVolMax + firstWeldVolMin + secondWeldEleMax +
                        secondWeldEleMin + secondWeldVolMax + secondWeldVolMin + arcEleMax + arcEleMin + arcVolMax + arcVolMin +
                        peakWeldEleMax + peakWeldEleMin + peakWeldVolMax + peakWeldVolMin + reserved1 + weldMethod + arcHaveNot +
                        pulseHaveNot + acWaveform + pulseRate + pulseFrequency + cleanWidth + acFrequency + mixFrequency + mixAcRate +
                        pulseRadian + arcStiffness + handWeldThrust + beforeAspiratedTime + afterStopGasTime + mainWeldRiseTime +
                        mainWeldDeclineTime + mainWeldRiseRadian + mainWeldDeclineRadian + spotWeldingTime + spotWeldIntervalTime +
                        spotWeldRiseTime + spotWeldDeclineTime + spotWeldRiseRadian + spotWeldDeclineRadian + maxChannel + nowChannel +
                        handWeldWeldEle + handWeldArcEle + reserved2 + dclInitialEleMax + dclInitialEleMin + dclWeldEleMax + dclWeldEleMin +
                        dclSecondWeldEleMax + dclSecondWeldEleMin + dclArcEleMax + dclArcEleMin + startDelayTime + clAmendPeriod +
                        reserved3 + oaInitialEleMax + oaInitialEleMin + oaInitialVolMax + oaInitialVolMin + oaWeldEleMax + oaWeldEleMin +
                        oaWeldVolMax + oaWeldVolMin + oaSecondWeldEleMax + oaSecondWeldEleMin + oaSecondWeldVolMax + oaSecondWeldVolMin +
                        oaArcEleMax + oaArcEleMin + oaArcVolMax + oaArcVolMin + arcDelayTime + alarmDelayTime + haltDelayTime +
                        haltFreezeTime + reserved4;
                str = str.toUpperCase();
                return str;
            } catch (Exception e) {
                log.error("松下TIG焊机工艺参数解析异常:" + e.getMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * 松下焊机/通道设定（解锁或锁定）
     *
     * @param sxWeldChannelSetting 松下焊机通道设定实体类
     * @return 16进制字符串
     */
    public static String sxWeldChannelSetProtocol(SxWeldChannelSetting sxWeldChannelSetting) {
        if (null != sxWeldChannelSetting) {
            try {
                String head = SxVerificationCode.SX_WELD_CHANNEL_SET_HEAD;
                LocalDateTime localDateTime = LocalDateTime.now();
                String year = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("yy")), 2);
                String month = CommonUtils.lengthJoint(localDateTime.getMonthValue(), 2);
                String day = CommonUtils.lengthJoint(localDateTime.getDayOfMonth(), 2);
                String hour = CommonUtils.lengthJoint(localDateTime.getHour(), 2);
                String minute = CommonUtils.lengthJoint(localDateTime.getMinute(), 2);
                String second = CommonUtils.lengthJoint(localDateTime.getSecond(), 2);
                String millisecond = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("SSS")), 4);
                String datetime = year + month + day + hour + minute + second + millisecond;
                String reserved1 = "00000000";
                //读写标志：0：主动上传；1：读取；2：设置；3：删除
                String readWriteFlag = CommonUtils.lengthJoint(sxWeldChannelSetting.getReadWriteFlag(), 2);
                String reserved2 = "000000";
                String function = CommonUtils.lengthJoint(sxWeldChannelSetting.getFunction(), 2);
                String channelSelect = CommonUtils.lengthJoint(sxWeldChannelSetting.getChannelSelect(), 2);
                String reserved = "000000000000000000000000";
                String str = head + datetime + reserved1 + readWriteFlag + reserved2 + function + channelSelect + reserved;
                str = str.toUpperCase();
                return str;
            } catch (Exception e) {
                log.error("松下焊机通道设定/读取参数拼接异常：" + e.getLocalizedMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * 松下工艺索取协议拼接
     *
     * @param sxProcessClaim 松下工艺索取/删除实体类
     * @return 16进制字符串
     */
    public static String sxProcessClaimProtocol(SxProcessClaim sxProcessClaim) {
        if (null != sxProcessClaim) {
            try {
                String head = SxVerificationCode.SX_PROCESS_CLAIM_HEAD;
                LocalDateTime localDateTime = LocalDateTime.now();
                String year = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("yy")), 2);
                String month = CommonUtils.lengthJoint(localDateTime.getMonthValue(), 2);
                String day = CommonUtils.lengthJoint(localDateTime.getDayOfMonth(), 2);
                String hour = CommonUtils.lengthJoint(localDateTime.getHour(), 2);
                String minute = CommonUtils.lengthJoint(localDateTime.getMinute(), 2);
                String second = CommonUtils.lengthJoint(localDateTime.getSecond(), 2);
                String millisecond = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("SSS")), 4);
                String datetime = year + month + day + hour + minute + second + millisecond;
                String reserved1 = "00000000";
                //读写标志
                String readWriteFlag = CommonUtils.lengthJoint(sxProcessClaim.getReadWriteFlag(), 2);
                String reserved2 = "0000";
                String channelNo = CommonUtils.lengthJoint(sxProcessClaim.getChannelNo(), 2);
                String reserved3 = "0000000000000000000000000000";
                String str = head + datetime + reserved1 + readWriteFlag + reserved2 + channelNo + reserved3;
                str = str.toUpperCase();
                return str;
            } catch (Exception e) {
                log.error("松下工艺索取/删除协议拼接异常：" + e.getMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * 松下FR2系列通道参数查询/删除协议拼接
     *
     * @param sxChannelParamQuery 松下FR2系列通道参数查询/删除
     * @return 16进制字符串
     */
    public static String sxChannelParamQueryProtocol(SxChannelParamQuery sxChannelParamQuery) {
        if (null != sxChannelParamQuery) {
            String head = SxVerificationCode.SX_CHANNEL_PARAM_HEAD;
            String command = CommonUtils.lengthJoint(sxChannelParamQuery.getCommand(), 2);
            String channel = CommonUtils.lengthJoint(sxChannelParamQuery.getChannel(), 2);
            String reserved = "0000";
            String str = head + command + channel + reserved;
            str = str.toUpperCase();
            return str;
        }
        return null;
    }

    /**
     * 松下FR2系列通道参数下载
     *
     * @param channelParamReplyHave 参数下载实体类
     * @return 16进制字符串
     */
    public static String sxChannelParamReplyHaveProtocol(SxChannelParamReplyHave paramDownload) {
        if (null != paramDownload) {
            String head = SxVerificationCode.SX_CHANNEL_PARAM_DOWNLOAD_HEAD;
            String command = CommonUtils.lengthJoint(paramDownload.getCommand(), 2);
            String channel = CommonUtils.lengthJoint(paramDownload.getChannel(), 2);
            String reserved = "00";
            String channelFlag = CommonUtils.lengthJoint(paramDownload.getChannelFlag(), 2);
            String presetEleMax = CommonUtils.lengthJoint(paramDownload.getPresetEleMax().intValue(), 4);
            String presetVolMax = CommonUtils.lengthJoint(paramDownload.getPresetVolMax().intValue(), 4);
            String presetEleMin = CommonUtils.lengthJoint(paramDownload.getPresetEleMin().intValue(), 4);
            String presetVolMin = CommonUtils.lengthJoint(paramDownload.getPresetVolMin().intValue(), 4);
            String initialEleMax = CommonUtils.lengthJoint(paramDownload.getInitialEleMax().intValue(), 4);
            String initialVolMax = CommonUtils.lengthJoint(paramDownload.getInitialVolMax().intValue(), 4);
            String initialEleMin = CommonUtils.lengthJoint(paramDownload.getInitialEleMin().intValue(), 4);
            String initialVolMin = CommonUtils.lengthJoint(paramDownload.getInitialVolMin().intValue(), 4);
            String arcEleMax = CommonUtils.lengthJoint(paramDownload.getArcEleMax().intValue(), 4);
            String arcVolMax = CommonUtils.lengthJoint(paramDownload.getArcVolMax().intValue(), 4);
            String arcEleMin = CommonUtils.lengthJoint(paramDownload.getArcEleMin().intValue(), 4);
            String arcVolMin = CommonUtils.lengthJoint(paramDownload.getArcVolMin().intValue(), 4);
            String texture = CommonUtils.lengthJoint(paramDownload.getTexture(), 2);
            String wireDiameter = CommonUtils.lengthJoint(paramDownload.getWireDiameter(), 2);
            String gases = CommonUtils.lengthJoint(paramDownload.getGases(), 2);
            String weldingControl = CommonUtils.lengthJoint(paramDownload.getWeldingControl(), 2);
            String pulseHaveNot = CommonUtils.lengthJoint(paramDownload.getPulseHaveNot(), 2);
            String spotWeldingTime = CommonUtils.lengthJoint(paramDownload.getSpotWeldingTime().intValue(), 4);
            String unitaryDifference = CommonUtils.lengthJoint(paramDownload.getUnitaryDifference(), 2);
            String dryExtendLength = CommonUtils.lengthJoint(paramDownload.getDryExtendLength(), 2);
            String reserved1 = "000000";
            String weldMax = CommonUtils.lengthJoint(paramDownload.getWeldMax().intValue(), 4);
            String weldMin = CommonUtils.lengthJoint(paramDownload.getWeldMin().intValue(), 4);
            String initialMax = CommonUtils.lengthJoint(paramDownload.getInitialMax().intValue(), 4);
            String initialMin = CommonUtils.lengthJoint(paramDownload.getInitialMin().intValue(), 4);
            String arcMax = CommonUtils.lengthJoint(paramDownload.getArcMax().intValue(), 4);
            String arcMin = CommonUtils.lengthJoint(paramDownload.getArcMin().intValue(), 4);
            String delayTime = CommonUtils.lengthJoint(paramDownload.getDelayTime().intValue(), 2);
            String amendPeriod = CommonUtils.lengthJoint(paramDownload.getAmendPeriod().intValue(), 2);
            String presetEleAlarmMax = CommonUtils.lengthJoint(paramDownload.getPresetEleAlarmMax().intValue(), 4);
            String presetVolAlarmMax = CommonUtils.lengthJoint(paramDownload.getPresetVolAlarmMax().intValue(), 4);
            String presetEleAlarmMin = CommonUtils.lengthJoint(paramDownload.getPresetEleAlarmMin().intValue(), 4);
            String presetVolAlarmMin = CommonUtils.lengthJoint(paramDownload.getPresetVolAlarmMin().intValue(), 4);
            String initialEleAlarmMax = CommonUtils.lengthJoint(paramDownload.getInitialEleAlarmMax().intValue(), 4);
            String initialVolAlarmMax = CommonUtils.lengthJoint(paramDownload.getInitialVolAlarmMax().intValue(), 4);
            String initialEleAlarmMin = CommonUtils.lengthJoint(paramDownload.getInitialEleAlarmMin().intValue(), 4);
            String initialVolAlarmMin = CommonUtils.lengthJoint(paramDownload.getInitialVolAlarmMin().intValue(), 4);
            String arcEleAlarmMax = CommonUtils.lengthJoint(paramDownload.getArcEleAlarmMax().intValue(), 4);
            String arcVolAlarmMax = CommonUtils.lengthJoint(paramDownload.getArcVolAlarmMax().intValue(), 4);
            String arcEleAlarmMin = CommonUtils.lengthJoint(paramDownload.getArcEleAlarmMin().intValue(), 4);
            String arcVolAlarmMin = CommonUtils.lengthJoint(paramDownload.getArcVolAlarmMin().intValue(), 4);
            String arcDelayTime = CommonUtils.lengthJoint(paramDownload.getArcDelayTime().intValue(), 2);
            String alarmDelayTime = CommonUtils.lengthJoint(paramDownload.getAlarmDelayTime().intValue(), 2);
            String alarmHaltTime = CommonUtils.lengthJoint(paramDownload.getAlarmHaltTime().intValue(), 2);
            String reserved2 = "00";
            String alarmFlag = CommonUtils.lengthJoint(paramDownload.getAlarmFlag(), 2);
            String reserved3 = "00000000";
            String str = head + command + channel + reserved + channelFlag + presetEleMax + presetVolMax + presetEleMin +
                    presetVolMin + initialEleMax + initialVolMax + initialEleMin + initialVolMin + arcEleMax + arcVolMax +
                    arcEleMin + arcVolMin + texture + wireDiameter + gases + weldingControl + pulseHaveNot + spotWeldingTime +
                    unitaryDifference + dryExtendLength + reserved1 + weldMax + weldMin + initialMax + initialMin + arcMax +
                    arcMin + delayTime + amendPeriod + presetEleAlarmMax + presetVolAlarmMax + presetEleAlarmMin + presetVolAlarmMin +
                    initialEleAlarmMax + initialVolAlarmMax + initialEleAlarmMin + initialVolAlarmMin + arcEleAlarmMax +
                    arcVolAlarmMax + arcEleAlarmMin + arcVolAlarmMin + arcDelayTime + alarmDelayTime + alarmHaltTime + reserved2 +
                    alarmFlag + reserved3;
            str = str.toUpperCase();
            return str;
        }
        return null;
    }

    /**
     * 松下AT3系列参数下载协议拼接
     *
     * @param at3ParamDownload 实体类
     * @return 16进制字符串
     */
    public static String At3ParamDownloadProtocol(At3ParamDownload at3ParamDownload) {
        if (null != at3ParamDownload) {
            String head = SxVerificationCode.SX_AT3_PARAM_DOWNLOAD_HEAD;
            String command = CommonUtils.lengthJoint(at3ParamDownload.getCommand(), 2);
            String channel = CommonUtils.lengthJoint(at3ParamDownload.getChannel(), 2);
            String reserved = "00";
            String channelFlag = CommonUtils.lengthJoint(at3ParamDownload.getChannelFlag(), 2);
            String presetEleMax = CommonUtils.lengthJoint(at3ParamDownload.getPresetEleMax().intValue(), 4);
            String presetVolMax = CommonUtils.lengthJoint(at3ParamDownload.getPresetVolMax().intValue(), 4);
            String presetEleMin = CommonUtils.lengthJoint(at3ParamDownload.getPresetEleMin().intValue(), 4);
            String presetVolMin = CommonUtils.lengthJoint(at3ParamDownload.getPresetVolMin().intValue(), 4);
            String eleAlarmMax = CommonUtils.lengthJoint(at3ParamDownload.getEleAlarmMax().intValue(), 4);
            String volAlarmMax = CommonUtils.lengthJoint(at3ParamDownload.getVolAlarmMax().intValue(), 4);
            String eleAlarmMin = CommonUtils.lengthJoint(at3ParamDownload.getEleAlarmMin().intValue(), 4);
            String volAlarmMin = CommonUtils.lengthJoint(at3ParamDownload.getVolAlarmMin().intValue(), 4);
            String alarmDelayTime = CommonUtils.lengthJoint(at3ParamDownload.getAlarmDelayTime().intValue(), 2);
            String alarmHaltTime = CommonUtils.lengthJoint(at3ParamDownload.getAlarmHaltTime().intValue(), 2);
            String reserved1 = "0000";
            String str = head + command + channel + reserved + channelFlag + presetEleMax + presetVolMax + presetEleMin +
                    presetVolMin + eleAlarmMax + volAlarmMax + eleAlarmMin + volAlarmMin + alarmDelayTime + alarmHaltTime + reserved1;
            str = str.toUpperCase();
            return str;
        }
        return null;
    }

}
