package com.gw.data.device.dao;

import com.gw.entities.RealtimeData;
import com.gw.entities.WeldStatisticsData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceDao {
    List<WeldStatisticsData> getList(@Param("areaId") Long areaId, @Param("teamName") String teamName, @Param("time1") String time1, @Param("time2") String time2);

    String getTeamName(Long teamId);
}
