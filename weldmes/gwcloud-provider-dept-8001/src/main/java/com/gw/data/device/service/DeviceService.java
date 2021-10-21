package com.gw.data.device.service;
import com.gw.entities.WeldStatisticsDataDevice;

import java.util.List;

/**
 * @Date 2021/10/18 15:14
 * @Description 设备生产数据业务层
 * @Params
 */
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
