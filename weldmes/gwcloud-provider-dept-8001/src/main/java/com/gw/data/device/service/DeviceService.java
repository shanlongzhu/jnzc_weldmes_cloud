package com.gw.data.device.service;

import com.gw.entities.RealtimeData;
import java.util.List;

public interface DeviceService {


    List<RealtimeData> getList(Long areaId ,Long teamId,String time1, String time2);
}
