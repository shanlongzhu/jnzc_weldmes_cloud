package com.shth.das.common;

import com.shth.das.pojo.db.SxWeldModel;
import com.shth.das.pojo.db.TaskClaimIssue;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 公共MAP
 * @author: Shan Long
 * @create: 2021-08-04
 */
public class CommonMap {

    /**
     * key:（IP+端口）IP:port
     * value：连接通道
     * 保存连接进服务端的通道数量（临时存储）
     */
    public static final Map<String, ChannelHandlerContext> OTC_CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * key:（IP+端口）IP:port
     * value：连接通道
     * 保存连接进服务端的通道数量（临时存储）
     */
    public static final Map<String, ChannelHandlerContext> SX_CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * KEY：采集编号（10进制数字）
     * VALUE: 设备通道（ctx）
     * 每个采集编号都有一个通道
     */
    public static final Map<String, ChannelHandlerContext> OTC_GATHER_NO_CTX_MAP = new ConcurrentHashMap<>();

    /**
     * KEY：客户端地址（ip+port）
     * VALUE: 采集编号（10进制数字）
     */
    public static final Map<String, String> OTC_CTX_GATHER_NO_MAP = new ConcurrentHashMap<>();

    /**
     * KEY：设备CID
     * VALUE: 设备通道（ctx）
     * 每个设备CID都有一个通道
     */
    public static final Map<String, ChannelHandlerContext> SX_WELD_CID_CTX_MAP = new ConcurrentHashMap<>();

    /**
     * KEY：客户端地址（ip+port）
     * VALUE: 设备CID
     * 通道和设备CID进行对应绑定
     */
    public static final Map<String, String> SX_CTX_WELD_CID_MAP = new ConcurrentHashMap<>();

    /**
     * key: 客户端地址（ip+port）
     * value：松下设备信息
     * 松下焊机设备数据暂存
     */
    public static final Map<String, SxWeldModel> SX_CTX_WELD_INFO_MAP = new ConcurrentHashMap<>();

    /**
     * OTC设备领任务存储
     * key:采集编号
     * value:任务信息
     */
    public static final Map<String, TaskClaimIssue> OTC_TASK_CLAIM_MAP = new ConcurrentHashMap<>();

    /**
     * 松下设备领任务存储
     * key:设备CID
     * value：任务信息
     */
    public static final Map<String, TaskClaimIssue> SX_TASK_CLAIM_MAP = new ConcurrentHashMap<>();

    /**
     * OTC设备锁或解锁焊机失败重试次数保存
     * String: 采集编号
     * Map<K,V>:k:控制命令，V：重试次数
     */
    public static final Map<String, Map<Integer, Integer>> OTC_LOCK_FAIL_RETRY_MAP = new ConcurrentHashMap<>();

}
