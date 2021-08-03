package com.gw.data.productionTask.service;

import com.gw.entities.RealtimeData;
import com.gw.entities.WeldStatisticsData;

import java.util.List;

public interface ProductionTaskService {
    List<WeldStatisticsData> getList(Long areaId, Long teamId, String welderNo, String junctionNo, String time1, String time2);
}
