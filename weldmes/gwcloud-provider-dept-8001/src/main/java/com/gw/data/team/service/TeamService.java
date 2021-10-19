package com.gw.data.team.service;
import com.gw.entities.WeldStatisticsDataTeam;

import java.util.List;
/**
 * @Date 2021/10/19 14:25
 * @Description 班组报表业务层
 * @Params
 */
public interface TeamService {

    /**
     * @Date 2021/10/19 14:25
     * @Description 获取班组生产数据列表
     * @Params
     */
    List<WeldStatisticsDataTeam> getList(String time1, String time2, String deptId);
}
