package com.shth.das.business.datadown.sx;

import com.alibaba.fastjson2.JSON;
import com.shth.das.business.datadown.BaseProtocol;
import com.shth.das.codeparam.MqttParam;
import com.shth.das.common.CommonMap;
import com.shth.das.common.DownTopicEnum;
import com.shth.das.common.GainTopicName;
import com.shth.das.pojo.jnsx.*;
import com.shth.das.util.CRC7Check;
import com.shth.das.util.CommonUtils;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class SxDownHandler extends BaseProtocol {

    private final SxProtocolJoint sxProtocolJoint = new SxProtocolJoint();

    @Override
    protected void register(Map<String, Consumer<MqttParam>> map) {
        //松下GL5系列CO2焊机工艺下发
        map.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxGl5Co2ProcessIssue), this::sxGl5Co2ProcessIssue);
        //松下GL5系列TIG焊机工艺下发
        map.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxGl5TigProcessIssue), this::sxGl5TigProcessIssue);
        //松下GL5系列【通道设定、通道读取】:106
        map.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxGl5WeldChannelSet), this::sxGl5WeldChannelSet);
        //松下GL5系列【工艺索取、工艺删除】:106
        map.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxGl5ProcessClaim), this::sxGl5ProcessClaim);
        //松下【FR2、AT3】系列【通道参数查询、通道参数删除】
        map.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxFr2ChannelParamQuery), this::sxFr2ChannelParamQuery);
        //松下FR2系列【通道参数下载】
        map.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxFr2ChannelParamDownload), this::sxFr2ChannelParamDownload);
        //松下AT3系列【通道参数下载】
        map.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.SxAt3ParamDownload), this::sxAt3ParamDownload);
    }

    /**
     * 松下GL5系列CO2焊机工艺下发
     *
     * @param param mqtt入参实体类
     */
    private void sxGl5Co2ProcessIssue(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            SxCO2ProcessIssue sxCo2ProcessIssue = JSON.parseObject(message, SxCO2ProcessIssue.class);
            if (null != sxCo2ProcessIssue) {
                String weldCid = sxCo2ProcessIssue.getWeldCid();
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
    private void sxGl5TigProcessIssue(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            SxTIGProcessIssue sxTigProcessIssue = JSON.parseObject(message, SxTIGProcessIssue.class);
            if (null != sxTigProcessIssue) {
                String weldCid = sxTigProcessIssue.getWeldCid();
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
    private void sxGl5WeldChannelSet(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            SxWeldChannelSetting sxWeldChannelSetting = JSON.parseObject(message, SxWeldChannelSetting.class);
            if (null != sxWeldChannelSetting) {
                String weldCid = sxWeldChannelSetting.getWeldCid();
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
    private void sxGl5ProcessClaim(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            SxProcessClaim sxProcessClaim = JSON.parseObject(message, SxProcessClaim.class);
            if (null != sxProcessClaim) {
                String weldCid = sxProcessClaim.getWeldCid();
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
    private void sxFr2ChannelParamQuery(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            SxChannelParamQuery sxChannelParamQuery = JSON.parseObject(message, SxChannelParamQuery.class);
            if (null != sxChannelParamQuery) {
                String weldCid = sxChannelParamQuery.getWeldCid();
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
    private void sxFr2ChannelParamDownload(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            SxChannelParamReplyHave channelParamReplyHave = JSON.parseObject(message, SxChannelParamReplyHave.class);
            if (null != channelParamReplyHave) {
                String weldCid = channelParamReplyHave.getWeldCid();
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
    private void sxAt3ParamDownload(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            At3ParamDownload at3ParamDownload = JSON.parseObject(message, At3ParamDownload.class);
            if (null != at3ParamDownload) {
                String weldCid = at3ParamDownload.getWeldCid();
                //length：92
                String str = sxProtocolJoint.at3ParamDownloadProtocol(at3ParamDownload);
                sxChannelWrite(weldCid, str, "----->松下AT3系列参数下载成功", topic);
            }
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
