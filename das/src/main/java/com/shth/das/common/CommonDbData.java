package com.shth.das.common;

import com.shth.das.pojo.GatherModel;
import com.shth.das.pojo.TaskModel;
import com.shth.das.pojo.WeldModel;
import com.shth.das.pojo.WelderModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 公共数据存放
 */
@Component
public class CommonDbData {

    /**
     * 核心线程：100
     * 最大线程：2000
     * 超时时间：30秒
     */
    public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(100, 2000, 30000, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 采集模块数据
     */
    public static List<GatherModel> GATHER_LIST = new ArrayList<>();

    /**
     * 焊机数据
     */
    public static List<WeldModel> WELD_LIST = new ArrayList<>();

    /**
     * 任务数据
     */
    public static List<TaskModel> TASK_LIST = new ArrayList<>();

    /**
     * 焊工数据
     */
    public static List<WelderModel> WELDER_LIST = new ArrayList<>();

}
