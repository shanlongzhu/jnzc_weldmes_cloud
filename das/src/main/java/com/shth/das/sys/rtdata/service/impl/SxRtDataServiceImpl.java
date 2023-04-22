package com.shth.das.sys.rtdata.service.impl;

import com.shth.das.codeparam.TableStrategy;
import com.shth.das.pojo.db.SxWeldStatisticsData;
import com.shth.das.pojo.jnsx.SxRtDataDb;
import com.shth.das.sys.rtdata.mapper.SxRtDataMapper;
import com.shth.das.sys.rtdata.service.SxRtDataService;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "ds2TransactionManager", rollbackFor = Exception.class)
@Slf4j
public class SxRtDataServiceImpl implements SxRtDataService {

    @Autowired
    private SxRtDataMapper sxRtDataMapper;

    @Override
    public Integer selectTableName(String tableName) {
        return sxRtDataMapper.selectTableName(tableName);
    }

    @Override
    public Integer createNewTable(String tableName) {
        return sxRtDataMapper.createNewTable(tableName);
    }

    @Override
    public void insertSxRtDataList(List<SxRtDataDb> list) {
        if (CommonUtils.isNotEmpty(list)) {
            try {
                String nowDateTime = DateTimeUtils.getNowDateTime();
                String tableName = TableStrategy.getSxTableByDateTime(nowDateTime);
                if (StringUtils.isNotBlank(tableName)) {
                    Map<String, Object> map = new HashMap<>(8);
                    map.put("tableName", tableName);
                    map.put("list", list);
                    sxRtDataMapper.insertSxRtDataList(map);
                }
            } catch (Exception e) {
                log.error("松下实时数据批量插入异常：{}", e.getMessage());
                throw new RuntimeException();
            }
        }
    }

    /**
     * 按时间段统计SX数据
     *
     * @param startTime
     * @param endTime
     * @param tableName
     * @param offset
     * @param size
     * @return
     */
    @Override
    public List<SxWeldStatisticsData> selectSxRtDataByDateTime(String startTime, String endTime, String tableName, Integer offset, Integer size) {
        return sxRtDataMapper.selectSxRtDataByDateTime(startTime, endTime, tableName, offset, size);
    }
}
