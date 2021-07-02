package com.gw.data.historicalCurve.service.impl;

import com.gw.data.historicalCurve.dao.HistoricalCurveDao;
import com.gw.data.historicalCurve.service.HistoricalCurveService;
import com.gw.entities.RealtimeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HistoricalCurveServiceImpl implements HistoricalCurveService {

    @Autowired
    private HistoricalCurveDao historicalCurveDao;

    @Override
    public List<RealtimeData> getList(RealtimeData realtimeData) {
        return historicalCurveDao.getList(realtimeData);
    }
}
