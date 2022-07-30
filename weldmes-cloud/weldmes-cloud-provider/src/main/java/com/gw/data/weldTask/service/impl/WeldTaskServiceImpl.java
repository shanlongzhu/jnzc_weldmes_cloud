package com.gw.data.weldTask.service.impl;

import com.gw.data.weldTask.dao.WeldTaskDao;
import com.gw.data.weldTask.service.WeldTaskService;
import com.gw.entities.WeldStatisticsDataWeldStatics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeldTaskServiceImpl implements WeldTaskService {

    @Autowired
    private WeldTaskDao weldTaskDao;

    /**
     * @Date 2021/10/19 13:52
     * @Description 获取焊机任务信息列表
     * @Params
     */
    @Override
    public List<WeldStatisticsDataWeldStatics> getList(Long areaId, Long teamId, String time1, String time2, Long status) {

        String teamName=weldTaskDao.getTeamName(teamId);

        String valueName=weldTaskDao.getValueName(status);

        List<WeldStatisticsDataWeldStatics> list = weldTaskDao.getList(areaId,teamName,time1,time2,valueName);

        return list;
    }
}
