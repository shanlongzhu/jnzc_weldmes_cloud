package com.gw.service.equipment.collection.service;

import com.gw.entities.FirmMachineInfo;

import java.util.List;

public interface FirmMachineService {

    /**
     * @Date 2021/7/15 16:44
     * @Description 绑定厂家、设备
     * @Params firmMachineInfo 厂家设备信息
     */
    public void addFirmMachineInfo(List<FirmMachineInfo> firmMachineInfos);
}
