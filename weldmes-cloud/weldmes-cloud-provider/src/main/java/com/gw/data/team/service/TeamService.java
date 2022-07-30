package com.gw.data.team.service;
import com.gw.entities.WeldStatisticsDataTeam;

import java.util.List;
import java.util.Set;

public interface TeamService {

    /**
     * @Date 2021/10/19 14:25
     * @Description 获取班组生产数据列表
     * @Params
     */
    List<WeldStatisticsDataTeam> getList(String time1, String time2, List<Long> ids);

    /**
     * @Date 2021/10/20 15:45
     * @Description 通过组织机构id 查询 该部门下所有的班组id
     * @Params
     */
    public List<Long> getNextDeptIds(String deptId);
}
