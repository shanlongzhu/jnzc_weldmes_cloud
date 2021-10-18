package com.gw.data.device.dao;
import com.gw.entities.WeldStatisticsDataDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Date 2021/10/18 15:13
 * @Description 设备生产数据dao层
 * @Params
 */
@Mapper
public interface DeviceDao {

    /**
     * @Date 2021/10/18 15:14
     * @Description 查询 设备生产数据列表信息
     * @Params
     */
    List<WeldStatisticsDataDevice> getList(@Param("time1") String time1, @Param("time2") String time2, @Param("machineNo") String machineNo, @Param("name") String name);

    /**
     * @Date 2021/10/18 15:14
     * @Description 通过 部门id 查询 部门名称
     * @Params
     */
    String getName(Long deptId);
}
