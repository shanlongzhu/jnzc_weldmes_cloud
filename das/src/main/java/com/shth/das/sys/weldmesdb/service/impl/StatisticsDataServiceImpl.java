package com.shth.das.sys.weldmesdb.service.impl;

import com.shth.das.sys.weldmesdb.mapper.StatisticsDataMapper;
import com.shth.das.sys.weldmesdb.service.StatisticsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "ds1TransactionManager", rollbackFor = Exception.class)
public class StatisticsDataServiceImpl implements StatisticsDataService {

    @Autowired
    StatisticsDataMapper statisticsDataMapper;

    @Override
    public void insertWeldStatisticsData(String startTime, String endTime, String tableName) {
        statisticsDataMapper.insertWeldStatisticsData(startTime, endTime, tableName);
    }

    @Override
    public void insertSxWeldStatisticsData(String startTime, String endTime, String tableName) {
        statisticsDataMapper.insertSxWeldStatisticsData(startTime, endTime, tableName);
    }

    @Override
    public String selectOtcMaxEndTime() {
        return statisticsDataMapper.selectOtcMaxEndTime();
    }

    @Override
    public String selectSxMaxEndTime() {
        return statisticsDataMapper.selectSxMaxEndTime();
    }
}
