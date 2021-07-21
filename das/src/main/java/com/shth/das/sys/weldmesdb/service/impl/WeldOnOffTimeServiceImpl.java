package com.shth.das.sys.weldmesdb.service.impl;

import com.shth.das.pojo.db.WeldOnOffTime;
import com.shth.das.sys.weldmesdb.mapper.WeldOnOffTimeMapper;
import com.shth.das.sys.weldmesdb.service.WeldOnOffTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "ds1TransactionManager", rollbackFor = Exception.class)
public class WeldOnOffTimeServiceImpl implements WeldOnOffTimeService {

    @Autowired
    WeldOnOffTimeMapper weldOnOffTimeMapper;

    @Override
    public int insertWeldOnOffTime(WeldOnOffTime weldOnOffTime) {
        return weldOnOffTimeMapper.insert(weldOnOffTime);
    }
}
