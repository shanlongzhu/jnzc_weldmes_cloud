package com.gw.data.artifact.service;

import com.gw.entities.RealtimeData;
import com.gw.entities.WeldStatisticsData;

import java.util.List;

public interface ArtifactService {
    List<WeldStatisticsData> getList(String time1, String time2, String taskNo);
}
