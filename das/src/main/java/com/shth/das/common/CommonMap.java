package com.shth.das.common;

import com.shth.das.pojo.db.SxWeldModel;
import com.shth.das.pojo.db.TaskClaimIssue;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 公共MAP
 * @author: Shan Long
 * @create: 2021-08-04
 */
public class CommonMap {

    /**
     * key:客户端IP地址
     * value:连接通道
     * 保存连接进服务端的通道数量
     */
    public static volatile ConcurrentHashMap<String, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * key:客户端IP地址
     * value:采集编号
     * 采集盒的IP地址和采集编号对应关系(用来向前端发送关机数据)
     */
    public static volatile ConcurrentHashMap<String, String> CLIENT_IP_GATHER_NO_MAP = new ConcurrentHashMap<>();

    /**
     * key：客户端IP地址
     * value：松下设备信息
     * 松下焊机设备数据暂存
     */
    public static volatile ConcurrentHashMap<String, SxWeldModel> SX_CLIENT_IP_BIND_WELD_INFO = new ConcurrentHashMap<>();

    /**
     * OTC设备领任务存储
     * key:采集编号
     * value:任务信息
     */
    public static volatile ConcurrentHashMap<String, TaskClaimIssue> OTC_TASK_CLAIM_MAP = new ConcurrentHashMap<>();
    /**
     * 松下设备领任务存储
     * key:设备IP
     * value：任务信息
     */
    public static volatile ConcurrentHashMap<String, TaskClaimIssue> SX_TASK_CLAIM_MAP = new ConcurrentHashMap<>();
}
