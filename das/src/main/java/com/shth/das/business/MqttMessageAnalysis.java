package com.shth.das.business;

import com.alibaba.fastjson.JSON;
import com.shth.das.codeparam.MqttParam;
import com.shth.das.common.CommonMap;
import com.shth.das.pojo.db.TaskClaimIssue;
import com.shth.das.pojo.jnotc.JNCommandIssue;
import com.shth.das.pojo.jnotc.JNPasswordIssue;
import com.shth.das.pojo.jnotc.JNProcessClaim;
import com.shth.das.pojo.jnotc.JNProcessIssue;
import com.shth.das.pojo.jnsx.*;
import com.shth.das.util.CRC7Check;
import com.shth.das.util.CommonUtils;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

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
        if (null != param) {
            final String message = param.getMessage();
            TaskClaimIssue taskClaimIssue = JSON.parseObject(message, TaskClaimIssue.class);
            if (null != taskClaimIssue) {
                //如果是OTC设备
                if (taskClaimIssue.getWeldType() == 0) {
                    //判断是开始任务还是结束任务（0：开始）
                    if (taskClaimIssue.getStartFlag() == 0) {
                        String gatherNo = taskClaimIssue.getGatherNo();
                        if (CommonUtils.isNotEmpty(gatherNo)) {
                            gatherNo = Integer.valueOf(gatherNo).toString();
                            //如果已经开始任务，则会将之前的任务顶掉
                            CommonMap.OTC_TASK_CLAIM_MAP.put(gatherNo, taskClaimIssue);
                        }
                    }
                    //否则就是结束任务
                    else {
                        String gatherNo = taskClaimIssue.getGatherNo();
                        if (CommonUtils.isNotEmpty(gatherNo)) {
                            gatherNo = Integer.valueOf(gatherNo).toString();
                            CommonMap.OTC_TASK_CLAIM_MAP.remove(gatherNo);
                        }
                    }
                }
                //否则就是松下设备
                else {
                    //判断是开始任务还是结束任务（0：开始）
                    if (taskClaimIssue.getStartFlag() == 0) {
                        final String weldCid = taskClaimIssue.getWeldCid();
                        if (CommonUtils.isNotEmpty(weldCid)) {
                            //如果已经开始任务，则会将之前的任务顶掉
                            CommonMap.SX_TASK_CLAIM_MAP.put(weldCid, taskClaimIssue);
                        }
                    }
                    //否则就是结束任务
                    else {
                        final String weldCid = taskClaimIssue.getWeldCid();
                        if (CommonUtils.isNotEmpty(weldCid)) {
                            CommonMap.SX_TASK_CLAIM_MAP.remove(weldCid);
                        }
                    }
                }
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
            final String message = param.getMessage();
            final String topic = param.getTopic();
            //匹配下发规范模板
            List<JNProcessIssue> list = JSON.parseArray(message, JNProcessIssue.class);
            if (CommonUtils.isNotEmpty(list)) {
                for (JNProcessIssue issue : list) {
                    String gatherNo = issue.getGatherNo();
                    //Java对象解析成16进制字符串
                    String str = JnOtcRtDataProtocol.jnIssueProtocol(issue);
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
            final String message = param.getMessage();
            final String topic = param.getTopic();
            JNProcessClaim jnProcessClaim = JSON.parseObject(message, JNProcessClaim.class);
            if (null != jnProcessClaim) {
                String gatherNo = jnProcessClaim.getGatherNo();
                //Java对象解析成16进制字符串
                String str = JnOtcRtDataProtocol.jnClaimProtocol(jnProcessClaim);
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
            final String message = param.getMessage();
            final String topic = param.getTopic();
            JNPasswordIssue jnPasswordIssue = JSON.parseObject(message, JNPasswordIssue.class);
            if (null != jnPasswordIssue) {
                String gatherNo = jnPasswordIssue.getGatherNo();
                //java对象转16进制字符串
                String str = JnOtcRtDataProtocol.jnPasswordProtocol(jnPasswordIssue);
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
            final String message = param.getMessage();
            final String topic = param.getTopic();
            JNCommandIssue jnCommandIssue = JSON.parseObject(message, JNCommandIssue.class);
            if (null != jnCommandIssue) {
                String gatherNo = jnCommandIssue.getGatherNo();
                //java对象转16进制字符串
                String str = JnOtcRtDataProtocol.jnCommandProtocol(jnCommandIssue);
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
            final String message = param.getMessage();
            final String topic = param.getTopic();
            SxCO2ProcessIssue sxCo2ProcessIssue = JSON.parseObject(message, SxCO2ProcessIssue.class);
            if (null != sxCo2ProcessIssue) {
                final String weldCid = sxCo2ProcessIssue.getWeldCid();
                String str = JnSxRtDataProtocol.sxCO2ProcessProtocol(sxCo2ProcessIssue);
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
            final String message = param.getMessage();
            final String topic = param.getTopic();
            SxTIGProcessIssue sxTigProcessIssue = JSON.parseObject(message, SxTIGProcessIssue.class);
            if (null != sxTigProcessIssue) {
                final String weldCid = sxTigProcessIssue.getWeldCid();
                String str = JnSxRtDataProtocol.sxTigProcessProtocol(sxTigProcessIssue);
                sxChannelWrite(weldCid, str, "----->松下GL5系列TIG工艺下发成功", topic);
            }
        }
    }

    /**
     * 松下GL5系列TIG焊机工艺下发
     *
     * @param param mqtt入参实体类
     */
    public void sxGl5WeldChannelSet(MqttParam param) {
        if (null != param) {
            final String message = param.getMessage();
            final String topic = param.getTopic();
            SxWeldChannelSetting sxWeldChannelSetting = JSON.parseObject(message, SxWeldChannelSetting.class);
            if (null != sxWeldChannelSetting) {
                final String weldCid = sxWeldChannelSetting.getWeldCid();
                String str = JnSxRtDataProtocol.sxWeldChannelSetProtocol(sxWeldChannelSetting);
                sxChannelWrite(weldCid, str, "----->松下GL5系列焊机通道设定/读取成功", topic);
            }
        }
    }

    /**
     * 松下GL5系列工艺索取/删除
     *
     * @param param mqtt入参实体类
     */
    public void sxGl5ProcessClaim(MqttParam param) {
        if (null != param) {
            final String message = param.getMessage();
            final String topic = param.getTopic();
            SxProcessClaim sxProcessClaim = JSON.parseObject(message, SxProcessClaim.class);
            if (null != sxProcessClaim) {
                final String weldCid = sxProcessClaim.getWeldCid();
                String str = JnSxRtDataProtocol.sxProcessClaimProtocol(sxProcessClaim);
                sxChannelWrite(weldCid, str, "----->松下GL5系列工艺索取/删除成功", topic);
            }
        }
    }

    /**
     * 松下FR2系列通道参数查询/删除
     *
     * @param param mqtt入参实体类
     */
    public void sxFr2ChannelParamQuery(MqttParam param) {
        if (null != param) {
            final String message = param.getMessage();
            final String topic = param.getTopic();
            SxChannelParamQuery sxChannelParamQuery = JSON.parseObject(message, SxChannelParamQuery.class);
            if (null != sxChannelParamQuery) {
                final String weldCid = sxChannelParamQuery.getWeldCid();
                String str = JnSxRtDataProtocol.sxChannelParamQueryProtocol(sxChannelParamQuery);
                sxChannelWrite(weldCid, str, "----->松下FR2系列通道参数查询/删除成功", topic);
            }
        }
    }

    /**
     * 松下FR2系列通道参数下载
     *
     * @param param mqtt入参实体类
     */
    public void sxFr2ChannelParamDownload(MqttParam param) {
        if (null != param) {
            final String message = param.getMessage();
            final String topic = param.getTopic();
            SxChannelParamReplyHave channelParamReplyHave = JSON.parseObject(message, SxChannelParamReplyHave.class);
            if (null != channelParamReplyHave) {
                String weldCid = channelParamReplyHave.getWeldCid();
                String str = JnSxRtDataProtocol.sxChannelParamReplyHaveProtocol(channelParamReplyHave);
                sxChannelWrite(weldCid, str, "----->松下FR2系列通道参数下载成功", topic);
            }
        }
    }

    /**
     * 松下AT3系列参数下载
     *
     * @param param mqtt入参实体类
     */
    public void sxAt3ParamDownload(MqttParam param) {
        if (null != param) {
            final String message = param.getMessage();
            final String topic = param.getTopic();
            At3ParamDownload at3ParamDownload = JSON.parseObject(message, At3ParamDownload.class);
            if (null != at3ParamDownload) {
                final String weldCid = at3ParamDownload.getWeldCid();
                String str = JnSxRtDataProtocol.at3ParamDownloadProtocol(at3ParamDownload);
                sxChannelWrite(weldCid, str, "----->松下AT3系列参数下载成功", topic);
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
                        channel.writeAndFlush(str).sync();
                        log.info("{}:{}", msg, topic);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("OTC数据下行异常：{}", e.getMessage());
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
                    final Channel channel = CommonMap.SX_WELD_CID_CTX_MAP.get(weldCid).channel();
                    //判断该焊机通道是否打开、是否活跃、是否可写
                    if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                        channel.writeAndFlush(str).sync();
                        log.info("{}:{}", msg, topic);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("松下数据下行异常：{}", e.getMessage());
        }
    }
}
