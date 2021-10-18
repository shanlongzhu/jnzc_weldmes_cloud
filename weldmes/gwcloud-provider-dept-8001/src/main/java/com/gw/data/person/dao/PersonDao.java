package com.gw.data.person.dao;

import com.gw.entities.WeldStatisticsDataPerson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @Date 2021/10/18 16:24
 * @Description 人员生产数据dao层
 * @Params
 */
@Mapper
public interface PersonDao {

    /**
     * @Date 2021/10/18 16:25
     * @Description 查询人员生产数据列表
     * @Params
     */
    List<WeldStatisticsDataPerson> getList(@Param("time1") String time1, @Param("time2") String time2, @Param("welderNo") String welderNo, @Param("welderName") String welderName, @Param("name") String name);

    /**
     * @Date 2021/10/18 16:25
     * @Description 根据部门id 查询部门名称
     * @Params
     */
    String getDeptName(Long deptId);
}
