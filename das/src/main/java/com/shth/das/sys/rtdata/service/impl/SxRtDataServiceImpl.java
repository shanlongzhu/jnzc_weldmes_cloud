package com.shth.das.sys.rtdata.service.impl;

import com.shth.das.pojo.jnsx.SxRtDataDb;
import com.shth.das.sys.rtdata.mapper.SxRtDataMapper;
import com.shth.das.sys.rtdata.service.SxRtDataService;
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
public class SxRtDataServiceImpl implements SxRtDataService {

    @Autowired
    SxRtDataMapper sxRtDataMapper;

    @Override
    public int createNewTable(String tableName) {
        return sxRtDataMapper.createNewTable(tableName);
    }

    @Override
    public void insertSxRtDataList(List<SxRtDataDb> list) {
        if (CommonUtils.isNotEmpty(list)) {
            try {
                Map<String, Object> map = new HashMap<>(6);
                String tableName = "sxrtd" + DateTimeUtils.getNowDate(DateTimeUtils.CUSTOM_DATE);
                map.put("tableName", tableName);
                map.put("list", list);
                sxRtDataMapper.insertSxRtDataList(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
