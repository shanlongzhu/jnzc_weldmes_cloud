package com.gw.data.productionTask.service.impl;

import com.gw.data.productionTask.dao.ProductionTaskDao;
import com.gw.data.productionTask.service.ProductionTaskService;
import com.gw.entities.RealtimeData;
import com.gw.entities.WeldStatisticsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductionTaskServiceImpl implements ProductionTaskService {

    @Autowired
    private ProductionTaskDao productionTaskDao;

    @Override
    public List<WeldStatisticsData> getList(String time1, String time2,String welderNo,String welderName,String machineNo,String taskNo,String name) {
        return productionTaskDao.getList(time1,time2,welderNo,welderName,machineNo,taskNo,name);
    }

    @Override
    public String getName(Long deptId) {
        return productionTaskDao.getName(deptId);
    }
}
