package com.shth.das.sys.weldmesdb.mapper;

import com.shth.das.pojo.db.OtcWeldStatisticsData;
import com.shth.das.pojo.db.SxWeldStatisticsData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StatisticsDataMapper {

    /**
     * 统计OTC实时数据到报表中
     * @return 统计结果
     */
    int insertWeldStatisticsData(List<OtcWeldStatisticsData> list);

    /**
     * 统计SX实时数据到报表中
     * @return 统计结果
     */
    int insertSxWeldStatisticsData(List<SxWeldStatisticsData> list);

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
