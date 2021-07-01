package com.gw.data.productionTask.service;

import com.gw.entities.RealtimeData;

import java.util.List;

public interface ProductionTaskService {
    List<RealtimeData> getList(Long areaId, Long teamId, String welderNo, String junctionNo, String time1, String time2);
}
