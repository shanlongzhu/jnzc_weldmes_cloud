package com.gw.data.weldTask.dao;

import com.gw.entities.WeldStatisticsDataWeldStatics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WeldTaskDao {

    /**
     * @Date 2021/10/19 13:52
     * @Description 查询焊机任务列表信息
     * @Params
     */
    List<WeldStatisticsDataWeldStatics> getList(@Param("areaId") Long areaId, @Param("teamName") String teamName, @Param("time1") String time1, @Param("time2") String time2, @Param("valueName") String valueName);

    /**
     * @Date 2021/10/19 13:52
     * @Description 根据部门id 查询部门名称
     * @Params
     */
    String getTeamName(Long teamId);

    /**
     * @Date 2021/10/19 13:53
     * @Description 根据类型名称查询描述
     * @Params
     */
    String getValueName(Long status);




}
