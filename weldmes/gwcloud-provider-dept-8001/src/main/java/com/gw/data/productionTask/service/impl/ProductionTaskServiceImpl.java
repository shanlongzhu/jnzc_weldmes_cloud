package com.gw.data.productionTask.service.impl;

import com.gw.data.productionTask.dao.ProductionTaskDao;
import com.gw.data.productionTask.service.ProductionTaskService;
import com.gw.entities.RealtimeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductionTaskServiceImpl implements ProductionTaskService {

//    @Autowired
//    private ProductionTaskDao productionTaskDao;

    @Override
    public List<RealtimeData> getList(Long areaId, Long teamId, String welderNo, String junctionNo, String time1, String time2) {
//        String teamName=productionTaskDao.getTeamName(teamId);
//        return productionTaskDao.getList(areaId,teamName,welderNo,junctionNo,time1,time2);
        return null;
    }
}
