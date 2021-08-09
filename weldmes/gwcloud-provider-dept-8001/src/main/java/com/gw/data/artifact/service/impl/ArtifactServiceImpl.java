package com.gw.data.artifact.service.impl;

import com.gw.data.artifact.dao.ArtifactDao;
import com.gw.data.artifact.service.ArtifactService;
import com.gw.entities.RealtimeData;
import com.gw.entities.WeldStatisticsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtifactServiceImpl implements ArtifactService {

    @Autowired
    private ArtifactDao artifactDao;

    @Override
    public List<WeldStatisticsData> getList(String time1, String time2) {
        return artifactDao.getList(time1,time2);
    }
}
