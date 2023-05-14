package com.shth.das.business.datadown;

import com.alibaba.fastjson2.JSON;
import com.shth.das.business.datadown.otc.OtcProtocolJoint;
import com.shth.das.business.datadown.sx.SxProtocolJoint;
import com.shth.das.codeparam.MqttParam;
import com.shth.das.common.CommonFunction;
import com.shth.das.common.CommonMap;
import com.shth.das.pojo.db.TaskClaimIssue;
import com.shth.das.pojo.jnotc.*;
import com.shth.das.pojo.jnsx.*;
import com.shth.das.util.CRC7Check;
import com.shth.das.util.CommonUtils;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @description: 协议拼接处理
 * @author: Shan Long
 * @create: 2021-09-01
 */
@Slf4j
public class MqttMessageAnalysis {

    /**
     * 焊工刷卡领取任务
     *
     * @param param mqtt入参实体类
     */
    public void taskClaimIssue(MqttParam param) {
        if (ObjectUtils.isEmpty(param)) {
            return;
        }
        String message = param.getMessage();
        TaskClaimIssue taskClaimIssue = JSON.parseObject(message, TaskClaimIssue.class);
        if (ObjectUtils.isEmpty(taskClaimIssue)) {
            return;
        }
        //0:如果是OTC设备
        if (taskClaimIssue.getWeldType() == 0) {
            //判断是开始任务还是结束任务（0：开始）
            if (taskClaimIssue.getStartFlag() == 0) {
                String gatherNo = taskClaimIssue.getGatherNo();
                if (CommonUtils.isNotEmpty(gatherNo)) {
                    gatherNo = Integer.valueOf(gatherNo).toString();
                    //如果已经开始任务，则会将之前的任务顶掉
                    CommonMap.OTC_TASK_CLAIM_MAP.put(gatherNo, taskClaimIssue);
                    //领任务成功后，将解锁焊机
                    slotCardUnLockOtcWeld(gatherNo);
                }
            }
            //判断是开始任务还是结束任务（1：结束）
            else if (taskClaimIssue.getStartFlag() == 1) {
                String gatherNo = taskClaimIssue.getGatherNo();
                if (CommonUtils.isNotEmpty(gatherNo)) {
                    gatherNo = Integer.valueOf(gatherNo).toString();
                    CommonMap.OTC_TASK_CLAIM_MAP.remove(gatherNo);
                }
            }
        }
        //1:如果是松下设备
        else if (taskClaimIssue.getWeldType() == 1) {
            //判断是开始任务还是结束任务（0：开始）
            if (taskClaimIssue.getStartFlag() == 0) {
                String weldCid = taskClaimIssue.getWeldCid();
                if (CommonUtils.isNotEmpty(weldCid)) {
                    //如果已经开始任务，则会将之前的任务顶掉
                    CommonMap.SX_TASK_CLAIM_MAP.put(weldCid, taskClaimIssue);
                    //领任务成功后，将解锁焊机
                    slotCardUnLockSxWeld(weldCid);
                }
            }
            //判断是开始任务还是结束任务（1：结束）
            else if (taskClaimIssue.getStartFlag() == 1) {
                String weldCid = taskClaimIssue.getWeldCid();
                if (CommonUtils.isNotEmpty(weldCid)) {
                    CommonMap.SX_TASK_CLAIM_MAP.remove(weldCid);
                }
            }
        }
    }

    /**
     * 刷卡解锁OTC焊机
     *
     * @param gatherNo 采集编号
     */
    private void slotCardUnLockOtcWeld(String gatherNo) {
        if (CommonFunction.isSlotCardEnableDevice() && StringUtils.isNotBlank(gatherNo)) {
            try {
                //去除0
                gatherNo = Integer.valueOf(gatherNo).toString();
                if (CommonMap.OTC_GATHER_NO_CTX_MAP.containsKey(gatherNo)) {
                    //根据采集编号查询通道
                    Channel channel = CommonMap.OTC_GATHER_NO_CTX_MAP.get(gatherNo).channel();
                    //数据长度拼接
                    String gatherno = CommonUtils.lengthJoint(gatherNo, 4);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("007E0A01010119").append(gatherno).append("00007D");
                    //总长度：24（解锁焊机指令）
                    //String str = "007E0A01010119" + gatherno + "00007D";
                    //判断该焊机通道是否打开、是否活跃、是否可写
                    if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                        channel.writeAndFlush(stringBuilder.toString());
                    }
                }
            } catch (Exception e) {
                log.error("刷卡解锁OTC焊机异常：", e);
            }
        }
    }

    /**
     * 刷卡解锁松下焊机
     *
     * @param weldCid 设备CID
     */
    private void slotCardUnLockSxWeld(String weldCid) {
        if (CommonFunction.isSlotCardEnableDevice() && StringUtils.isNotBlank(weldCid)) {
            try {
                SxWeldChannelSetting sxWeldChannelSetting = new SxWeldChannelSetting();
                sxWeldChannelSetting.setWeldCid(weldCid);
                //（0：解锁；1：锁定）
                sxWeldChannelSetting.setFunction(0);
                //2：设置
                sxWeldChannelSetting.setReadWriteFlag(2);
                //0：焊接通道
                sxWeldChannelSetting.setChannelSelect(0);
                SxProtocolJoint sxProtocolJoint = new SxProtocolJoint();
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
                log.error("刷卡解锁松下焊机异常：", e);
            }
        }
    }

    /**
     * OTC1.0工艺下发
     *
     * @param param mqtt入参实体类
     */
    public void otcV1ProcessIssue(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            //匹配下发规范模板
            List<JNProcessIssue> list = JSON.parseArray(message, JNProcessIssue.class);
            if (CommonUtils.isNotEmpty(list)) {
                for (JNProcessIssue issue : list) {
                    String gatherNo = issue.getGatherNo();
                    OtcProtocolJoint otcProtocolJoint = new OtcProtocolJoint();
                    //Java对象解析成16进制字符串
                    String str = otcProtocolJoint.jnIssueProtocol(issue);
                    otcChannelWrite(gatherNo, str, "----->OTC工艺下发成功", topic);
                }
            }
        }
    }

    /**
     * OTC1.0工艺索取
     *
     * @param param mqtt入参实体类
     */
    public void otcV1ProcessClaim(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            JNProcessClaim jnProcessClaim = JSON.parseObject(message, JNProcessClaim.class);
            if (null != jnProcessClaim) {
                String gatherNo = jnProcessClaim.getGatherNo();
                OtcProtocolJoint otcProtocolJoint = new OtcProtocolJoint();
                //Java对象解析成16进制字符串
                String str = otcProtocolJoint.jnClaimProtocol(jnProcessClaim);
                otcChannelWrite(gatherNo, str, "----->OTC工艺索取成功", topic);
            }
        }
    }

    /**
     * OTC1.0密码下发
     *
     * @param param mqtt入参实体类
     */
    public void otcV1PasswordIssue(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            JNPasswordIssue jnPasswordIssue = JSON.parseObject(message, JNPasswordIssue.class);
            if (null != jnPasswordIssue) {
                String gatherNo = jnPasswordIssue.getGatherNo();
                OtcProtocolJoint otcProtocolJoint = new OtcProtocolJoint();
                //java对象转16进制字符串
                String str = otcProtocolJoint.jnPasswordProtocol(jnPasswordIssue);
                otcChannelWrite(gatherNo, str, "----->OTC密码下发成功", topic);
            }
        }
    }

    /**
     * OTC1.0控制命令下发
     *
     * @param param mqtt入参实体类
     */
    public void otcV1CommandIssue(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            JNCommandIssue jnCommandIssue = JSON.parseObject(message, JNCommandIssue.class);
            if (null != jnCommandIssue) {
                String gatherNo = jnCommandIssue.getGatherNo();
                OtcProtocolJoint otcProtocolJoint = new OtcProtocolJoint();
                //java对象转16进制字符串
                String str = otcProtocolJoint.jnCommandProtocol(jnCommandIssue);
                otcChannelWrite(gatherNo, str, "----->OTC控制命令下发成功", topic);
            }
        }
    }

    /**
     * 松下GL5系列CO2焊机工艺下发
     *
     * @param param mqtt入参实体类
     */
    public void sxGl5Co2ProcessIssue(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            SxCO2ProcessIssue sxCo2ProcessIssue = JSON.parseObject(message, SxCO2ProcessIssue.class);
            if (null != sxCo2ProcessIssue) {
                String weldCid = sxCo2ProcessIssue.getWeldCid();
                SxProtocolJoint sxProtocolJoint = new SxProtocolJoint();
                //length：406
                String str = sxProtocolJoint.sxCO2ProcessProtocol(sxCo2ProcessIssue);
                sxChannelWrite(weldCid, str, "----->松下GL5系列CO2工艺下发成功", topic);
            }
        }
    }

    /**
     * 松下GL5系列TIG焊机工艺下发
     *
     * @param param mqtt入参实体类
     */
    public void sxGl5TigProcessIssue(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            SxTIGProcessIssue sxTigProcessIssue = JSON.parseObject(message, SxTIGProcessIssue.class);
            if (null != sxTigProcessIssue) {
                String weldCid = sxTigProcessIssue.getWeldCid();
                SxProtocolJoint sxProtocolJoint = new SxProtocolJoint();
                //length：446
                String str = sxProtocolJoint.sxTigProcessProtocol(sxTigProcessIssue);
                sxChannelWrite(weldCid, str, "----->松下GL5系列TIG工艺下发成功", topic);
            }
        }
    }

    /**
     * 松下GL5系列【通道设定、通道读取】
     *
     * @param param mqtt入参实体类
     */
    public void sxGl5WeldChannelSet(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            SxWeldChannelSetting sxWeldChannelSetting = JSON.parseObject(message, SxWeldChannelSetting.class);
            if (null != sxWeldChannelSetting) {
                String weldCid = sxWeldChannelSetting.getWeldCid();
                SxProtocolJoint sxProtocolJoint = new SxProtocolJoint();
                //length:106
                String str = sxProtocolJoint.sxWeldChannelSetProtocol(sxWeldChannelSetting);
                sxChannelWrite(weldCid, str, "----->松下GL5系列焊机通道[设定/读取]成功", topic);
            }
        }
    }

    /**
     * 松下GL5系列工艺【工艺索取、工艺删除】
     *
     * @param param mqtt入参实体类
     */
    public void sxGl5ProcessClaim(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            SxProcessClaim sxProcessClaim = JSON.parseObject(message, SxProcessClaim.class);
            if (null != sxProcessClaim) {
                String weldCid = sxProcessClaim.getWeldCid();
                SxProtocolJoint sxProtocolJoint = new SxProtocolJoint();
                //length:106
                String str = sxProtocolJoint.sxProcessClaimProtocol(sxProcessClaim);
                sxChannelWrite(weldCid, str, "----->松下GL5系列工艺[索取/删除]成功", topic);
            }
        }
    }

    /**
     * 松下【FR2、AT3】系列【通道参数查询、通道参数删除】
     *
     * @param param mqtt入参实体类
     */
    public void sxFr2ChannelParamQuery(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            SxChannelParamQuery sxChannelParamQuery = JSON.parseObject(message, SxChannelParamQuery.class);
            if (null != sxChannelParamQuery) {
                String weldCid = sxChannelParamQuery.getWeldCid();
                SxProtocolJoint sxProtocolJoint = new SxProtocolJoint();
                //length:52
                String str = sxProtocolJoint.sxChannelParamQueryProtocol(sxChannelParamQuery);
                sxChannelWrite(weldCid, str, "----->松下[FR2、AT3]系列通道参数[查询/删除]成功", topic);
            }
        }
    }

    /**
     * 松下FR2系列【通道参数下载】
     *
     * @param param mqtt入参实体类
     */
    public void sxFr2ChannelParamDownload(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            SxChannelParamReplyHave channelParamReplyHave = JSON.parseObject(message, SxChannelParamReplyHave.class);
            if (null != channelParamReplyHave) {
                String weldCid = channelParamReplyHave.getWeldCid();
                SxProtocolJoint sxProtocolJoint = new SxProtocolJoint();
                //length：204
                String str = sxProtocolJoint.sxChannelParamReplyHaveProtocol(channelParamReplyHave);
                sxChannelWrite(weldCid, str, "----->松下FR2系列通道参数下载成功", topic);
            }
        }
    }

    /**
     * 松下AT3系列【通道参数下载】
     *
     * @param param mqtt入参实体类
     */
    public void sxAt3ParamDownload(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            At3ParamDownload at3ParamDownload = JSON.parseObject(message, At3ParamDownload.class);
            if (null != at3ParamDownload) {
                String weldCid = at3ParamDownload.getWeldCid();
                SxProtocolJoint sxProtocolJoint = new SxProtocolJoint();
                //length：92
                String str = sxProtocolJoint.at3ParamDownloadProtocol(at3ParamDownload);
                sxChannelWrite(weldCid, str, "----->松下AT3系列参数下载成功", topic);
            }
        }
    }

    /**
     * OTC程序包路径下发
     *
     * @param param mqtt入参实体类
     */
    public void otcV1IssueProgramPath(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            OtcV1IssueProgramPath otcV1IssueProgramPath = JSON.parseObject(message, OtcV1IssueProgramPath.class);
            if (null != otcV1IssueProgramPath) {
                String gatherNo = otcV1IssueProgramPath.getGatherNo();
                OtcProtocolJoint otcProtocolJoint = new OtcProtocolJoint();
                String str = otcProtocolJoint.otcV1IssueProgramPath(otcV1IssueProgramPath);
                otcChannelWrite(gatherNo, str, "----->OTC程序包路径下发成功", topic);
            }
        }
    }

    /**
     * 根据采集编号查找通道并写入数据
     *
     * @param gatherNo 采集编号(10进制数字)
     * @param str      16进制字符串
     * @param msg      打印内容
     * @param topic    主题
     */
    private void otcChannelWrite(String gatherNo, String str, String msg, String topic) {
        try {
            if (CommonUtils.isNotEmpty(gatherNo) && CommonUtils.isNotEmpty(str)) {
                gatherNo = Integer.valueOf(gatherNo).toString();
                if (CommonMap.OTC_GATHER_NO_CTX_MAP.containsKey(gatherNo)) {
                    Channel channel = CommonMap.OTC_GATHER_NO_CTX_MAP.get(gatherNo).channel();
                    //判断该焊机通道是否打开、是否活跃、是否可写
                    if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                        channel.writeAndFlush(str);
                        log.info("{}:{}", msg, topic);
                    }
                }
            }
        } catch (Exception e) {
            log.error("OTC数据下行异常：", e);
        }
    }

    /**
     * 根据设备CID查找通道并写入数据
     *
     * @param weldCid 设备CID
     * @param str     16进制字符串
     * @param msg     打印内容
     * @param topic   主题
     */
    private void sxChannelWrite(String weldCid, String str, String msg, String topic) {
        try {
            //对字符串进行CRC7校验
            str = CRC7Check.crc7CheckAndReplace(str);
            if (CommonUtils.isNotEmpty(weldCid) && CommonUtils.isNotEmpty(str)) {
                if (!CommonMap.SX_WELD_CID_CTX_MAP.isEmpty() && CommonMap.SX_WELD_CID_CTX_MAP.containsKey(weldCid)) {
                    Channel channel = CommonMap.SX_WELD_CID_CTX_MAP.get(weldCid).channel();
                    //判断该焊机通道是否打开、是否活跃、是否可写
                    if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                        channel.writeAndFlush(str);
                        log.info("{}:{}", msg, topic);
                    }
                }
            }
        } catch (Exception e) {
            log.error("松下数据下行异常：", e);
        }
    }
}
