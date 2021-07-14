package com.gw.data.historicalCurve.service;

import com.gw.entities.RealtimeData;
import com.gw.entities.RtData;

import java.text.ParseException;
import java.util.List;

public interface HistoricalCurveService {

    List<RtData> getList(String startTime, String endTime) throws ParseException;

}
