package com.gw.data.device.service;

import com.gw.entities.RealtimeData;
import com.gw.entities.WeldStatisticsData;

import java.util.List;

public interface DeviceService {


    List<WeldStatisticsData> getList(Long areaId , Long teamId, String time1, String time2);
}
