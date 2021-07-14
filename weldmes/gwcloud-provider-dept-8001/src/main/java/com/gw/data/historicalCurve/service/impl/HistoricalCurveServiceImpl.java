package com.gw.data.historicalCurve.service.impl;

import com.gw.data.historicalCurve.dao.HistoricalCurveDao;
import com.gw.data.historicalCurve.service.HistoricalCurveService;
import com.gw.entities.RealtimeData;
import com.gw.entities.RtData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class HistoricalCurveServiceImpl implements HistoricalCurveService {

    @Autowired
    private HistoricalCurveDao historicalCurveDao;

    @Override
    public List<RtData> getList(String startTime, String endTime) throws ParseException {

        Date bigTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime + " 00:00:00");

        Date endTimes = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime + " 00:00:00");

        List<Date> lDate = new ArrayList<>();

        lDate.add(bigTime);

        Calendar calBegin = Calendar.getInstance();

        calBegin.setTime(bigTime);

        Calendar calEnd = Calendar.getInstance();

        calEnd.setTime(endTimes);

        while (endTimes.after(calBegin.getTime())) {

            calBegin.add(Calendar.DAY_OF_MONTH, 1);

            lDate.add(calBegin.getTime());
        }

        List<String> tableNames = new ArrayList<>();

        //获取表名列表
        for (Date date : lDate) {

            tableNames.add("rtdata" + new SimpleDateFormat("yyyyMMdd").format(date));
        }

        List<RtData> list = new ArrayList<>();

        for (String tableName : tableNames) {

            List<RtData> tableInfos = historicalCurveDao.getList(startTime, endTime, tableName);

            list.addAll(tableInfos);
        }

        return list;

    }
}
