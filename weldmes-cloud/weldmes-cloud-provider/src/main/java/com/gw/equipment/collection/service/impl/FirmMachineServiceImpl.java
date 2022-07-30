package com.gw.equipment.collection.service.impl;

import com.gw.entities.FirmMachineInfo;
import com.gw.equipment.collection.dao.FirmMachineDao;
import com.gw.equipment.collection.service.FirmMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void addFirmMachineInfo(List<FirmMachineInfo> firmMachineInfos) {

        //删除表中原有的厂家设备绑定信息
        firmMachineDao.deleteFirmMachineInfo(firmMachineInfos.get(0).getFirmId());

        //查询新的厂家设备绑定信息
        for (FirmMachineInfo firmMachineInfo : firmMachineInfos) {

            firmMachineDao.insertFirmMachineInfo(firmMachineInfo);
        }
    }
}
