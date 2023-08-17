package com.shth.das.sys.rtdata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shth.das.codeparam.TableStrategy;
import com.shth.das.pojo.db.OtcWeldStatisticsData;
import com.shth.das.pojo.jnotc.JNRtDataDB;
import com.shth.das.sys.rtdata.mapper.OtcRtDataMapper;
import com.shth.das.sys.rtdata.service.OtcRtDataService;
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
public class OtcRtDataServiceImpl extends ServiceImpl<OtcRtDataMapper, JNRtDataDB> implements OtcRtDataService {

    @Autowired
    private OtcRtDataMapper otcRtDataMapper;

    @Override
    public Integer selectTableName(String tableName) {
        return otcRtDataMapper.selectTableName(tableName);
    }

    @Override
    public Integer createNewTable(String tableName) {
        return otcRtDataMapper.createNewTable(tableName);
    }

    @Override
    public void insertRtDataList(List<JNRtDataDB> list) {
        if (CommonUtils.isNotEmpty(list)) {
            try {
                String nowDateTime = DateTimeUtils.getNowDateTime();
                String tableName = TableStrategy.getOtcTableByDateTime(nowDateTime);
                if (StringUtils.isNotBlank(tableName)) {
                    Map<String, Object> map = new HashMap<>(8);
                    map.put("tableName", tableName);
                    map.put("list", list);
                    otcRtDataMapper.insertRtDataList(map);
                }
            } catch (Exception e) {
                log.error("OTC实时数据批量插入异常：{}", e.getMessage());
                throw new RuntimeException();
            }
        }
    }

    /**
     * 按时间段统计OTC数据
     *
     * @param startTime
     * @param endTime
     * @param tableName
     * @param offset
     * @param size
     * @return
     */
    @Override
    public List<OtcWeldStatisticsData> selectOtcRtDataByDateTime(String startTime, String endTime, String tableName, Integer offset, Integer size) {
        return otcRtDataMapper.selectOtcRtDataByDateTime(startTime, endTime, tableName, offset, size);
    }
}
