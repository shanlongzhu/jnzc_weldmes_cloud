package com.gw.data.team.service;

import com.gw.entities.RealtimeData;
import com.gw.entities.weldStatisticsData;

import java.util.List;

public interface TeamService {

    List<weldStatisticsData> getList(String time1, String time2);
}
