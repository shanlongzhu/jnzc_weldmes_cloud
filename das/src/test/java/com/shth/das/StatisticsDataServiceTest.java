package com.shth.das;

import com.shth.das.sys.weldmesdb.service.StatisticsDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class StatisticsDataServiceTest extends BaseTest {

    @Autowired
    private StatisticsDataService statisticsDataService;


    @Test
    public void testOtc() {
        //测试统计OTC数据接口
        String startTime = "2023-03-28 00:00:00";
        String endTime = "2023-03-28 18:00:00";
        String tableName = "otcrtd20230328";
        statisticsDataService.insertWeldStatisticsData(startTime, endTime, tableName);
    }

    @Test
    public void testSx() {
        //测试统计OTC数据接口
        String startTime = "2022-01-08 00:00:00";
        String endTime = "2022-01-08 18:00:00";
        String tableName = "sxrtd20220108";
        statisticsDataService.insertSxWeldStatisticsData(startTime, endTime, tableName);
    }

}
