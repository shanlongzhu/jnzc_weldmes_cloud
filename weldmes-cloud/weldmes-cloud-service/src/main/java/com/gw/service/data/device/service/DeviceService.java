package com.gw.service.data.device.service;
import com.gw.entities.WeldStatisticsDataDevice;

import java.util.List;

public interface DeviceService {

    /**
     * @Date 2021/10/18 15:15
     * @Description 获取设备生产数据 列表
     * @Params
     */
    List<WeldStatisticsDataDevice> getList(String time1, String time2, String machineNo, List<Long> ids);

    /**
     * @Date 2021/10/18 15:15
     * @Description 通过部门id 查询 部门名称
     * @Params
     */
    String getName(Long deptId);
}
