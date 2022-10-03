package com.gw.service.data.historicalCurve.service;

import com.gw.entities.RtData;
import com.gw.entities.TableInfo;

import java.util.List;
import java.util.Map;

public interface HistoricalCurveService {

    /**
     * @Date 2021/7/14 8:57
     * @Description 焊机历史曲线   电流  电压
     * @Params startTime 开始时间  endTime 结束时间  taskId 任务id  welderId 焊工id  weldMachineId 焊机id
     */
    Map<String, Object> getList(TableInfo tableInfo);

    /**
     * @Date 2021/7/14 8:57
     * @Description 焊机历史曲线信息列表
     * @Params startTime 开始时间  endTime 结束时间  taskId 任务id  welderId 焊工id  weldMachineId 焊机id
     */
    List<RtData> getHistoryCurveInfos(TableInfo tableInfo);

    /**
     * @Date 2021/7/15 17:46
     * @Description 根据表名 查询 焊机历史曲线
     * @Params
     */
    public Map<String, Object> getHistoryCurveInfoByTableName(TableInfo tableInfo);

}
