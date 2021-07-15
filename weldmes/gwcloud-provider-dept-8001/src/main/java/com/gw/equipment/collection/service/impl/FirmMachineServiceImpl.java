package com.gw.equipment.collection.service.impl;

import com.gw.entities.FirmMachineInfo;
import com.gw.equipment.collection.dao.FirmMachineDao;
import com.gw.equipment.collection.service.FirmMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhanghan
 * @Date 2021/7/15 16:43
 * @Description 厂家设备关联业务实现层
 */
@Service
public class FirmMachineServiceImpl implements FirmMachineService {

    @Autowired
    FirmMachineDao firmMachineDao;

    /**
     * @Date 2021/7/15 16:44
     * @Description 绑定厂家、设备
     * @Params firmMachineInfo 厂家设备信息
     */
    @Override
    public void addFirmMachineInfo(FirmMachineInfo firmMachineInfo) {

        firmMachineDao.insertFirmMachineInfo(firmMachineInfo);

    }
}
