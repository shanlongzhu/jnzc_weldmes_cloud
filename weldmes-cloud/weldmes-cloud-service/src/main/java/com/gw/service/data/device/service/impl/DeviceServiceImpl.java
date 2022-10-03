package com.gw.service.data.device.service.impl;

import com.gw.service.data.device.dao.DeviceDao;
import com.gw.service.data.device.service.DeviceService;
import com.gw.entities.WeldStatisticsDataDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceDao deviceDao;


    /**
     * @Date 2021/10/18 15:11
     * @Description 获取设备生产数据列表
     * @Params
     */
    @Override
    public List<WeldStatisticsDataDevice> getList(String time1, String time2,
                                                  String machineNo, List<Long> ids) {

        List<WeldStatisticsDataDevice> list = deviceDao.getList(time1,time2,machineNo,ids);

        return list;
    }

    /**
     * @Date 2021/10/18 15:12
     * @Description 通过 部门id 获取部门名称
     * @Params
     */
    @Override
    public String getName(Long deptId) {

        String deptName = deviceDao.getName(deptId);

        return deptName;
    }
}
