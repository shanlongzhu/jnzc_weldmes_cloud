package com.gw.data.artifact.service;

import com.gw.entities.RealtimeData;

import java.util.List;

public interface ArtifactService {
    List<RealtimeData> getList(String time1, String time2, String taskNo);
}
