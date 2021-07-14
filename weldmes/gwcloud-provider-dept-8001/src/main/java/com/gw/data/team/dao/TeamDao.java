package com.gw.data.team.dao;

import com.gw.entities.RealtimeData;
import com.gw.entities.weldStatisticsData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface TeamDao {

    List<weldStatisticsData> getList(@Param("time1") String time1, @Param("time2") String time2);
}
