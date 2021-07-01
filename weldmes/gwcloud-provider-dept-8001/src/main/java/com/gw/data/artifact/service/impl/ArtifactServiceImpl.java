package com.gw.data.artifact.service.impl;

import com.gw.data.artifact.dao.ArtifactDao;
import com.gw.data.artifact.service.ArtifactService;
import com.gw.entities.RealtimeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtifactServiceImpl implements ArtifactService {

    @Autowired
    private ArtifactDao artifactDao;

    @Override
    public List<RealtimeData> getList(String time1, String time2, String taskNo) {
        return artifactDao.getList(time1,time2,taskNo);
    }
}
