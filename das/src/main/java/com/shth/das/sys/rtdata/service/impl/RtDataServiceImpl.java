package com.shth.das.sys.rtdata.service.impl;

import com.shth.das.pojo.JNRtDataDB;
import com.shth.das.sys.rtdata.mapper.RtDataMapper;
import com.shth.das.sys.rtdata.service.RtDataService;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "ds2TransactionManager")
public class RtDataServiceImpl implements RtDataService {

    @Autowired
    RtDataMapper rtDataMapper;

    @Override
    public int createNewTable(String tableName) {
        return rtDataMapper.createNewTable(tableName);
    }

    @Override
    public void insertRtDataList(List<JNRtDataDB> list) {
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> map = new HashMap<>();
            String tableName = "rtdata" + DateTimeUtils.getSdfDate.format(System.currentTimeMillis());
            map.put("tableName", tableName);
            map.put("list", list);
            rtDataMapper.insertRtDataList(map);
        }
    }
}
