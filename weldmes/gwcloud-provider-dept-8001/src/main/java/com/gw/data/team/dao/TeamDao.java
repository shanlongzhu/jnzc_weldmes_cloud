package com.gw.data.team.dao;

import com.gw.entities.WeldStatisticsDataTeam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Date 2021/10/19 14:24
 * @Description 班组报表dao层
 * @Params
 */
@Mapper
public interface TeamDao {

    /**
     * @Date 2021/10/19 14:24
     * @Description 查询班组报表信息列表
     * @Params
     */
    List<WeldStatisticsDataTeam> getList(@Param("time1") String time1, @Param("time2") String time2, @Param("ids")List<Long> ids);
}
