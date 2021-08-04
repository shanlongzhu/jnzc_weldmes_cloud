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
        try {
            statisticsDataMapper.insertWeldStatisticsData(startTime, endTime, tableName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void insertSxWeldStatisticsData(String startTime, String endTime, String tableName) {
        try {
            statisticsDataMapper.insertSxWeldStatisticsData(startTime, endTime, tableName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
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
