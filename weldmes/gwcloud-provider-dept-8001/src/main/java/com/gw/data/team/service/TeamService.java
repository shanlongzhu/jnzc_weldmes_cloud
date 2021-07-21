package com.gw.data.team.service;

import com.gw.entities.WeldStatisticsData;

import java.util.List;

public interface TeamService {

    List<WeldStatisticsData> getList(String time1, String time2);
}
