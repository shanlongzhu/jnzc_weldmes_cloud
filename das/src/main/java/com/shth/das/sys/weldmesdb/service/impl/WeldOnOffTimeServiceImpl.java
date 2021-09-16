package com.shth.das.sys.weldmesdb.service.impl;

import com.shth.das.pojo.db.WeldOnOffTime;
import com.shth.das.sys.weldmesdb.mapper.WeldOnOffTimeMapper;
import com.shth.das.sys.weldmesdb.service.WeldOnOffTimeService;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Transactional(value = "ds1TransactionManager", rollbackFor = Exception.class)
@Slf4j
public class WeldOnOffTimeServiceImpl implements WeldOnOffTimeService {

    @Autowired
    WeldOnOffTimeMapper weldOnOffTimeMapper;

    @Override
    public void insertWeldOnOffTime(WeldOnOffTime weldOnOffTime) {
        try {
            weldOnOffTimeMapper.insert(weldOnOffTime);
        } catch (Exception e) {
            log.error("设备开关机时间新增存DB异常：", e);
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
                    String startTime = onOffTime.getStartTime();
                    if (CommonUtils.isNotEmpty(startTime) && CommonUtils.isNotEmpty(weldOnOffTime.getEndTime())) {
                        if (startTime.length() > 19) {
                            startTime = startTime.substring(0, 19);
                        }
                        final LocalDateTime starttime = LocalDateTime.parse(startTime, DateTimeUtils.DEFAULT_DATETIME);
                        final LocalDateTime endtime = LocalDateTime.parse(weldOnOffTime.getEndTime(), DateTimeUtils.DEFAULT_DATETIME);
                        //时间差（单位：毫秒）
                        final long difference = Duration.between(starttime, endtime).toMillis();
                        //赋值时除1000，毫秒转换为秒
                        onOffTime.setWeldOnDuration(BigInteger.valueOf(difference / 1000));
                    }
                    onOffTime.setEndTime(weldOnOffTime.getEndTime());
                    weldOnOffTimeMapper.updateById(onOffTime);
                }
            }
            //判断是否是松下设备
            if (weldOnOffTime.getMachineType() == 1) {
                //根据设备CID查询开机时间最近的一条记录
                WeldOnOffTime onOffTime = weldOnOffTimeMapper.selectWeldByWeldCid(weldOnOffTime);
                if (null != onOffTime) {
                    String startTime = onOffTime.getStartTime();
                    if (CommonUtils.isNotEmpty(startTime) && CommonUtils.isNotEmpty(weldOnOffTime.getEndTime())) {
                        if (startTime.length() > 19) {
                            startTime = startTime.substring(0, 19);
                        }
                        final LocalDateTime starttime = LocalDateTime.parse(startTime, DateTimeUtils.DEFAULT_DATETIME);
                        final LocalDateTime endtime = LocalDateTime.parse(weldOnOffTime.getEndTime(), DateTimeUtils.DEFAULT_DATETIME);
                        //时间差（单位：毫秒）
                        final long difference = Duration.between(starttime, endtime).toMillis();
                        //赋值时除1000，毫秒转换为秒
                        onOffTime.setWeldOnDuration(BigInteger.valueOf(difference / 1000));
                    }
                    onOffTime.setEndTime(weldOnOffTime.getEndTime());
                    weldOnOffTimeMapper.updateById(onOffTime);
                }
            }
        } catch (Exception e) {
            log.error("设备开关机时间修改存DB异常：", e);
            throw new RuntimeException();
        }
    }
}
