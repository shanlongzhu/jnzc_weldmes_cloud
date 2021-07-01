package com.gw.data.person.service;

import com.gw.entities.RealtimeData;

import java.util.List;

public interface PersonService {
    List<RealtimeData> getList(String time1, String time2);
}
