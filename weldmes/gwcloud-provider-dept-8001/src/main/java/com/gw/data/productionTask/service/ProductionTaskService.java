package com.gw.data.productionTask.service;

import com.gw.entities.RealtimeData;
import com.gw.entities.WeldStatisticsData;

import java.util.List;

public interface ProductionTaskService {
    List<WeldStatisticsData> getList(String time1, String time2,String welderNo,String welderName,String machineNo,String taskNo,String name);

    String getName(Long deptId);
}
