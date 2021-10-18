package com.gw.data.team.service;
import com.gw.entities.WeldStatisticsDataTeam;

import java.util.List;

public interface TeamService {

    List<WeldStatisticsDataTeam> getList(String time1, String time2, String deptId);
}
