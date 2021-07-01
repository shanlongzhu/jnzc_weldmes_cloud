package com.gw.data.device.service.impl;

import com.gw.data.device.dao.DeviceDao;
import com.gw.data.device.service.DeviceService;
import com.gw.entities.RealtimeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceDao deviceDao;


    @Override
    public List<RealtimeData> getList(Long areaId ,Long teamId,String time1, String time2) {
        String teamName=deviceDao.getTeamName(teamId);
        return deviceDao.getList(areaId,teamName,time1,time2);
    }
}