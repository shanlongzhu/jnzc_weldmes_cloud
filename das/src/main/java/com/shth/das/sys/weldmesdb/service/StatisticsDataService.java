package com.shth.das.sys.weldmesdb.service;

public interface StatisticsDataService {

    /**
     * 根据时间端统计OTC实时数据并插入到报表中
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param tableName 实时数据表名
     */
    void insertWeldStatisticsData(String startTime, String endTime, String tableName);

    /**
     * 根据时间端统计SX实时数据并插入到报表中
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param tableName 实时数据表名
     */
    void insertSxWeldStatisticsData(String startTime, String endTime, String tableName);

    /**
     * 查询OTC统计表最大结束时间
     * @return 返回这个最大时间
     */
    String selectOtcMaxEndTime();

    /**
     * 查询松下统计表最大结束时间
     * @return 返回这个最大时间
     */
    String selectSxMaxEndTime();

}
