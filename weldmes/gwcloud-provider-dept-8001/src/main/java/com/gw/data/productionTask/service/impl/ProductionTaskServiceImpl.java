package com.gw.data.productionTask.service.impl;

import com.gw.data.productionTask.dao.ProductionTaskDao;
import com.gw.data.productionTask.service.ProductionTaskService;
import com.gw.entities.WeldStatisticsDataProductionTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Date 2021/10/18 17:26
 * @Description 生产任务详情业务实现层
 * @Params
 */
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
    public List<WeldStatisticsDataProductionTask> getList(String time1, String time2, String welderNo, String welderName, String machineNo, String taskNo, String name) {
        return productionTaskDao.getList(time1,time2,welderNo,welderName,machineNo,taskNo,name);
    }

    /**
     * @Date 2021/10/18 17:27
     * @Description 获取部门名称
     * @Params
     */
    @Override
    public String getName(Long deptId) {
        return productionTaskDao.getName(deptId);
    }
}
