package com.shth.das.common;

import com.shth.das.pojo.db.GatherModel;
import com.shth.das.pojo.db.WeldModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 数据集合
 */
@Component
@Slf4j
public class CommonList {


    /**
     * 采集模块数据
     */
    private static final List<GatherModel> GATHER_LIST = new CopyOnWriteArrayList<>();

    /**
     * 焊机数据
     */
    private static final List<WeldModel> WELD_LIST = new CopyOnWriteArrayList<>();

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

    public static void setGatherList(List<GatherModel> gatherList) {
        GATHER_LIST.clear();
        GATHER_LIST.addAll(gatherList);
    }

    public static List<WeldModel> getWeldList() {
        return WELD_LIST;
    }

    public synchronized static void setWeldList(List<WeldModel> weldList) {
        WELD_LIST.clear();
        WELD_LIST.addAll(weldList);
    }

}
