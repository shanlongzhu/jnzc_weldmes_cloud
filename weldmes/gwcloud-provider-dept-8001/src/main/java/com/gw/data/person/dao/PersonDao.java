package com.gw.data.person.dao;

import com.gw.entities.WeldStatisticsData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonDao {
    List<WeldStatisticsData> getList(@Param("time1") String time1, @Param("time2") String time2, @Param("welderNo") String welderNo, @Param("welderName") String welderName, @Param("name") String name);

    String getDeptId(Long deptId);
}
