package com.gw.service.data.historicalCurve.service.impl;

import com.gw.common.TableStrategy;
import com.gw.service.data.historicalCurve.dao.HistoricalCurveDao;
import com.gw.service.data.historicalCurve.service.HistoricalCurveService;
import com.gw.entities.RtData;
import com.gw.entities.TableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class HistoricalCurveServiceImpl implements HistoricalCurveService {

    @Autowired
    private HistoricalCurveDao historicalCurveDao;

    private Map<String, Object> getOtcTableNameAndData(TableInfo tableInfo) {
        Map<String, Object> map = new HashMap<>();
        List<RtData> rtDataList = new ArrayList<>();
        List<TableInfo> tableInfoList = new ArrayList<>();
        if (null != tableInfo) {
            Map<Integer, List<String>> listMap = new HashMap<>();
            //如果设备类型为空，则查询OTC和松下两个类型
            if (tableInfo.getWeldType() == null) {
                final List<String> otcTable = TableStrategy.getOtcTableByDateTime(tableInfo.getStartTime(), tableInfo.getEndTime());
                if (otcTable != null && otcTable.size() > 0) {
                    //删除数据库不存在的表
                    otcTable.removeIf(table -> ObjectUtils.isEmpty(historicalCurveDao.tableExistYesOrNo(table)));
                    if (otcTable.size() > 0) {
                        listMap.put(0, otcTable);
                    }
                }
                final List<String> sxTable = TableStrategy.getSxTableByDateTime(tableInfo.getStartTime(), tableInfo.getEndTime());
                if (sxTable != null && sxTable.size() > 0) {
                    //删除数据库不存在的表
                    sxTable.removeIf(table -> ObjectUtils.isEmpty(historicalCurveDao.tableExistYesOrNo(table)));
                    if (sxTable.size() > 0) {
                        listMap.put(1, sxTable);
                    }
                }
            } else {
                //OTC设备
                if (tableInfo.getWeldType() == 0) {
                    //根据时间段获取OTC表名集合
                    final List<String> otcTable = TableStrategy.getOtcTableByDateTime(tableInfo.getStartTime(), tableInfo.getEndTime());
                    if (otcTable != null && otcTable.size() > 0) {
                        //删除数据库不存在的表
                        otcTable.removeIf(table -> ObjectUtils.isEmpty(historicalCurveDao.tableExistYesOrNo(table)));
                        if (otcTable.size() > 0) {
                            listMap.put(0, otcTable);
                        }
                    }
                }
                //松下设备
                else {
                    //根据时间段获取松下表名集合
                    final List<String> sxTable = TableStrategy.getSxTableByDateTime(tableInfo.getStartTime(), tableInfo.getEndTime());
                    if (sxTable != null && sxTable.size() > 0) {
                        //删除数据库不存在的表
                        sxTable.removeIf(table -> ObjectUtils.isEmpty(historicalCurveDao.tableExistYesOrNo(table)));
                        if (sxTable.size() > 0) {
                            listMap.put(1, sxTable);
                        }
                    }
                }
            }
            if (listMap.size() > 0) {
                //表名集合
                tableInfoList = getTableInfoList(listMap, tableInfo);
                final String tableName = tableInfoList.get(0).getTableName();
                tableInfo.setTableName(tableName);
                //返回第一张表的数据
                rtDataList = historicalCurveDao.getRealTimeDataList(tableInfo);
            }
        }
        map.put("list", rtDataList);
        map.put("tableNames", tableInfoList);
        return map;
    }

    private List<TableInfo> getTableInfoList(Map<Integer, List<String>> listMap, TableInfo tableInfo) {
        List<TableInfo> tableInfoList = new ArrayList<>();
        if (listMap.size() > 0) {
            if (listMap.containsKey(0)) {
                final List<String> otcTables = listMap.get(0);
                //添加存在的表到集合中并返回
                otcTables.forEach(tableName -> {
                    TableInfo table = new TableInfo();
                    table.setTableName(tableName);
                    table.setTaskId(tableInfo.getTaskId());
                    table.setWelderId(tableInfo.getWelderId());
                    table.setWeldMachineId(tableInfo.getWeldMachineId());
                    table.setStartTime(tableInfo.getStartTime());
                    table.setEndTime(tableInfo.getEndTime());
                    table.setWeldType(0);
                    tableInfoList.add(table);
                });
            }
            if (listMap.containsKey(1)) {
                final List<String> sxTables = listMap.get(1);
                //添加存在的表到集合中并返回
                sxTables.forEach(tableName -> {
                    TableInfo table = new TableInfo();
                    table.setTableName(tableName);
                    table.setTaskId(tableInfo.getTaskId());
                    table.setWelderId(tableInfo.getWelderId());
                    table.setWeldMachineId(tableInfo.getWeldMachineId());
                    table.setStartTime(tableInfo.getStartTime());
                    table.setEndTime(tableInfo.getEndTime());
                    table.setWeldType(1);
                    tableInfoList.add(table);
                });
            }
        }
        return tableInfoList;
    }

    /**
     * @Date 2021/7/14 8:57
     * @Description 焊机历史曲线
     * @Params startTime 开始时间  endTime 结束时间  taskId 任务id  welderId 焊工id  weldMachineId 焊机id
     */
    @Override
    public Map<String, Object> getList(TableInfo tableInfo) {

        return getOtcTableNameAndData(tableInfo);

    }

    /**
     * @Date 2021/7/14 8:57
     * @Description 焊机历史曲线信息列表
     * @Params startTime 开始时间  endTime 结束时间  taskId 任务id  welderId 焊工id  weldMachineId 焊机id
     */
    @Override
    public List<RtData> getHistoryCurveInfos(TableInfo tableInfo) {

        return historicalCurveDao.selectHistoryCurveInfos(tableInfo);
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
