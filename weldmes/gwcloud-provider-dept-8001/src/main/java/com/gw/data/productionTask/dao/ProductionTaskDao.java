package com.gw.data.productionTask.dao;

import com.gw.entities.RealtimeData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//@Mapper
public interface ProductionTaskDao {
    List<RealtimeData> getList(@Param("areaId") Long areaId, @Param("teamName") String teamName, @Param("welderNo") String welderNo, @Param("junctionNo") String junctionNo, @Param("time1") String time1, @Param("time2") String time2);

    String getTeamName(Long teamId);
}
