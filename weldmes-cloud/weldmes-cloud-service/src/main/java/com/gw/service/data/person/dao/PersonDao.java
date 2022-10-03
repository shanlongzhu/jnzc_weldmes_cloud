package com.gw.service.data.person.dao;

import com.gw.entities.WeldStatisticsDataPerson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonDao {

    /**
     * @Date 2021/10/18 16:25
     * @Description 查询人员生产数据列表
     * @Params
     */
    List<WeldStatisticsDataPerson> getList(@Param("time1") String time1, @Param("time2") String time2,
                                           @Param("welderNo") String welderNo, @Param("welderName") String welderName,
                                           @Param("ids") List<Long> ids);
}
