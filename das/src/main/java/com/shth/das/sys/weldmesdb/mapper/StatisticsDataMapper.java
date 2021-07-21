package com.shth.das.sys.weldmesdb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StatisticsDataMapper {

    /**
     * 统计松下实时数据到报表中
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param tableName 表名
     * @return 统计结果
     */
    int insertWeldStatisticsData(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("tableName") String tableName);

    /**
     * 统计松下实时数据到报表中
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param tableName 表名
     * @return 统计结果
     */
    int insertSxWeldStatisticsData(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("tableName") String tableName);

    /**
     * 查询OTC统计表上一次的统计时间（最大结束时间）
     * @return 返回最大的结束时间
     */
    String selectOtcMaxEndTime();

    /**
     * 查询松下统计表上一次的统计时间（最大结束时间）
     * @return 返回最大的结束时间
     */
    String selectSxMaxEndTime();

}
