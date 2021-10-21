package com.gw.data.productionTask.dao;

import com.gw.entities.WeldStatisticsDataProductionTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Date 2021/10/18 17:27
 * @Description 生产任务详情dao层
 * @Params
 */
@Mapper
public interface ProductionTaskDao {

    /**
     * @Date 2021/10/18 17:28
     * @Description 查询生产任务详情数据列表
     * @Params
     */
    List<WeldStatisticsDataProductionTask> getList(@Param("time1") String time1, @Param("time2") String time2,
                                                   @Param("welderNo") String welderNo,
                                                   @Param("welderName") String welderName,
                                                   @Param("machineNo") String machineNo,
                                                   @Param("taskNo") String taskNo, @Param("ids") List<Long> ids);

}
