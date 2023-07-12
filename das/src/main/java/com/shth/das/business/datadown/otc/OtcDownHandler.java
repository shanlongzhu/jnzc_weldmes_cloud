package com.shth.das.business.datadown.otc;

import com.alibaba.fastjson2.JSON;
import com.shth.das.business.datadown.BaseProtocol;
import com.shth.das.codeparam.MqttParam;
import com.shth.das.common.CommonMap;
import com.shth.das.common.DownTopicEnum;
import com.shth.das.common.GainTopicName;
import com.shth.das.pojo.jnotc.*;
import com.shth.das.util.CommonUtils;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class OtcDownHandler extends BaseProtocol {

    private final OtcProtocolJoint otcProtocolJoint = new OtcProtocolJoint();

    @Override
    protected void register(Map<String, Consumer<MqttParam>> map) {
        //OTC（1.0）工艺下发
        map.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1ProcessIssue), this::otcV1ProcessIssue);
        //OTC（1.0）工艺索取
        map.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1ProcessClaim), this::otcV1ProcessClaim);
        //OTC（1.0）密码下发
        map.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1PasswordIssue), this::otcV1PasswordIssue);
        //OTC（1.0）控制命令下发
        map.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1CommandIssue), this::otcV1CommandIssue);
        //OTC程序包路径下发
        map.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.OtcV1IssueProgramPath), this::otcV1IssueProgramPath);
    }

    /**
     * OTC1.0工艺下发
     *
     * @param param mqtt入参实体类
     */
    private void otcV1ProcessIssue(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            //匹配下发规范模板
            List<JNProcessIssue> list = JSON.parseArray(message, JNProcessIssue.class);
            if (CommonUtils.isNotEmpty(list)) {
                for (JNProcessIssue issue : list) {
                    String gatherNo = issue.getGatherNo();
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
    private void otcV1ProcessClaim(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            JNProcessClaim jnProcessClaim = JSON.parseObject(message, JNProcessClaim.class);
            if (null != jnProcessClaim) {
                String gatherNo = jnProcessClaim.getGatherNo();
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
    private void otcV1PasswordIssue(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            JNPasswordIssue jnPasswordIssue = JSON.parseObject(message, JNPasswordIssue.class);
            if (null != jnPasswordIssue) {
                String gatherNo = jnPasswordIssue.getGatherNo();
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
    private void otcV1CommandIssue(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            JNCommandIssue jnCommandIssue = JSON.parseObject(message, JNCommandIssue.class);
            if (null != jnCommandIssue) {
                String gatherNo = jnCommandIssue.getGatherNo();
                //java对象转16进制字符串
                String str = otcProtocolJoint.jnCommandProtocol(jnCommandIssue);
                otcChannelWrite(gatherNo, str, "----->OTC控制命令下发成功", topic);
            }
        }
    }

    /**
     * OTC程序包路径下发
     *
     * @param param mqtt入参实体类
     */
    private void otcV1IssueProgramPath(MqttParam param) {
        if (null != param) {
            String message = param.getMessage();
            String topic = param.getTopic();
            OtcV1IssueProgramPath otcV1IssueProgramPath = JSON.parseObject(message, OtcV1IssueProgramPath.class);
            if (null != otcV1IssueProgramPath) {
                String gatherNo = otcV1IssueProgramPath.getGatherNo();
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

}
