package com.gw.data.person.service;

import com.gw.entities.WeldStatisticsDataPerson;

import java.util.List;

/**
 * @Date 2021/10/18 16:22
 * @Description 人员生产业务层
 * @Params
 */
public interface PersonService {

    /**
     * @Date 2021/10/18 16:22
     * @Description 获取人员生产数据列表
     * @Params
     */
    List<WeldStatisticsDataPerson> getList(String time1, String time2, String welderNo, String welderName, String name);

    /**
     * @Date 2021/10/18 16:22
     * @Description 获取部门名称
     * @Params
     */
    String getDeptName(Long deptId);
}
