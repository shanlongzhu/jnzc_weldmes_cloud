package com.shth.das.sys.weldmesdb.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.shth.das.sys.weldmesdb.mapper.StatisticsDataMapper;
import com.shth.das.sys.weldmesdb.service.StatisticsDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "ds1TransactionManager", rollbackFor = Exception.class)
@Slf4j
public class StatisticsDataServiceImpl implements StatisticsDataService {

    @Autowired
    StatisticsDataMapper statisticsDataMapper;

    @Override
    public void insertWeldStatisticsData(String startTime, String endTime, String tableName) {
        try {
            if (!StringUtils.isEmpty(tableName)) {
                statisticsDataMapper.insertWeldStatisticsData(startTime, endTime, tableName);
            }
        } catch (Exception e) {
            log.error("OTC设备实时数据统计到报表中异常：", e);
            throw new RuntimeException();
        }
    }

    @Override
    public void insertSxWeldStatisticsData(String startTime, String endTime, String tableName) {
        try {
            if (!StringUtils.isEmpty(tableName)) {
                statisticsDataMapper.insertSxWeldStatisticsData(startTime, endTime, tableName);
            }
        } catch (Exception e) {
            log.error("松下设备实时数据统计到报表中异常：", e);
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
