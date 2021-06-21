package com.shth.das.sys.weldmesdb.service;

public interface StatisticsDataService {

    /**
     * 根据时间端统计实时数据并插入到报表中
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param tableName 实时数据表名
     * @return
     */
    int insertWeldStatisticsData(String startTime, String endTime, String tableName);

    String selectMaxEndTime();
}
