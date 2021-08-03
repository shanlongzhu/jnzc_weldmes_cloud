package com.gw.data.weldTask.service.impl;

import com.gw.data.weldTask.dao.WeldTaskDao;
import com.gw.data.weldTask.service.WeldTaskService;
import com.gw.entities.RealtimeData;
import com.gw.entities.WeldStatisticsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeldTaskServiceImpl implements WeldTaskService {

    @Autowired
    private WeldTaskDao weldTaskDao;

    @Override
    public List<WeldStatisticsData> getList(Long areaId, Long teamId, String time1, String time2, Long status) {
        String teamName=weldTaskDao.getTeamName(teamId);
        String valueName=weldTaskDao.getValueName(status);
        return weldTaskDao.getList(areaId,teamName,time1,time2,valueName);
    }
}
