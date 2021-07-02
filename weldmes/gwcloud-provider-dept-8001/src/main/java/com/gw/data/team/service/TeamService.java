package com.gw.data.team.service;

import com.gw.entities.RealtimeData;

import java.util.List;

public interface TeamService {

    List<RealtimeData> getList(String time1,String time2);
}
