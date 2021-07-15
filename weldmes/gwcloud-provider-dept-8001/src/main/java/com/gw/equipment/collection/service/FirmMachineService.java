package com.gw.equipment.collection.service;

import com.gw.entities.FirmMachineInfo;

/**
 * @Author zhanghan
 * @Date 2021/7/15 16:42
 * @Description 厂家设备关联业务层
 */
public interface FirmMachineService {

    /**
     * @Date 2021/7/15 16:44
     * @Description 绑定厂家、设备
     * @Params firmMachineInfo 厂家设备信息
     */
    public void addFirmMachineInfo(FirmMachineInfo firmMachineInfo);
}
