package com.gw.equipment.collection.dao;

import com.gw.entities.FirmMachineInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author zhanghan
 * @Date 2021/7/15 16:42
 * @Description 厂家设备关联dao层
 */
@Mapper
public interface FirmMachineDao {

    /**
     * @Date 2021/7/15 16:44
     * @Description 绑定厂家、设备
     * @Params firmMachineInfo 厂家设备信息
     */
    public void insertFirmMachineInfo(@Param("firmMachineInfo") FirmMachineInfo firmMachineInfo);
}
