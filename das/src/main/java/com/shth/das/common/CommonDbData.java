package com.shth.das.common;

import com.shth.das.pojo.GatherModel;
import com.shth.das.pojo.WeldModel;
import com.shth.das.pojo.TaskModel;
import com.shth.das.pojo.WelderModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 公共数据存放
 */
public class CommonDbData {

    /**
     * 核心线程：500
     * 最大线程：2000
     * 超时时间：30秒
     */
    public static final ThreadPoolExecutor executor = new ThreadPoolExecutor(500, 2000, 30000, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 采集模块数据
     */
    public static List<GatherModel> gatherList = new ArrayList<>();

    /**
     * 焊机数据
     */
    public static List<WeldModel> weldList = new ArrayList<>();

    /**
     * 任务数据
     */
    public static List<TaskModel> taskList = new ArrayList<>();

    /**
     * 焊工数据
     */
    public static List<WelderModel> welderList = new ArrayList<>();

}
