package com.gw.data.team.dao;

import com.gw.entities.WeldStatisticsData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface TeamDao {

    List<WeldStatisticsData> getList(@Param("time1") String time1, @Param("time2") String time2);
}
