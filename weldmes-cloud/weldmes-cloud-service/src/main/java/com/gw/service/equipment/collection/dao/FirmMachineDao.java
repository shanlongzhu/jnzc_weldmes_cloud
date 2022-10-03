package com.gw.service.equipment.collection.dao;

import com.gw.entities.FirmMachineInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FirmMachineDao {

    /**
     * @Date 2021/7/15 16:44
     * @Description 绑定厂家、设备
     * @Params firmMachineInfo 厂家设备信息
     */
    public void insertFirmMachineInfo(@Param("firmMachineInfo") FirmMachineInfo firmMachineInfo);

    /**
     * @Date 2021/8/10 14:20
     * @Description 根据厂家id删除 厂家设备绑定信息
     * @Params
     */
    public void deleteFirmMachineInfo(@Param("firmId") Long firmId);
}
