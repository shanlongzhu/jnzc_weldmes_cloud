package com.shth.das.sys.weldmesdb.service.impl;

import com.shth.das.codeparam.SysConstants;
import com.shth.das.pojo.db.OtcWeldStatisticsData;
import com.shth.das.pojo.db.SxWeldStatisticsData;
import com.shth.das.sys.rtdata.service.OtcRtDataService;
import com.shth.das.sys.rtdata.service.SxRtDataService;
import com.shth.das.sys.weldmesdb.mapper.StatisticsDataMapper;
import com.shth.das.sys.weldmesdb.service.StatisticsDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(value = "ds1TransactionManager", rollbackFor = Exception.class)
@Slf4j
public class StatisticsDataServiceImpl implements StatisticsDataService {

    @Autowired
    private StatisticsDataMapper statisticsDataMapper;
    @Autowired
    private OtcRtDataService otcRtDataService;
    @Autowired
    private SxRtDataService sxRtDataService;

    @Override
    public void insertWeldStatisticsData(String startTime, String endTime, String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return;
        }
        int count = 0;
        int totalSize = 0;
        try {
            int offset = SysConstants.pageOffset;
            while (true) {
                List<OtcWeldStatisticsData> list = otcRtDataService.selectOtcRtDataByDateTime(startTime, endTime, tableName, offset, SysConstants.pageSize);
                if (ObjectUtils.isEmpty(list)) {
                    break;
                }
                statisticsDataMapper.insertWeldStatisticsData(list);
                offset += SysConstants.pageSize;
                count += 1;
                totalSize += list.size();
            }
            log.info("OTC全部统计完成，当前统计次数：{},统计数据量：{}", count, totalSize);
        } catch (Exception e) {
            log.error("OTC设备实时数据统计到报表中异常：", e);
            throw new RuntimeException();
        }
    }

    @Override
    public void insertSxWeldStatisticsData(String startTime, String endTime, String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return;
        }
        int count = 0;
        int totalSize = 0;
        try {
            int offset = SysConstants.pageOffset;
            while (true) {
                List<SxWeldStatisticsData> list = sxRtDataService.selectSxRtDataByDateTime(startTime, endTime, tableName, offset, SysConstants.pageSize);
                if (ObjectUtils.isEmpty(list)) {
                    break;
                }
                statisticsDataMapper.insertSxWeldStatisticsData(list);
                offset += SysConstants.pageSize;
                count += 1;
                totalSize += list.size();
            }
            log.info("松下全部统计完成，当前统计次数：{},统计数据量：{}", count, totalSize);
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
