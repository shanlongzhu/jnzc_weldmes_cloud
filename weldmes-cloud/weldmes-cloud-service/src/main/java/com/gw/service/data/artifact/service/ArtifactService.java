package com.gw.service.data.artifact.service;

import com.gw.entities.WeldStatisticsDataArtifact;

import java.util.List;

public interface ArtifactService {

    /**
     * @Date 2021/10/18 14:05
     * @Description 工件生产报表数据列表
     * @Params
     */
    List<WeldStatisticsDataArtifact> getList(String time1, String time2, String taskNo, List<Long> ids);

    /**
     * @Date 2021/10/18 14:06
     * @Description 根据部门id 查询 部门名称
     * @Params
     */
    String getName(Long deptId);
}
