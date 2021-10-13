package com.gw.data.historicalCurve.service.impl;

import com.gw.common.TableStrategy;
import com.gw.data.historicalCurve.dao.HistoricalCurveDao;
import com.gw.data.historicalCurve.service.HistoricalCurveService;
import com.gw.entities.RtData;
import com.gw.entities.TableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class HistoricalCurveServiceImpl implements HistoricalCurveService {

    @Autowired
    private HistoricalCurveDao historicalCurveDao;

    private Map<String, Object> getOtcTableNameAndData(String startTime, String endTime, Long taskId, Long welderId, Long weldMachineId) {
        Map<String, Object> map = new HashMap<>();
        List<RtData> rtDataList = new ArrayList<>();
        List<TableInfo> tableInfoList = new ArrayList<>();
        //根据时间段获取OTC表名集合
        final List<String> otcTableList = TableStrategy.getOtcTableByDateTime(startTime, endTime);
        if (!ObjectUtils.isEmpty(otcTableList)) {
            //删除数据库不存在的表
            otcTableList.removeIf(s -> ObjectUtils.isEmpty(historicalCurveDao.tableExistYesOrNo(s)));
            if (!otcTableList.isEmpty()) {
                //添加存在的表到集合中并返回
                otcTableList.forEach(tableName -> {
                    TableInfo tableInfo = new TableInfo();
                    tableInfo.setTableName(tableName);
                    tableInfo.setTaskId(taskId);
                    tableInfo.setWelderId(welderId);
                    tableInfo.setWeldMachineId(weldMachineId);
                    tableInfo.setStartTime(startTime);
                    tableInfo.setEndTime(endTime);
                    tableInfoList.add(tableInfo);
                });
                //返回第一张表的数据
                rtDataList = historicalCurveDao.getList(startTime, endTime, taskId, welderId, weldMachineId, otcTableList.get(0));
            }
        }
        map.put("list", rtDataList);
        map.put("tableNames", tableInfoList);
        return map;
    }

    /**
     * @Date 2021/7/14 8:57
     * @Description 焊机历史曲线
     * @Params startTime 开始时间  endTime 结束时间  taskId 任务id  welderId 焊工id  weldMachineId 焊机id
     */
    @Override
    public Map<String, Object> getList(String startTime, String endTime, Long taskId, Long welderId, Long weldMachineId) throws ParseException {

        return getOtcTableNameAndData(startTime, endTime, taskId, welderId, weldMachineId);

//        Map<String, Object> map = new HashMap<>();
//
//        Date bigTime = null;
//
//        Date endTimes = null;
//        Calendar calEnd = Calendar.getInstance();
//        if (!ObjectUtils.isEmpty(startTime)) {
//
//            bigTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime + " 00:00:00");
//        }
//
//        if (!ObjectUtils.isEmpty(endTime)) {
//
//            endTimes = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime + " 00:00:00");
//            calEnd.setTime(endTimes);
//        }
//
//        List<Date> lDate = new ArrayList<>();
//
//        lDate.add(bigTime);
//
//        Calendar calBegin = Calendar.getInstance();
//
//        calBegin.setTime(bigTime);
//
//        if (!ObjectUtils.isEmpty(endTimes)) {
//
//            while (endTimes.after(calBegin.getTime())) {
//
//                calBegin.add(Calendar.DAY_OF_MONTH, 1);
//
//                lDate.add(calBegin.getTime());
//            }
//        }
//
//        List<TableInfo> tableNames = new ArrayList<>();
//        List<TableInfo> tableNamesList = new ArrayList<>();
//
//        //获取表名列表 并 判断表是否存在
//        for (Date date : lDate) {
//
//            TableInfo tableInfo = new TableInfo();
//
//            String tableName = "otcrtd" + new SimpleDateFormat("yyyyMMdd").format(date);
//
//            Integer rows = historicalCurveDao.tableExistYesOrNo(tableName);
//
//            if (ObjectUtils.isEmpty(rows)) {
//                continue;
//            }
//
//            tableInfo.setTableName(tableName);
//
//            tableInfo.setTaskId(taskId);
//
//            tableInfo.setWelderId(welderId);
//
//            tableInfo.setWeldMachineId(weldMachineId);
//
//            tableNames.add(tableInfo);
//
//        }
//
//        for (int i = 0; i < tableNames.size(); i++) {
//
//            List<RtData> list = historicalCurveDao.getList(startTime, endTime, taskId, welderId, weldMachineId, tableNames.get(i).getTableName());
//
//            if (list.size() != 0) {
//                tableNamesList.add(tableNames.get(i));
//                map.put("list", list);
//                continue;
//            }
//        }
//
//        map.put("tableNames", tableNamesList);
//
//        return map;

    }

    /**
     * @Date 2021/7/14 8:57
     * @Description 焊机历史曲线信息列表
     * @Params startTime 开始时间  endTime 结束时间  taskId 任务id  welderId 焊工id  weldMachineId 焊机id
     */
    @Override
    public List<RtData> getHistoryCurveInfos(String startTime, String endTime, Long taskId, Long welderId, Long weldMachineId) {

        return historicalCurveDao.selectHistoryCurveInfos(startTime, endTime, taskId, welderId, weldMachineId);
    }

    /**
     * @Date 2021/7/15 17:46
     * @Description 根据表名 查询 焊机历史曲线
     * @Params
     */
    @Override
    public Map<String, Object> getHistoryCurveInfoByTableName(TableInfo tableInfo) {

        List<RtData> list = historicalCurveDao.getHistoryCurveByTableName(tableInfo);

        Map<String, Object> map = new HashMap<>();

        map.put("list", list);

        return map;
    }
}
