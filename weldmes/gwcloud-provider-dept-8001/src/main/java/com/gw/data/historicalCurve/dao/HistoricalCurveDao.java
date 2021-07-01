package com.gw.data.historicalCurve.dao;

import com.gw.entities.RealtimeData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HistoricalCurveDao {
    List<RealtimeData> getList(RealtimeData realtimeData);
}
