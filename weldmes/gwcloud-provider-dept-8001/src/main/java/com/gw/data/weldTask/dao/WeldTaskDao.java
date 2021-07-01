package com.gw.data.weldTask.dao;

import com.gw.entities.RealtimeData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WeldTaskDao {
    List<RealtimeData> getList(@Param("areaId") Long areaId,@Param("teamName") String teamName, @Param("time1") String time1, @Param("time2") String time2,@Param("valueName") String valueName);

    String getTeamName(Long teamId);

    String getValueName(Long status);




}
