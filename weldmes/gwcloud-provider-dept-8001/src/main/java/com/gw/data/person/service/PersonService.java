package com.gw.data.person.service;

import com.gw.entities.RealtimeData;
import com.gw.entities.WeldStatisticsData;

import java.util.List;

public interface PersonService {
    List<WeldStatisticsData> getList(String time1, String time2);
}
