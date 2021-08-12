package com.shth.das.sys.rtdata.service.impl;

import com.shth.das.pojo.jnotc.JNRtDataDB;
import com.shth.das.sys.rtdata.mapper.OtcRtDataMapper;
import com.shth.das.sys.rtdata.service.OtcRtDataService;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "ds2TransactionManager", rollbackFor = Exception.class)
public class OtcRtDataServiceImpl implements OtcRtDataService {

    @Autowired
    OtcRtDataMapper otcRtDataMapper;

    @Override
    public int createNewTable(String tableName) {
        return otcRtDataMapper.createNewTable(tableName);
    }

    @Override
    public void insertRtDataList(List<JNRtDataDB> list) {
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> map = new HashMap<>(6);
            String tableName = "rtdata" + DateTimeUtils.getNowDate(DateTimeUtils.CUSTOM_DATE);
            map.put("tableName", tableName);
            map.put("list", list);
            otcRtDataMapper.insertRtDataList(map);
        }
    }
}
