package com.gw.data.historicalCurve.service;

import com.gw.entities.RtData;

import java.text.ParseException;
import java.util.List;

public interface HistoricalCurveService {

    /**
     * @Date 2021/7/14 8:57
     * @Description 焊机历史曲线
     * @Params startTime 开始时间  endTime 结束时间  taskId 任务id  welderId 焊工id  weldMachineId 焊机id
     */
    List<RtData> getList(String startTime, String endTime,Long taskId,Long welderId,Long weldMachineId) throws ParseException;

}
