package com.shth.das.sys.weldmesdb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StatisticsDataMapper {

    /**
     * 实时数据统计到报表中
     * @param tableName 表名
     * @return
     */
    int insertWeldStatisticsData(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("tableName") String tableName);

    /**
     * 查询上一次的统计时间（最大结束时间）
     * @return 返回最大的结束时间
     */
    String selectMaxEndTime();

}
