package com.shth.das.sys.weldmesdb.service;

import com.shth.das.pojo.db.WeldOnOffTime;

public interface WeldOnOffTimeService {

    /**
     * 新增设备开关机时间
     *
     * @param weldOnOffTime 设备开关机实体类
     */
    void insertWeldOnOffTime(WeldOnOffTime weldOnOffTime);

    /**
     * 修改开关机时间
     *
     * @param weldOnOffTime 设备开关机实体类
     */
    void updateWeldOnOffTime(WeldOnOffTime weldOnOffTime);

}
