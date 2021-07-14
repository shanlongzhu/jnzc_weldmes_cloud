package com.shth.das.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.shth.das.common.CommonDbData;
import com.shth.das.common.TopicEnum;
import com.shth.das.mqtt.EmqMqttClient;
import com.shth.das.netty.NettyServerHandler;
import com.shth.das.pojo.*;
import com.shth.das.sys.rtdata.service.RtDataService;
import com.shth.das.sys.weldmesdb.service.WeldOnOffTimeService;
import com.shth.das.util.BeanContext;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 江南项目数据解析类
 */
@Slf4j
public class JnRtDataProtocol {

    /**
     * 数据累积后插入到数据库
     */
    private static final List<JNRtDataDB> JN_RT_DATA_LIST = new LinkedList<>();

    /**
     * 采集盒IP地址盒采集编号绑定
     */
    public static void gatherNoIpBinding(String clientIp, String gatherNo) {
        //判断map集合是否有，没有则新增
        if (!NettyServerHandler.CLIENT_IP_GATHER_NO_MAP.containsKey(clientIp)) {
            NettyServerHandler.CLIENT_IP_GATHER_NO_MAP.put(clientIp, gatherNo);
            //新增设备开机时间
            WeldOnOffTimeService onOffTimeService = BeanContext.getBean(WeldOnOffTimeService.class);
            WeldOnOffTime onOffTime = new WeldOnOffTime();
            onOffTime.setGatherNo(gatherNo);
            onOffTime.setStartTime(DateTimeUtils.getNowDateTime());
            onOffTime.setMachineId(getMachineIdByGatherNo(gatherNo));
            onOffTime.setMachineType(0);
            onOffTimeService.insertWeldOnOffTime(onOffTime);
        }
    }

    /**
     * 江南版实时数据协议解码处理
     */
    public static Map<String, Object> jnRtDataDecoderManage(String str) {
        Map<String, Object> map = new HashMap<>(8);
        /*
        江南版实时数据协议解析
         */
        if (str.length() == 282) {
            //存数据库
            List<JNRtDataDB> jnRtDataDbs = JnRtDataProtocol.jnRtDataAnalysis(str);
            //发送前端
            List<JNRtDataUI> jnRtDataUis = JnRtDataProtocol.jnRtDataUiAnalysis(str);
            map.put("JNRtDataDB", jnRtDataDbs);
            map.put("JNRtDataUI", jnRtDataUis);
        }
        /*
        下发规范返回
         */
        if (str.length() == 24) {
            JNProcessIssueReturn issueReturn = JnRtDataProtocol.jnIssueReturnAnalysis(str);
            map.put("JNProcessIssueReturn", issueReturn);
        }
        /*
        索取规范返回
         */
        if (str.length() == 112) {
            JNProcessClaimReturn claimReturn = JnRtDataProtocol.jnClaimReturnAnalysis(str);
            map.put("JNProcessClaimReturn", claimReturn);
        }
        /*
        密码返回
        控制命令返回
         */
        if (str.length() == 22) {
            //密码返回
            if ("7E".equals(str.substring(0, 2)) && "53".equals(str.substring(10, 12)) && "7D".equals(str.substring(20, 22))) {
                JNPasswordReturn passwordReturn = JnRtDataProtocol.jnPasswordReturnAnalysis(str);
                map.put("JNPasswordReturn", passwordReturn);
            }
            //控制命令返回
            if ("7E".equals(str.substring(0, 2)) && "54".equals(str.substring(10, 12)) && "7D".equals(str.substring(20, 22))) {
                JNCommandReturn commandReturn = JnRtDataProtocol.jnCommandReturnAnalysis(str);
                map.put("JNCommandReturn", commandReturn);
            }
        }
        return map;
    }

    /**
     * 实时数据处理
     */
    @SuppressWarnings("unchecked")
    public static void jnRtDataManage(String clientIp, Object msg) {
        Map<String, Object> map = (Map<String, Object>) msg;
        if (map.size() > 0) {
            //实时数据发送到前端
            if (map.containsKey("JNRtDataUI")) {
                List<JNRtDataUI> jnRtDataUis = (List<JNRtDataUI>) map.get("JNRtDataUI");
                String gatherNo = jnRtDataUis.get(0).getGatherNo();
                gatherNoIpBinding(clientIp, gatherNo);
                //集合转字符串[消除对同一对象的循环引用]
                String message = JSON.toJSONString(jnRtDataUis, SerializerFeature.DisableCircularReferenceDetect);
                //通过mqtt发送到服务端
                EmqMqttClient.publishMessage(TopicEnum.rtcdata.name(), message, 0);
            }
            //实时数据存数据库
            if (map.containsKey("JNRtDataDB")) {
                List<JNRtDataDB> list = (List<JNRtDataDB>) map.get("JNRtDataDB");
                if (CommonUtils.isNotEmpty(list)) {
                    synchronized (JN_RT_DATA_LIST) {
                        JN_RT_DATA_LIST.addAll(list);
                        if (JN_RT_DATA_LIST.size() >= 100) {
                            RtDataService rtDataService = BeanContext.getBean(RtDataService.class);
                            rtDataService.insertRtDataList(JN_RT_DATA_LIST);
                            JN_RT_DATA_LIST.clear();
                        }
                    }
                }
            }
            //工艺下发返回
            if (map.containsKey("JNProcessIssueReturn")) {
                JNProcessIssueReturn processIssueReturn = (JNProcessIssueReturn) map.get("JNProcessIssueReturn");
                if (null != processIssueReturn) {
                    //Java类转JSON字符串
                    String message = JSON.toJSONString(processIssueReturn);
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(TopicEnum.processIssueReturn.name(), message, 0);
                }
            }
            //工艺索取返回
            if (map.containsKey("JNProcessClaimReturn")) {
                JNProcessClaimReturn processClaimReturn = (JNProcessClaimReturn) map.get("JNProcessClaimReturn");
                if (null != processClaimReturn) {
                    String message = JSON.toJSONString(processClaimReturn);
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(TopicEnum.processClaimReturn.name(), message, 0);
                }
            }
            //密码返回
            if (map.containsKey("JNPasswordReturn")) {
                JNPasswordReturn passwordReturn = (JNPasswordReturn) map.get("JNPasswordReturn");
                if (null != passwordReturn) {
                    String message = JSON.toJSONString(passwordReturn);
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(TopicEnum.passwordReturn.name(), message, 0);
                }
            }
            //控制命令返回
            if (map.containsKey("JNCommandReturn")) {
                JNCommandReturn commandReturn = (JNCommandReturn) map.get("JNCommandReturn");
                if (null != commandReturn) {
                    String message = JSON.toJSONString(commandReturn);
                    //通过mqtt发送到服务端
                    EmqMqttClient.publishMessage(TopicEnum.commandReturn.name(), message, 0);
                }
            }
        }
    }

    /**
     * 实时数据解析通过mq发前端
     *
     * @param str 需要解析的16进制字符串
     * @return 返回一个集合对象
     */
    public static List<JNRtDataUI> jnRtDataUiAnalysis(String str) {
        List<JNRtDataUI> rtData = new ArrayList<>();
        try {
            if (CommonUtils.isNotEmpty(str) && str.length() == 282) {
                str = str.toUpperCase();
                if ("7E".equals(str.substring(0, 2)) && "22".equals(str.substring(10, 12)) && "7D".equals(str.substring(280, 282))) {
                    JNRtDataUI data = new JNRtDataUI();
                    //采集模块编号
                    data.setGatherNo(Integer.valueOf(str.substring(14, 18), 16).toString());
                    for (int a = 0; a < 239; a += 80) {
                        //电流
                        data.setElectricity(BigDecimal.valueOf(Integer.valueOf(str.substring(50 + a, 54 + a), 16)));
                        //电压
                        data.setVoltage(BigDecimal.valueOf(Integer.valueOf(str.substring(54 + a, 58 + a), 16))
                                .divide(new BigDecimal("10"), 1, BigDecimal.ROUND_HALF_UP));
                        //焊机状态
                        data.setWeldStatus(Integer.valueOf(str.substring(78 + a, 80 + a), 16));
                        rtData.add(data);
                    }
                }
            }
        } catch (Exception e) {
            log.error("江南实时数据协议解析异常：{}", e.getMessage());
        }
        return rtData;
    }

    /**
     * 实时数据协议解析存数据库
     *
     * @param str 需要解析的16进制字符串
     * @return 返回一个集合对象
     */
    public static List<JNRtDataDB> jnRtDataAnalysis(String str) {
        List<JNRtDataDB> rtdata = new LinkedList<>();
        try {
            if (CommonUtils.isNotEmpty(str) && str.length() == 282) {
                str = str.toUpperCase();
                if ("7E".equals(str.substring(0, 2)) && "7D".equals(str.substring(280, 282))) {
                    JNRtDataDB data = new JNRtDataDB();
                    //焊机型号
                    data.setWeldModel(Integer.valueOf(str.substring(12, 14), 16));
                    //采集模块编号
                    data.setGatherNo(Integer.valueOf(str.substring(14, 18), 16).toString());
                    //焊工号
                    data.setWelderNo(Integer.valueOf(str.substring(34, 38), 16).toString());
                    //当前日期：yyyy-MM-dd
                    String nowDate = DateTimeUtils.getNowDate();
                    //当前系统时间 yyyy-MM-dd HH:mm:ss
                    String nowDateTime = DateTimeUtils.getNowDateTime();
                    //创建时间
                    data.setCreateTime(nowDateTime);
                    //采集模块信息查询并绑定
                    List<GatherModel> gatherList = CommonDbData.GATHER_LIST;
                    if (CommonUtils.isNotEmpty(gatherList)) {
                        for (GatherModel gather : gatherList) {
                            if (Integer.valueOf(data.getGatherNo()).equals(Integer.valueOf(gather.getGatherNo()))) {
                                data.setGatherId(gather.getId());
                                data.setGatherDeptId(gather.getDeptId());
                                break;
                            }
                        }
                    }
                    //焊机信息查询并绑定
                    List<WeldModel> weldList = CommonDbData.WELD_LIST;
                    if (CommonUtils.isNotEmpty(weldList)) {
                        for (WeldModel weld : weldList) {
                            if (Integer.valueOf(data.getGatherNo()).equals(Integer.valueOf(weld.getGatherNo()))) {
                                data.setMachineId(weld.getId());
                                data.setMachineNo(weld.getMachineNo());
                                data.setMachineDeptId(weld.getDeptId());
                                break;
                            }
                        }
                    }
                    //焊工信息查询并绑定
                    List<WelderModel> welderList = CommonDbData.WELDER_LIST;
                    if (CommonUtils.isNotEmpty(welderList)) {
                        for (WelderModel welder : welderList) {
                            if (Integer.valueOf(data.getWelderNo()).equals(Integer.valueOf(welder.getWelderNo()))) {
                                data.setWelderId(welder.getId());
                                data.setWelderName(welder.getWelderName());
                                data.setWelderDeptId(welder.getDeptId());
                                break;
                            }
                        }
                    }
                    //任务信息查询并绑定
                    List<TaskModel> taskList = CommonDbData.TASK_LIST;
                    if (CommonUtils.isNotEmpty(taskList)) {
                        for (TaskModel task : taskList) {
                            if (CommonUtils.isNotEmpty(task.getWelderNo()) && Integer.valueOf(data.getWelderNo()).equals(Integer.valueOf(task.getWelderNo()))) {
                                data.setTaskId(task.getId());
                                data.setTaskNo(task.getTaskNo());
                                data.setTaskName(task.getTaskName());
                                break;
                            }
                        }
                    }
                    for (int a = 0; a < 239; a += 80) {
                        String year = CommonUtils.hexToDecLengthJoint(str.substring(38 + a, 40 + a), 2);
                        String month = CommonUtils.hexToDecLengthJoint(str.substring(40 + a, 42 + a), 2);
                        String day = CommonUtils.hexToDecLengthJoint(str.substring(42 + a, 44 + a), 2);
                        String hour = CommonUtils.hexToDecLengthJoint(str.substring(44 + a, 46 + a), 2);
                        String minute = CommonUtils.hexToDecLengthJoint(str.substring(46 + a, 48 + a), 2);
                        String second = CommonUtils.hexToDecLengthJoint(str.substring(48 + a, 50 + a), 2);
                        String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        String weldDate = LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.TODAY_DATE);
                        String weldDateTime = LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME);
                        //判断实时数据是否当天数据，true：保存，false；赋值系统时间
                        if (weldDate.equals(nowDate)) {
                            data.setWeldTime(weldDateTime);
                        } else {
                            //焊机时间
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
                        rtdata.add(data);
                    }
                }
            }
        } catch (Exception e) {
            log.error("江南实时数据协议解析异常：{}", e.getMessage());
        }
        return rtdata;
    }

    /**
     * 下发规范字符串拼接
     *
     * @param model 下发规范实体类
     * @return 返回解析完成的16进制字符串
     */
    public static String jnIssueProtocol(JNProcessIssue model) {
        String head = JNProcessIssue.Flag.HEAD.value;
        String order = JNProcessIssue.Flag.ORDER.value;
        String foot = JNProcessIssue.Flag.FOOT.value;
        String gatherNo = CommonUtils.lengthJoint(model.getGatherNo(), 4);//采集编号：0001
        String channelNo = CommonUtils.lengthJoint(model.getChannelNo(), 2);//通道号：01
        String spotWeldTime = CommonUtils.lengthJoint(model.getSpotWeldTime().toString(), 4);//点焊时间：30
        String preflowTime = CommonUtils.lengthJoint(model.getPreflowTime().toString(), 4);//提前送气时间：1
        String initialEle = CommonUtils.lengthJoint(model.getInitialEle().toString(), 4);//初期电流:100
        String initialVol = CommonUtils.lengthJoint(model.getInitialVol().toString(), 4);//初期电压:190
        String initialVolUnitary = CommonUtils.lengthJoint(model.getInitialVolUnitary().toString(), 4);//初期电压一元:0
        String weldElectricity = CommonUtils.lengthJoint(model.getWeldElectricity().toString(), 4);//焊接电流:100
        String weldVoltage = CommonUtils.lengthJoint(model.getWeldVoltage().toString(), 4);//焊接电压:190
        String weldVoltageUnitary = CommonUtils.lengthJoint(model.getWeldVoltageUnitary().toString(), 4);//焊接电压一元:0
        String extinguishArcEle = CommonUtils.lengthJoint(model.getExtinguishArcEle().toString(), 4);//收弧电流:100
        String extinguishArcVol = CommonUtils.lengthJoint(model.getExtinguishArcVol().toString(), 4);//收弧电压:190
        String extinguishArcVolUnitary = CommonUtils.lengthJoint(model.getExtinguishArcVolUnitary().toString(), 4);//收弧电压一元:0
        String hysteresisAspirated = CommonUtils.lengthJoint(model.getHysteresisAspirated().toString(), 4);//滞后送气:1
        String arcPeculiarity = CommonUtils.lengthJoint(model.getArcPeculiarity().toString(), 4);//电弧特性:0
        String gases = CommonUtils.lengthJoint(model.getGases().toString(), 2);//气体:0
        String wireDiameter = CommonUtils.lengthJoint(model.getWireDiameter().toString(), 2);//焊丝直径:12
        String wireMaterials = CommonUtils.lengthJoint(model.getWireMaterials(), 2);//焊丝材料:0
        String weldProcess = CommonUtils.lengthJoint(model.getWeldProcess(), 2);//焊接过程:0
        String controlInfo = CommonUtils.lengthJoint(model.getControlInfo(), 4);//控制信息:229
        String weldEleAdjust = CommonUtils.lengthJoint(model.getWeldEleAdjust().toString(), 2);//焊接电流微调:0
        String weldVolAdjust = CommonUtils.lengthJoint(model.getWeldVolAdjust().toString(), 2);//焊接电压微调:0
        String extinguishArcEleAdjust = CommonUtils.lengthJoint(model.getExtinguishArcEleAdjust().toString(), 2);//收弧电流微调:0
        String extinguishArcVolAdjust = CommonUtils.lengthJoint(model.getExtinguishArcVolAdjust().toString(), 2);//收弧电压微调:0
        String alarmsElectricityMax = CommonUtils.lengthJoint(model.getAlarmsElectricityMax().toString(), 4);//报警电流上限:0
        String alarmsElectricityMin = CommonUtils.lengthJoint(model.getAlarmsElectricityMin().toString(), 4);//报警电流下限:0
        String alarmsVoltageMax = CommonUtils.lengthJoint(model.getAlarmsVoltageMax().toString(), 4);//报警电压上限:0
        String alarmsVoltageMin = CommonUtils.lengthJoint(model.getAlarmsVoltageMin().toString(), 4);//报警电压下限:0
        String str = head + order + gatherNo + channelNo + spotWeldTime + preflowTime + initialEle + initialVol + initialVolUnitary + weldElectricity +
                weldVoltage + weldVoltageUnitary + extinguishArcEle + extinguishArcVol + extinguishArcVolUnitary + hysteresisAspirated + arcPeculiarity +
                gases + wireDiameter + wireMaterials + weldProcess + controlInfo + weldEleAdjust + weldVolAdjust + extinguishArcEleAdjust + extinguishArcVolAdjust +
                alarmsElectricityMax + alarmsElectricityMin + alarmsVoltageMax + alarmsVoltageMin + foot;
        str = str.toUpperCase(); //字母全部转为大写
        return str;
    }

    /**
     * 下发返回协议解析
     *
     * @param str 16进制字符串
     * @return 下发规范返回类
     */
    public static JNProcessIssueReturn jnIssueReturnAnalysis(String str) {
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

    /**
     * 索取返回协议解析
     *
     * @param str 16进制字符串
     * @return 索取返回实体类
     */
    public static JNProcessClaimReturn jnClaimReturnAnalysis(String str) {
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
        String head = JNPasswordIssue.Flag.HEAD.value;
        String order = JNPasswordIssue.Flag.ORDER.value;
        String foot = JNPasswordIssue.Flag.FOOT.value;
        String gatherNo = CommonUtils.lengthJoint(jnPasswordIssue.getGatherNo(), 4);
        String password = CommonUtils.lengthJoint(jnPasswordIssue.getPassword(), 4);
        String str = head + order + gatherNo + password + foot;
        str = str.toUpperCase();
        return str;
    }

    /**
     * 控制命令下发字符串拼接
     *
     * @param jnCommandIssue 控制命令下发实体类
     * @return 16进制字符串
     */
    public static String jnCommandProtocol(JNCommandIssue jnCommandIssue) {
        String head = JNCommandIssue.Flag.HEAD.value;
        String order = JNCommandIssue.Flag.ORDER.value;
        String foot = JNCommandIssue.Flag.FOOT.value;
        String gatherNo = CommonUtils.lengthJoint(jnCommandIssue.getGatherNo(), 4);
        String command = CommonUtils.lengthJoint(jnCommandIssue.getCommand(), 2);
        String str = head + order + gatherNo + command + foot;
        str = str.toUpperCase();
        return str;
    }

    /**
     * 密码返回协议解析
     *
     * @param str 16进制字符串
     * @return 密码返回实体类
     */
    public static JNPasswordReturn jnPasswordReturnAnalysis(String str) {
        JNPasswordReturn passwordReturn = new JNPasswordReturn();
        if ("7E".equals(str.substring(0, 2)) && "53".equals(str.substring(10, 12)) && "7D".equals(str.substring(20, 22))) {
            passwordReturn.setGatherNo(Integer.valueOf(str.substring(12, 16), 16).toString());
            passwordReturn.setFlag(Integer.valueOf(str.substring(16, 18)));
        }
        return passwordReturn;
    }

    /**
     * 控制命令返回协议解析
     *
     * @param str 16进制字符串
     * @return 控制命令返回实体类
     */
    public static JNCommandReturn jnCommandReturnAnalysis(String str) {
        JNCommandReturn commandReturn = new JNCommandReturn();
        if ("7E".equals(str.substring(0, 2)) && "54".equals(str.substring(10, 12)) && "7D".equals(str.substring(20, 22))) {
            commandReturn.setGatherNo(Integer.valueOf(str.substring(12, 16), 16).toString());
            commandReturn.setFlag(Integer.valueOf(str.substring(16, 18)));
        }
        return commandReturn;
    }

    /**
     * 根据采集编号查询焊机ID
     *
     * @param gatherNo 采集编号
     * @return 焊机ID
     */
    public static BigInteger getMachineIdByGatherNo(String gatherNo) {
        List<WeldModel> weldList = CommonDbData.WELD_LIST;
        if (CommonUtils.isNotEmpty(weldList) && CommonUtils.isNotEmpty(gatherNo)) {
            for (WeldModel weld : weldList) {
                if (gatherNo.equals(weld.getGatherNo())) {
                    return weld.getId();
                }
            }
        }
        return BigInteger.ZERO;
    }

    /**
     * 江南OTC设备关机数据处理
     */
    public static void jnWeldOffDataManage(String clientIp) {
        //有客户端终止连接则发送关机数据到mq，刷新实时界面
        if (NettyServerHandler.CLIENT_IP_GATHER_NO_MAP.containsKey(clientIp)) {
            List<JNRtDataUI> dataList = new ArrayList<>();
            JNRtDataUI jnRtDataUi = new JNRtDataUI();
            //采集编号
            String gatherNo = NettyServerHandler.CLIENT_IP_GATHER_NO_MAP.get(clientIp);
            jnRtDataUi.setGatherNo(gatherNo);
            //-1 为关机
            jnRtDataUi.setWeldStatus(-1);
            jnRtDataUi.setElectricity(BigDecimal.ZERO);
            jnRtDataUi.setVoltage(BigDecimal.ZERO);
            dataList.add(jnRtDataUi);
            String dataArray = JSONArray.toJSONString(dataList);
            EmqMqttClient.publishMessage(TopicEnum.rtcdata.name(), dataArray, 0);
            log.info("OTC关机：" + "：{}", TopicEnum.rtcdata.name() + ":" + dataArray);
            NettyServerHandler.CLIENT_IP_GATHER_NO_MAP.remove(clientIp);
            //新增设备关机时间
            WeldOnOffTimeService onOffTimeService = BeanContext.getBean(WeldOnOffTimeService.class);
            WeldOnOffTime onOffTime = new WeldOnOffTime();
            onOffTime.setGatherNo(gatherNo);
            onOffTime.setEndTime(DateTimeUtils.getNowDateTime());
            onOffTime.setMachineId(JnRtDataProtocol.getMachineIdByGatherNo(gatherNo));
            onOffTime.setMachineType(0);
            onOffTimeService.insertWeldOnOffTime(onOffTime);
        }
    }

    /**
     * 根据采集编号查找采集盒IP地址
     *
     * @param gatherNo 采集编号
     * @return 返回采集盒IP地址
     */
    public static String getClientIpByGatherNo(String gatherNo) {
        String clientIp = "";
        if (CommonUtils.isNotEmpty(gatherNo)) {
            synchronized (NettyServerHandler.CLIENT_IP_GATHER_NO_MAP) {
                Set<Map.Entry<String, String>> entries = NettyServerHandler.CLIENT_IP_GATHER_NO_MAP.entrySet();
                //循环查找value（采集编号）的key（IP地址）
                for (Map.Entry<String, String> entry : entries) {
                    //采集编号
                    String gatherno = entry.getValue();
                    if (Integer.valueOf(gatherNo).equals(Integer.valueOf(gatherno))) {
                        //采集盒IP地址
                        clientIp = entry.getKey();
                        break;
                    }
                }
            }
        }
        return clientIp;
    }

}