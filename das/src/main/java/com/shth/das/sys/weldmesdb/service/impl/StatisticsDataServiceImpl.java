package com.shth.das.sys.weldmesdb.service.impl;

import com.shth.das.sys.weldmesdb.mapper.StatisticsDataMapper;
import com.shth.das.sys.weldmesdb.service.StatisticsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsDataServiceImpl implements StatisticsDataService {

    @Autowired
    StatisticsDataMapper statisticsDataMapper;

    @Override
    public int insertWeldStatisticsData(String startTime, String endTime, String tableName) {
        return statisticsDataMapper.insertWeldStatisticsData(startTime, endTime, tableName);
    }

    @Override
    public String selectMaxEndTime() {
        return statisticsDataMapper.selectMaxEndTime();
    }
}
