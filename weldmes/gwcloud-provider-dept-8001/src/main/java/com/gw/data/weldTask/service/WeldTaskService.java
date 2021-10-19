package com.gw.data.weldTask.service;

import com.gw.entities.WeldStatisticsDataWeldStatics;

import java.util.List;
/**
 * @Date 2021/10/19 13:52
 * @Description 焊机任务业务层
 * @Params
 */
public interface WeldTaskService {

    /**
     * @Date 2021/10/19 13:52
     * @Description 获取焊机任务信息列表
     * @Params
     */
    List<WeldStatisticsDataWeldStatics> getList(Long areaId, Long teamId, String time1, String time2, Long status);
}
