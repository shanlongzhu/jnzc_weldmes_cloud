package com.shth.das.sys.weldmesdb.service;

import com.shth.das.pojo.WeldOnOffTime;

public interface WeldOnOffTimeService {

    /**
     * 新增设备开关机数据
     *
     * @param weldOnOffTime 设备开关机实体类
     * @return 新增结果
     */
    int insertWeldOnOffTime(WeldOnOffTime weldOnOffTime);

}
