package com.shth.das;

import com.shth.das.codeparam.SysConstants;
import com.shth.das.pojo.db.OtcWeldStatisticsData;
import com.shth.das.pojo.db.SxWeldStatisticsData;
import com.shth.das.sys.rtdata.service.OtcRtDataService;
import com.shth.das.sys.rtdata.service.SxRtDataService;
import com.shth.das.sys.weldmesdb.service.StatisticsDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class StatisticsDataServiceTest extends BaseTest {

    @Autowired
    private StatisticsDataService statisticsDataService;
    @Autowired
    private OtcRtDataService otcRtDataService;
    @Autowired
    private SxRtDataService sxRtDataService;

    @Test
    public void testOtcSelect() {
        //测试按时间段统计OTC数据接口
        String startTime = "2023-03-28 00:00:00";
        String endTime = "2023-03-28 18:00:00";
        String tableName = "otcrtd20230328";
        List<OtcWeldStatisticsData> list = otcRtDataService.selectOtcRtDataByDateTime(startTime, endTime, tableName, SysConstants.pageOffset, SysConstants.pageSize);
        System.out.println("list:" + list.size());
    }

    @Test
    public void testSxSelect() {
        //测试按时间段统计SX数据接口
        String startTime = "2023-03-28 00:00:00";
        String endTime = "2023-03-28 18:00:00";
        String tableName = "sxrtd20220108";
        List<SxWeldStatisticsData> list = sxRtDataService.selectSxRtDataByDateTime(startTime, endTime, tableName, SysConstants.pageOffset, SysConstants.pageSize);
        System.out.println("list:" + list.size());
    }

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
        //测试统计SX数据接口
        String startTime = "2022-01-08 00:00:00";
        String endTime = "2022-01-08 18:00:00";
        String tableName = "sxrtd20220108";
        statisticsDataService.insertSxWeldStatisticsData(startTime, endTime, tableName);
    }

}
