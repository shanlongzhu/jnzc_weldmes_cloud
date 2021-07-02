package com.gw.data.weldTask.service;

import com.gw.entities.RealtimeData;

import java.util.List;

public interface WeldTaskService {
    List<RealtimeData> getList(Long areaId,Long teamId,String time1, String time2,Long status);
}
