package com.shth.das.business.dataup.otc;

import com.alibaba.fastjson2.JSON;
import com.shth.das.business.dataup.base.BaseAnalysis;
import com.shth.das.business.dataup.base.DecodeObjectFactory;
import com.shth.das.codeparam.HandlerParam;
import com.shth.das.codeparam.JnOtcDecoderParam;
import com.shth.das.common.CommonFunction;
import com.shth.das.common.CommonList;
import com.shth.das.common.CommonMap;
import com.shth.das.pojo.db.GatherModel;
import com.shth.das.pojo.db.TaskClaimIssue;
import com.shth.das.pojo.db.WeldModel;
import com.shth.das.pojo.jnotc.*;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description: OTC协议解析
 * @author: Shan Long
 * @create: 2021-08-08
 */
@Slf4j
public class JnOtcProtocolAnalysis extends BaseAnalysis {

    /**
     * 保存字符串长度和方法映射关系
     * Function<T,R>,T:传入参数，R：返回参数
     * K：控制命令
     * V：解析方法
     */
    private final Map<String, Function<JnOtcDecoderParam, HandlerParam>> decoderMapping = new HashMap<>();

    private final JnOtcDecoderParam jnOtcDecoderParam = new JnOtcDecoderParam();

    private final DecodeObjectFactory factory = new DecodeObjectFactory();

    public JnOtcProtocolAnalysis() {
        init();
    }

    private void init() {
        //OTC（1.0）实时数据解析
        this.decoderMapping.put("22", this::jnOtcRtDataAnalysis);
        //OTC（1.0）工艺下发返回解析
        this.decoderMapping.put("52", this::otcIssueReturnAnalysis);
        //OTC（1.0）索取返回协议解析
        this.decoderMapping.put("56", this::otcClaimReturnAnalysis);
        //密码返回
        this.decoderMapping.put("53", this::otcPasswordReturnAnalysis);
        //控制命令返回
        this.decoderMapping.put("54", this::otcCommandReturnAnalysis);
        //锁焊机指令返回
        this.decoderMapping.put("18", this::otcLockMachineReturn);
        //解锁焊机指令返回
        this.decoderMapping.put("19", this::otcUnLockMachineReturn);
        //程序包路径下发返回
        this.decoderMapping.put("11", this::otcProgramPathIssueReturnAnalysis);
    }

    @Override
    public HandlerParam protocolAnalysis(ChannelHandlerContext ctx, String str) {
        if (!str.startsWith("7E") || !str.endsWith("7D")) {
            return null;
        }
        //控制命令
        String ctlCommand = str.substring(10, 12);
        if (this.decoderMapping.containsKey(ctlCommand)) {
            String clientIp = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
            this.jnOtcDecoderParam.setStr(str);
            this.jnOtcDecoderParam.setClientIp(clientIp);
            HandlerParam handlerParam = this.decoderMapping.get(ctlCommand).apply(this.jnOtcDecoderParam);
            if (ObjectUtils.isNotEmpty(handlerParam)) {
                handlerParam.setCtlCommand(ctlCommand);
            }
            return handlerParam;
        }
        return null;
    }

    private HandlerParam otcPasswordReturnAnalysis(JnOtcDecoderParam jnOtcDecoderParam) {
        if (null != jnOtcDecoderParam) {
            try {
                HandlerParam handlerParam = factory.getObject(HandlerParam.class);
                Map<String, String> map = factory.getMap();
                String str = jnOtcDecoderParam.getStr();
                JNPasswordReturn passwordReturn = jnPasswordReturnAnalysis(str);
                if (null != passwordReturn) {
                    map.put(JNPasswordReturn.class.getSimpleName(), JSON.toJSONString(passwordReturn));
                }
                handlerParam.setValue(map);
                return handlerParam;
            } catch (Exception e) {
                log.error("OTC1.0密码返回异常：{}", e.getMessage());
            }
        }
        return null;
    }

    private HandlerParam otcCommandReturnAnalysis(JnOtcDecoderParam jnOtcDecoderParam) {
        if (null != jnOtcDecoderParam) {
            try {
                HandlerParam handlerParam = factory.getObject(HandlerParam.class);
                Map<String, String> map = factory.getMap();
                String str = jnOtcDecoderParam.getStr();
                JNCommandReturn commandReturn = jnCommandReturnAnalysis(str);
                if (null != commandReturn) {
                    map.put(JNCommandReturn.class.getSimpleName(), JSON.toJSONString(commandReturn));
                }
                handlerParam.setValue(map);
                return handlerParam;
            } catch (Exception e) {
                log.error("OTC1.0控制命令返回异常：{}", e.getMessage());
            }
        }
        return null;
    }

    private HandlerParam otcLockMachineReturn(JnOtcDecoderParam jnOtcDecoderParam) {
        if (null != jnOtcDecoderParam) {
            try {
                HandlerParam handlerParam = factory.getObject(HandlerParam.class);
                Map<String, String> map = factory.getMap();
                String str = jnOtcDecoderParam.getStr();
                JnLockMachineReturn jnLockMachineReturn = jnLockMachineReturnAnalysis(str);
                if (null != jnLockMachineReturn) {
                    map.put(JnLockMachineReturn.class.getSimpleName(), JSON.toJSONString(jnLockMachineReturn));
                }
                handlerParam.setValue(map);
                return handlerParam;
            } catch (Exception e) {
                log.error("OTC1.0锁焊机指令返回异常：{}", e.getMessage());
            }
        }
        return null;
    }

    private HandlerParam otcUnLockMachineReturn(JnOtcDecoderParam jnOtcDecoderParam) {
        if (null != jnOtcDecoderParam) {
            try {
                HandlerParam handlerParam = factory.getObject(HandlerParam.class);
                Map<String, String> map = factory.getMap();
                String str = jnOtcDecoderParam.getStr();
                JnLockMachineReturn jnLockMachineReturn = jnLockMachineReturnAnalysis(str);
                if (null != jnLockMachineReturn) {
                    map.put(JnLockMachineReturn.class.getSimpleName(), JSON.toJSONString(jnLockMachineReturn));
                }
                handlerParam.setValue(map);
                return handlerParam;
            } catch (Exception e) {
                log.error("OTC1.0解锁焊机指令返回异常：{}", e.getMessage());
            }
        }
        return null;
    }

    private HandlerParam otcProgramPathIssueReturnAnalysis(JnOtcDecoderParam jnOtcDecoderParam) {
        if (null != jnOtcDecoderParam) {
            try {
                HandlerParam handlerParam = factory.getObject(HandlerParam.class);
                Map<String, String> map = factory.getMap();
                String str = jnOtcDecoderParam.getStr();
                OtcV1ProgramPathIssueReturn otcV1ProgramPathIssueReturn = otcV1ProgramPathIssueReturn(str);
                if (null != otcV1ProgramPathIssueReturn) {
                    map.put(OtcV1ProgramPathIssueReturn.class.getSimpleName(), JSON.toJSONString(otcV1ProgramPathIssueReturn));
                }
                handlerParam.setValue(map);
                return handlerParam;
            } catch (Exception e) {
                log.error("OTC1.0程序包路径下发返回异常：{}", e.getMessage());
            }
        }
        return null;
    }

    /**
     * OTC1.0实时数据解析
     *
     * @param jnOtcDecoderParam 入参
     * @return 返回HandlerParam
     */
    private HandlerParam jnOtcRtDataAnalysis(JnOtcDecoderParam jnOtcDecoderParam) {
        if (null != jnOtcDecoderParam) {
            try {
                HandlerParam handlerParam = factory.getObject(HandlerParam.class);
                Map<String, String> map = factory.getMap();
                //存数据库
                List<JNRtDataDB> jnRtDataDbs = jnRtDataDbAnalysis(jnOtcDecoderParam.getStr());
                if (CommonUtils.isNotEmpty(jnRtDataDbs)) {
                    map.put(JNRtDataDB.class.getSimpleName(), JSON.toJSONString(jnRtDataDbs));
                }
                //发送前端
                List<JNRtDataUI> jnRtDataUis = jnRtDataUiAnalysis(jnOtcDecoderParam.getClientIp(), jnOtcDecoderParam.getStr());
                if (CommonUtils.isNotEmpty(jnRtDataUis)) {
                    map.put(JNRtDataUI.class.getSimpleName(), JSON.toJSONString(jnRtDataUis));
                }
                handlerParam.setValue(map);
                return handlerParam;
            } catch (Exception e) {
                log.error("OTC1.0实时数据解析异常：{}", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 工艺下发返回解析
     *
     * @param jnOtcDecoderParam 入参
     * @return 返回HandlerParam
     */
    private HandlerParam otcIssueReturnAnalysis(JnOtcDecoderParam jnOtcDecoderParam) {
        if (null != jnOtcDecoderParam) {
            JNProcessIssueReturn issueReturn = jnIssueReturnAnalysis(jnOtcDecoderParam.getStr());
            if (null != issueReturn) {
                try {
                    HandlerParam handlerParam = factory.getObject(HandlerParam.class);
                    Map<String, String> map = factory.getMap();
                    map.put(JNProcessIssueReturn.class.getSimpleName(), JSON.toJSONString(issueReturn));
                    handlerParam.setValue(map);
                    return handlerParam;
                } catch (Exception e) {
                    log.error("OTC1.0工艺下发返回解析异常：{}", e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * 索取返回协议解析
     *
     * @param jnOtcDecoderParam 入参
     * @return 返回HandlerParam
     */
    private HandlerParam otcClaimReturnAnalysis(JnOtcDecoderParam jnOtcDecoderParam) {
        if (null != jnOtcDecoderParam) {
            JNProcessClaimReturn claimReturn = jnClaimReturnAnalysis(jnOtcDecoderParam.getStr());
            if (null != claimReturn) {
                try {
                    HandlerParam handlerParam = factory.getObject(HandlerParam.class);
                    Map<String, String> map = factory.getMap();
                    map.put(JNProcessClaimReturn.class.getSimpleName(), JSON.toJSONString(claimReturn));
                    handlerParam.setValue(map);
                    return handlerParam;
                } catch (Exception e) {
                    log.error("OTC1.0索取返回协议解析异常：{}", e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * 实时数据解析通过mq发前端
     *
     * @param str 需要解析的16进制字符串
     * @return 返回一个集合对象
     */
    public List<JNRtDataUI> jnRtDataUiAnalysis(String clientIp, String str) {
        if (CommonUtils.isEmpty(str) || str.length() != 282) {
            return null;
        }
        str = str.toUpperCase();
        try {
            List<JNRtDataUI> rtData = new ArrayList<>();
            Map<String, TaskClaimIssue> otcTaskClaimMap = CommonMap.OTC_TASK_CLAIM_MAP;
            //当前系统时间 yyyy-MM-dd HH:mm:ss
            String nowDateTime = DateTimeUtils.getNowDateTime();
            for (int a = 0; a < 239; a += 80) {
                JNRtDataUI data = new JNRtDataUI();
                //采集模块编号
                data.setGatherNo(Integer.valueOf(str.substring(14, 18), 16).toString());
                if (ObjectUtils.isNotEmpty(otcTaskClaimMap) && otcTaskClaimMap.containsKey(data.getGatherNo())) {
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
                String year = CommonUtils.hexToDecLengthJoint(str.substring(38 + a, 40 + a), 2);
                String month = CommonUtils.hexToDecLengthJoint(str.substring(40 + a, 42 + a), 2);
                String day = CommonUtils.hexToDecLengthJoint(str.substring(42 + a, 44 + a), 2);
                String hour = CommonUtils.hexToDecLengthJoint(str.substring(44 + a, 46 + a), 2);
                String minute = CommonUtils.hexToDecLengthJoint(str.substring(46 + a, 48 + a), 2);
                String second = CommonUtils.hexToDecLengthJoint(str.substring(48 + a, 50 + a), 2);
                StringBuilder strDate = new StringBuilder();
                strDate.append(year).append("-").append(month).append("-").append(day).append(" ").append(hour).append(":").append(minute).append(":").append(second);
                //String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                try {
                    String weldDateTime = LocalDateTime.parse(strDate.toString(), DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME);
                    data.setWeldTime(weldDateTime);
                } catch (Exception e) {
                    log.error("OTC1.0实时数据时间格式异常:{}", e.getMessage());
                    data.setWeldTime(nowDateTime);
                }
                data.setWeldIp(clientIp);
                //电流
                data.setElectricity(BigDecimal.valueOf(Integer.valueOf(str.substring(50 + a, 54 + a), 16)));
                //电压
                data.setVoltage(BigDecimal.valueOf(Integer.valueOf(str.substring(54 + a, 58 + a), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));
                data.setWireFeedRate(BigDecimal.valueOf(Integer.valueOf(str.substring(58 + a, 62 + a), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//送丝速度
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
                rtData.add(data);
            }
            return rtData;
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
        if (CommonUtils.isEmpty(str) || str.length() != 282) {
            return null;
        }
        str = str.toUpperCase();
        try {
            List<JNRtDataDB> rtdata = factory.getList(JNRtDataDB.class);
            //采集模块信息
            List<GatherModel> gatherList = CommonList.getGatherList();
            //焊机设备信息
            List<WeldModel> weldList = CommonList.getWeldList();
            //刷卡领取任务后进行数据绑定
            Map<String, TaskClaimIssue> otcTaskClaimMap = CommonMap.OTC_TASK_CLAIM_MAP;
            JNRtDataDB data = factory.getObject(JNRtDataDB.class);
            for (int a = 0; a < 239; a += 80) {
                //判断OTC待机数据是否存储,如果不存储，则取出待机状态判断
                if (!CommonFunction.isOtcStandbySave()) {
                    Integer otcStandby = Integer.valueOf(str.substring(78 + a, 80 + a), 16);
                    //焊接状态为0表示待机，则直接进入下一次循环
                    if (otcStandby == 0) {
                        continue;
                    }
                }
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
                extracted(weldList, otcTaskClaimMap, data);
                String year = CommonUtils.hexToDecLengthJoint(str.substring(38 + a, 40 + a), 2);
                String month = CommonUtils.hexToDecLengthJoint(str.substring(40 + a, 42 + a), 2);
                String day = CommonUtils.hexToDecLengthJoint(str.substring(42 + a, 44 + a), 2);
                String hour = CommonUtils.hexToDecLengthJoint(str.substring(44 + a, 46 + a), 2);
                String minute = CommonUtils.hexToDecLengthJoint(str.substring(46 + a, 48 + a), 2);
                String second = CommonUtils.hexToDecLengthJoint(str.substring(48 + a, 50 + a), 2);
                StringBuilder strDate = new StringBuilder();
                strDate.append(year).append("-").append(month).append("-").append(day).append(" ").append(hour).append(":").append(minute).append(":").append(second);
                //String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                try {
                    String weldDate = LocalDateTime.parse(strDate.toString(), DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.TODAY_DATE);
                    String weldDateTime = LocalDateTime.parse(strDate.toString(), DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME);
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
                data.setVoltage(BigDecimal.valueOf(Integer.valueOf(str.substring(54 + a, 58 + a), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));  //电压
                data.setWireFeedRate(BigDecimal.valueOf(Integer.valueOf(str.substring(58 + a, 62 + a), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//送丝速度
                data.setPresetEle(BigDecimal.valueOf(Integer.valueOf(str.substring(62 + a, 66 + a), 16)));//预置(给定)电流
                data.setPresetVol(BigDecimal.valueOf(Integer.valueOf(str.substring(66 + a, 70 + a), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//预置(给定)电压
                data.setWeldedJunctionNo(Integer.valueOf(str.substring(70 + a, 78 + a), 16).toString());//焊口号
                data.setWeldStatus(Integer.valueOf(str.substring(78 + a, 80 + a), 16));//焊机状态
                data.setWireDiameter(BigDecimal.valueOf(Integer.valueOf(str.substring(80 + a, 82 + a), 16)));//焊丝直径
                data.setWireMaterialsGases(BigDecimal.valueOf(Integer.valueOf(str.substring(82 + a, 84 + a), 16)));//焊丝材料和保护气体
                data.setWeldElectricity(BigDecimal.valueOf(Integer.valueOf(str.substring(84 + a, 88 + a), 16)));//焊接电流（中位数）
                data.setWeldVoltage(BigDecimal.valueOf(Integer.valueOf(str.substring(88 + a, 92 + a), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//焊接电压（中位数）
                data.setWeldEleAdjust(BigDecimal.valueOf(Integer.valueOf(str.substring(92 + a, 94 + a), 16)));//焊接电流微调
                data.setWeldVolAdjust(BigDecimal.valueOf(Integer.valueOf(str.substring(94 + a, 96 + a), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//焊接电压微调
                data.setChannelNo(Integer.valueOf(str.substring(100 + a, 102 + a), 16).toString());//当前通道号
                data.setAlarmsEleMax(BigDecimal.valueOf(Integer.valueOf(str.substring(102 + a, 106 + a), 16)));//报警电流上限
                data.setAlarmsVolMax(BigDecimal.valueOf(Integer.valueOf(str.substring(106 + a, 110 + a), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//报警电压上限
                data.setAlarmsEleMin(BigDecimal.valueOf(Integer.valueOf(str.substring(110 + a, 114 + a), 16)));//报警电流下限
                data.setAlarmsVolMin(BigDecimal.valueOf(Integer.valueOf(str.substring(114 + a, 118 + a), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//报警电压下限
                //待机数据不存储，则针对起弧、收弧各存储一条待机数据
                extracted(rtdata, data);
                rtdata.add(data);
            }
            return rtdata;
        } catch (Exception e) {
            log.error("OTC1.0实时数据解析存DB异常：", e);
        }
        return null;
    }

    private void extracted(List<WeldModel> weldList, Map<String, TaskClaimIssue> otcTaskClaimMap, JNRtDataDB data) {
        //焊工、任务、焊机等信息判断并赋值
        if (ObjectUtils.isNotEmpty(otcTaskClaimMap) && otcTaskClaimMap.containsKey(data.getGatherNo())) {
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
                    if (StringUtils.isNotBlank(weld.getGatherNo())) {
                        List<String> gatherNoList = Arrays.stream(weld.getGatherNo().split(",")).map(gatherNo -> CommonUtils.stringLengthJoint(gatherNo, 4)).collect(Collectors.toList());
                        if (gatherNoList.contains(CommonUtils.stringLengthJoint(data.getGatherNo(), 4))) {
                            data.setMachineId(weld.getId());
                            data.setMachineNo(weld.getMachineNo());
                            data.setMachineDeptId(weld.getDeptId());
                            break;
                        }
                    }
                }
            }
        }
    }

    private void extracted(List<JNRtDataDB> rtdata, JNRtDataDB data) {
        if (!CommonFunction.isOtcStandbySave()) {
            //起弧（增加一条待机数据）
            if (data.getWeldStatus() == 7) {
                JNRtDataDB jnRtDataDb = new JNRtDataDB();
                jnRtDataDb.setWeldStatus(0);
                LocalDateTime parse = LocalDateTime.parse(jnRtDataDb.getWeldTime(), DateTimeUtils.DEFAULT_DATETIME);
                //减去1秒
                String weldTime = parse.minusSeconds(1).format(DateTimeUtils.DEFAULT_DATETIME);
                jnRtDataDb.setWeldTime(weldTime);
                jnRtDataDb.setElectricity(BigDecimal.ZERO);
                jnRtDataDb.setVoltage(BigDecimal.ZERO);
                rtdata.add(jnRtDataDb);
            }
            //收弧（增加一条待机数据）
            else if (data.getWeldStatus() == 5) {
                JNRtDataDB jnRtDataDb = new JNRtDataDB();
                jnRtDataDb.setWeldStatus(0);
                LocalDateTime parse = LocalDateTime.parse(jnRtDataDb.getWeldTime(), DateTimeUtils.DEFAULT_DATETIME);
                //加上1秒
                String weldTime = parse.plusSeconds(1).format(DateTimeUtils.DEFAULT_DATETIME);
                jnRtDataDb.setWeldTime(weldTime);
                jnRtDataDb.setElectricity(BigDecimal.ZERO);
                jnRtDataDb.setVoltage(BigDecimal.ZERO);
                rtdata.add(jnRtDataDb);
            }
        }
    }

    /**
     * 下发返回协议解析
     *
     * @param str 16进制字符串
     * @return 下发规范返回类
     */
    public JNProcessIssueReturn jnIssueReturnAnalysis(String str) {
        if (CommonUtils.isNotEmpty(str) && str.length() == 24) {
            JNProcessIssueReturn issueReturn = new JNProcessIssueReturn();
            issueReturn.setGatherNo(Integer.valueOf(str.substring(12, 16), 16).toString());
            issueReturn.setChannelNo(Integer.valueOf(str.substring(16, 18), 16).toString());
            issueReturn.setFlag(Integer.valueOf(str.substring(18, 20), 16));
            return issueReturn;
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
            JNProcessClaimReturn claim = new JNProcessClaimReturn();
            claim.setGatherNo(Integer.valueOf(str.substring(12, 16), 16).toString()); //采集模块编号
            claim.setStatus(Integer.valueOf(str.substring(16, 18), 16));
            claim.setChannelNo(Integer.valueOf(str.substring(18, 20), 16).toString());
            claim.setSpotWeldTime(BigDecimal.valueOf(Long.valueOf(str.substring(20, 24), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//点焊时间
            claim.setPreflowTime(BigDecimal.valueOf(Long.valueOf(str.substring(24, 28), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//提前送气时间
            claim.setInitialEle(BigDecimal.valueOf(Long.valueOf(str.substring(28, 32), 16)));//初期电流
            claim.setInitialVol(BigDecimal.valueOf(Long.valueOf(str.substring(32, 36), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//初期电压
            claim.setInitialVolUnitary(BigDecimal.valueOf(Long.valueOf(str.substring(36, 40), 16)));//初期电压一元
            claim.setWeldElectricity(BigDecimal.valueOf(Long.valueOf(str.substring(40, 44), 16)));//焊接电流
            claim.setWeldVoltage(BigDecimal.valueOf(Long.valueOf(str.substring(44, 48), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//焊接电压
            claim.setWeldVoltageUnitary(BigDecimal.valueOf(Long.valueOf(str.substring(48, 52), 16)));//焊接电压一元
            claim.setExtinguishArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(52, 56), 16)));//收弧电流
            claim.setExtinguishArcVol(BigDecimal.valueOf(Long.valueOf(str.substring(56, 60), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//收弧电压
            claim.setExtinguishArcVolUnitary(BigDecimal.valueOf(Long.valueOf(str.substring(60, 64), 16)));//收弧电压一元
            claim.setHysteresisAspirated(BigDecimal.valueOf(Long.valueOf(str.substring(64, 68), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//滞后送气
            claim.setArcPeculiarity(BigDecimal.valueOf(Long.valueOf(str.substring(68, 72), 16)));//电弧特性
            claim.setGases(BigDecimal.valueOf(Long.valueOf(str.substring(72, 74), 16)));//气体
            claim.setWireDiameter(BigDecimal.valueOf(Long.valueOf(str.substring(74, 76), 16)));//焊丝直径
            claim.setWireMaterials(Integer.valueOf(str.substring(76, 78), 16).toString());//焊丝材料
            claim.setWeldProcess(Integer.valueOf(str.substring(78, 80), 16).toString());//焊接过程
            claim.setControlInfo(Integer.valueOf(str.substring(80, 84), 16).toString());//控制信息
            claim.setWeldEleAdjust(BigDecimal.valueOf(Long.valueOf(str.substring(84, 86), 16)));//焊接电流微调
            claim.setWeldVolAdjust(BigDecimal.valueOf(Long.valueOf(str.substring(86, 88), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//焊接电压微调
            claim.setExtinguishArcEleAdjust(BigDecimal.valueOf(Long.valueOf(str.substring(88, 90), 16)));//收弧电流微调
            claim.setExtinguishArcVolAdjust(BigDecimal.valueOf(Long.valueOf(str.substring(90, 92), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//收弧电压微调
            claim.setAlarmsElectricityMax(BigDecimal.valueOf(Long.valueOf(str.substring(92, 96), 16)));//报警电流上限
            claim.setAlarmsVoltageMax(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//报警电压上限
            claim.setAlarmsElectricityMin(BigDecimal.valueOf(Long.valueOf(str.substring(100, 104), 16)));//报警电流下限
            claim.setAlarmsVoltageMin(BigDecimal.valueOf(Long.valueOf(str.substring(104, 108), 16)).divide(new BigDecimal("10"), 1, RoundingMode.HALF_UP));//报警电压下限
            return claim;
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
        if (CommonUtils.isNotEmpty(str) && str.length() == 22) {
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
        if (CommonUtils.isNotEmpty(str) && str.length() == 22) {
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
            }
        }
        return null;
    }

    /**
     * OTCv1程序包路径下发返回解析
     *
     * @param str 16进制字符串
     * @return 程序包路径下发返回实体类
     */
    public OtcV1ProgramPathIssueReturn otcV1ProgramPathIssueReturn(String str) {
        if (CommonUtils.isNotEmpty(str) && str.length() == 22) {
            try {
                OtcV1ProgramPathIssueReturn pathIssueReturn = new OtcV1ProgramPathIssueReturn();
                pathIssueReturn.setGatherNo(Integer.valueOf(str.substring(12, 16), 16).toString());
                pathIssueReturn.setStatus(Integer.valueOf(str.substring(16, 18), 16));
                return pathIssueReturn;
            } catch (Exception e) {
                log.error("OTCv1程序包路径下发返回解析异常：", e);
            }
        }
        return null;
    }

}
