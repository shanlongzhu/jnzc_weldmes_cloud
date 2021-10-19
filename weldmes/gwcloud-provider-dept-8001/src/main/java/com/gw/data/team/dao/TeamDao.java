package com.gw.data.team.dao;

import com.gw.entities.WeldStatisticsDataTeam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface TeamDao {

    List<WeldStatisticsDataTeam> getList(@Param("time1") String time1, @Param("time2") String time2, @Param("ids")List<Long> ids);
}
