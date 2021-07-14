package com.gw.data.historicalCurve.service.impl;

import com.gw.data.historicalCurve.dao.HistoricalCurveDao;
import com.gw.data.historicalCurve.service.HistoricalCurveService;
import com.gw.entities.RealtimeData;
import com.gw.entities.RtData;
import com.gw.entities.TaskClaim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class HistoricalCurveServiceImpl implements HistoricalCurveService {

    @Autowired
    private HistoricalCurveDao historicalCurveDao;

    /**
     * @Date 2021/7/14 8:57
     * @Description 焊机历史曲线
     * @Params startTime 开始时间  endTime 结束时间  taskId 任务id  welderId 焊工id  weldMachineId 焊机id
     */
    @Override
    public List<RtData> getList(String startTime, String endTime,Long taskId,Long welderId,Long weldMachineId) throws ParseException {

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

            Integer rows = historicalCurveDao.tableExistYesOrNo(tableName);

            if(ObjectUtils.isEmpty(rows)){
                continue;
            }
            List<RtData> tableInfos = historicalCurveDao.getList(startTime,endTime,taskId,welderId,weldMachineId,tableName);

            list.addAll(tableInfos);
        }

        return list;

    }

    /**
     * @Date 2021/7/14 8:57
     * @Description 焊机历史曲线信息列表
     * @Params startTime 开始时间  endTime 结束时间  taskId 任务id  welderId 焊工id  weldMachineId 焊机id
     */
    @Override
    public List<RtData> getHistoryCurveInfos(String startTime, String endTime, Long taskId, Long welderId, Long weldMachineId) {

        List<RtData> list = historicalCurveDao.selectHistoryCurveInfos(startTime,endTime,taskId,welderId,weldMachineId);

        return list;
    }
}
