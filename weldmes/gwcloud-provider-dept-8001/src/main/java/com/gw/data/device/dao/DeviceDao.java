package com.gw.data.device.dao;
import com.gw.entities.WeldStatisticsData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceDao {
    List<WeldStatisticsData> getList(@Param("time1") String time1, @Param("time2") String time2, @Param("machineNo") String machineNo,@Param("name") String name);

    String getName(Long deptId);
}
