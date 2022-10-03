package com.gw.service.data.productionTask.service.impl;

import com.gw.service.data.productionTask.dao.ProductionTaskDao;
import com.gw.service.data.productionTask.service.ProductionTaskService;
import com.gw.entities.WeldStatisticsDataProductionTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductionTaskServiceImpl implements ProductionTaskService {

    @Autowired
    private ProductionTaskDao productionTaskDao;

    /**
     * @Date 2021/10/18 17:26
     * @Description 获取生产任务详情数据列表
     * @Params
     */
    @Override
    public List<WeldStatisticsDataProductionTask> getList(String time1, String time2, String welderNo, String welderName,
                                                          String machineNo, String taskNo, List<Long> ids) {

        List<WeldStatisticsDataProductionTask> list = productionTaskDao.getList(time1,time2,welderNo,welderName,machineNo,taskNo,ids);

        return list;
    }
}
