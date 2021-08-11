package com.gw.data.device.service;
import com.gw.entities.WeldStatisticsData;
import java.util.List;

public interface DeviceService {

    List<WeldStatisticsData> getList(String time1, String time2,String machineNo,String name);

    String getName(Long deptId);
}
