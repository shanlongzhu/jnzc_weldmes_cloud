package com.gw.data.historicalCurve.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.gw.entities.RtData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("slave_1")
public interface HistoricalCurveDao {
    List<RtData> getList(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("list") List<String> list);

    List<RtData> get();
}
