package com.gw.data.person.service;

import com.gw.entities.WeldStatisticsData;

import java.util.List;

public interface PersonService {
    List<WeldStatisticsData> getList(String time1, String time2,String welderNo,String welderName,String name);

    String getDeptId(Long deptId);
}
