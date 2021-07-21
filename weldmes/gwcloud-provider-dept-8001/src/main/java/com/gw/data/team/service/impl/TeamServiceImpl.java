package com.gw.data.team.service.impl;
import com.gw.data.team.dao.TeamDao;
import com.gw.data.team.service.TeamService;
import com.gw.entities.WeldStatisticsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamDao teamDao;


    @Override
    public List<WeldStatisticsData> getList(String time1, String time2) {
        return teamDao.getList(time1,time2);
    }
}
