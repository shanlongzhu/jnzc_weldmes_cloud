package com.gw.data.productionTask.service;

import com.gw.entities.WeldStatisticsDataProductionTask;

import java.util.List;

/**
 * @Date 2021/10/18 17:25
 * @Description 生产任务详情业务层
 * @Params
 */
public interface ProductionTaskService {

    /**
     * @Date 2021/10/18 17:26
     * @Description 获取生产任务详情数据列表
     * @Params
     */
    List<WeldStatisticsDataProductionTask> getList(String time1, String time2, String welderNo, String welderName,
                                                   String machineNo, String taskNo, List<Long> ids);

}
