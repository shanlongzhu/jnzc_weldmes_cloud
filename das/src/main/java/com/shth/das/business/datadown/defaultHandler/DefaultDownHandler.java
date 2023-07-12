package com.shth.das.business.datadown.defaultHandler;

import com.alibaba.fastjson2.JSON;
import com.shth.das.business.datadown.BaseProtocol;
import com.shth.das.business.datadown.sx.SxProtocolJoint;
import com.shth.das.codeparam.MqttParam;
import com.shth.das.common.CommonFunction;
import com.shth.das.common.CommonMap;
import com.shth.das.common.DownTopicEnum;
import com.shth.das.common.GainTopicName;
import com.shth.das.pojo.db.TaskClaimIssue;
import com.shth.das.pojo.jnsx.SxWeldChannelSetting;
import com.shth.das.util.CommonUtils;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class DefaultDownHandler extends BaseProtocol {

    @Override
    protected void register(Map<String, Consumer<MqttParam>> map) {
        //焊工刷卡领取任务[解锁焊机]
        map.put(GainTopicName.getMqttDownTopicName(DownTopicEnum.TaskClaimIssue), this::taskClaimIssue);
    }

    /**
     * 焊工刷卡领取任务
     *
     * @param param mqtt入参实体类
     */
    private void taskClaimIssue(MqttParam param) {
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

}
