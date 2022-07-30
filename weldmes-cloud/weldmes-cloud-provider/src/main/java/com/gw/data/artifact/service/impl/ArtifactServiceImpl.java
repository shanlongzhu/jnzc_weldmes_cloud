package com.gw.data.artifact.service.impl;

import com.gw.data.artifact.dao.ArtifactDao;
import com.gw.data.artifact.service.ArtifactService;
import com.gw.entities.WeldStatisticsDataArtifact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtifactServiceImpl implements ArtifactService {

    @Autowired
    private ArtifactDao artifactDao;

    /**
     * @Date 2021/10/18 14:09
     * @Description 获取工件生产报表信息列表
     * @Params
     */
    @Override
    public List<WeldStatisticsDataArtifact> getList(String time1, String time2, String taskNo, List<Long> ids) {

        List<WeldStatisticsDataArtifact> list = artifactDao.getList(time1,time2,taskNo,ids);

        return list;
    }

    /**
     * @Date 2021/10/18 14:09
     * @Description 根据部门id 获取部门名称
     * @Params
     */
    @Override
    public String getName(Long deptId) {
        return artifactDao.getName(deptId);
    }
}
