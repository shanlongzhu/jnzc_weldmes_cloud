package com.gw.data.device.service.impl;

import com.gw.data.device.dao.DeviceDao;
import com.gw.data.device.service.DeviceService;
import com.gw.entities.RealtimeData;
import com.gw.entities.WeldStatisticsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceDao deviceDao;


    @Override
    public List<WeldStatisticsData> getList(String time1, String time2,String machineNo,String name) {
        return deviceDao.getList(time1,time2,machineNo,name);
    }

    @Override
    public String getName(Long deptId) {
        return deviceDao.getName(deptId);
    }
}
