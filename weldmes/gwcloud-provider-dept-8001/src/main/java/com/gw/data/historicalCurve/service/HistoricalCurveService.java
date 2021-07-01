package com.gw.data.historicalCurve.service;

import com.gw.entities.RealtimeData;

import java.util.List;

public interface HistoricalCurveService {
    List<RealtimeData> getList(RealtimeData realtimeData);
}
