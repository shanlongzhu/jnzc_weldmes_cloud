package com.gw.data.person.dao;

import com.gw.entities.RealtimeData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonDao {
    List<RealtimeData> getList(@Param("time1") String time1, @Param("time2") String time2);
}
