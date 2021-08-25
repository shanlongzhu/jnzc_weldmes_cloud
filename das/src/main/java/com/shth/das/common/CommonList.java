package com.shth.das.common;

import com.shth.das.pojo.db.GatherModel;
import com.shth.das.pojo.db.WeldModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据集合
 */
@Component
@Slf4j
public class CommonList {


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
