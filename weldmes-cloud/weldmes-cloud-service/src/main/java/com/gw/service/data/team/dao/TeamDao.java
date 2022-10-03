package com.gw.service.data.team.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.gw.entities.WeldStatisticsDataTeam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeamDao {

    /**
     * @Date 2021/10/19 14:24
     * @Description 查询班组报表信息列表
     * @Params
     */
    List<WeldStatisticsDataTeam> getList(@Param("time1") String time1, @Param("time2") String time2, @Param("ids")List<Long> ids);

    /**
     * @Date 2021/10/28 13:52
     * @Description 查询OTC、松下实时班组报表信息列表
     * @Params
     */
    @DS("slave_1")
    List<WeldStatisticsDataTeam> getOTCAndSXRealTimeInfos(@Param("otcTableName") String otcTableName,
                                                          @Param("sxTableName") String sxTableName,
                                                          @Param("time") String time,@Param("time2") String time2,
                                                          @Param("ids")List<Long> ids);

}
