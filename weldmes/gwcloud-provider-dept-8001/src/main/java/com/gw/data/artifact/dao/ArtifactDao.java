package com.gw.data.artifact.dao;

import com.gw.entities.WeldStatisticsDataArtifact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @Date 2021/10/18 14:06
 * @Description 工件生产dao层
 * @Params
 */
@Mapper
public interface ArtifactDao {

    /**
     * @Date 2021/10/18 14:07
     * @Description 查询 工件生产报表列表信息
     * @Params
     */
    List<WeldStatisticsDataArtifact> getList(@Param("time1") String time1, @Param("time2") String time2, @Param("taskNo") String taskNo, @Param("name") String name);

    /**
     * @Date 2021/10/18 14:07
     * @Description 根据 部门id 查询 部门名称
     * @Params
     */
    String getName(Long deptId);
}
