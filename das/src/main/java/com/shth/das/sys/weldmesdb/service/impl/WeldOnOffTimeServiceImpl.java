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
    public void insertWeldOnOffTime(WeldOnOffTime weldOnOffTime) {
        try {
            weldOnOffTimeMapper.insert(weldOnOffTime);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void updateWeldOnOffTime(WeldOnOffTime weldOnOffTime) {
        try {
            //判断是否是OTC设备
            if (weldOnOffTime.getMachineType() == 0) {
                //根据采集编号查询开机时间最近的一条记录
                WeldOnOffTime onOffTime = weldOnOffTimeMapper.selectWeldByGatherNo(weldOnOffTime);
                if (null != onOffTime) {
                    onOffTime.setEndTime(weldOnOffTime.getEndTime());
                    weldOnOffTimeMapper.updateById(onOffTime);
                }
            }
            //判断是否是松下设备
            if (weldOnOffTime.getMachineType() == 1) {
                //根据采集编号查询开机时间最近的一条记录
                WeldOnOffTime onOffTime = weldOnOffTimeMapper.selectWeldByWeldIp(weldOnOffTime);
                if (null != onOffTime) {
                    onOffTime.setEndTime(weldOnOffTime.getEndTime());
                    weldOnOffTimeMapper.updateById(onOffTime);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
