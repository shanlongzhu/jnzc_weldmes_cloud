package com.shth.das.common;

import com.shth.das.pojo.db.*;
import com.shth.das.pojo.jnotc.JNRtDataDB;
import com.shth.das.pojo.jnsx.SxRtDataDb;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 公共数据存放
 */
@Component
@Slf4j
public class CommonDbData {

    /**
     * 阻塞队列存储实时数据，并定时同步到MySQL数据库
     */
    public static final LinkedBlockingQueue<JNRtDataDB> OTC_LINKED_BLOCKING_QUEUE = new LinkedBlockingQueue<>(10000);
    /**
     * 阻塞队列存储实时数据，并定时同步到MySQL数据库
     */
    public static final LinkedBlockingQueue<SxRtDataDb> SX_LINKED_BLOCKING_QUEUE = new LinkedBlockingQueue<>(10000);

    /**
     * OTC开机设备阻塞队列
     */
    public static final LinkedBlockingQueue<OtcMachineQueue> OTC_ON_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

    /**
     * OTC关机设备阻塞队列
     */
    public static final LinkedBlockingQueue<OtcMachineQueue> OTC_OFF_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

    /**
     * 松下设备新增到阻塞队列
     */
    public static final LinkedBlockingQueue<SxWeldModel> SX_ADD_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

    /**
     * 松下开机设备阻塞队列
     * String:焊机IP
     */
    public static final LinkedBlockingQueue<String> SX_ON_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

    /**
     * 松下关机设备阻塞队列
     */
    public static final LinkedBlockingQueue<String> SX_OFF_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

    /**
     * 执行THREAD_POOL_EXECUTOR多出的任务
     */
    private static final ThreadPoolExecutor CUSTOM_THREAD_POOL = new ThreadPoolExecutor(10, 200, 30000, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.DiscardOldestPolicy());

    /**
     * 核心线程：100
     * 最大线程：2000
     * 超时时间：30秒
     */
    public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(100, 2000, 30000, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(10), new CustomRejectedExecutionHandler());

    /**
     * 采集模块数据
     */
    private volatile static List<GatherModel> GATHER_LIST = new ArrayList<>();

    /**
     * 焊机数据
     */
    private volatile static List<WeldModel> WELD_LIST = new ArrayList<>();

    /**
     * 任务数据
     */
    //private static List<TaskModel> TASK_LIST = new ArrayList<>();

    /**
     * 焊工数据
     */
    //private static List<WelderModel> WELDER_LIST = new ArrayList<>();

    /**
     * OTC设备领任务存储
     * key:采集编号
     * value:任务信息
     */
    public static ConcurrentHashMap<String, TaskClaimIssue> OTC_TASK_CLAIM_MAP = new ConcurrentHashMap<>();
    /**
     * 松下设备领任务存储
     * key:设备IP
     * value：任务信息
     */
    public static ConcurrentHashMap<String, TaskClaimIssue> SX_TASK_CLAIM_MAP = new ConcurrentHashMap<>();

    /**
     * 自定义拒绝策略
     */
    private static class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            CUSTOM_THREAD_POOL.execute(r);
            log.error("[THREAD_POOL_EXECUTOR]-->创建过最大线程数：{} -- 当前线程数：{} -- 活跃线程数：{} -- 队列数量：{}",
                    executor.getLargestPoolSize(), executor.getPoolSize(), executor.getActiveCount(), executor.getQueue().size());
            log.error("[CUSTOM_THREAD_POOL]-->创建过最大线程数：{} -- 当前线程数：{} -- 活跃线程数：{} -- 队列数量：{}",
                    CUSTOM_THREAD_POOL.getLargestPoolSize(), CUSTOM_THREAD_POOL.getPoolSize(), CUSTOM_THREAD_POOL.getActiveCount(), CUSTOM_THREAD_POOL.getQueue().size());
        }
    }

    public static List<GatherModel> getGatherList() {
        return GATHER_LIST;
    }

    public synchronized static void setGatherList(List<GatherModel> gatherList) {
        GATHER_LIST = gatherList;
    }

    public static List<WeldModel> getWeldList() {
        return WELD_LIST;
    }

    public synchronized static void setWeldList(List<WeldModel> weldList) {
        WELD_LIST = weldList;
    }

}
