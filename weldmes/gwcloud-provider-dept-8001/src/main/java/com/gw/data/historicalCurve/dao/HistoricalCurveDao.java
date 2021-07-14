package com.gw.data.historicalCurve.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.gw.entities.RtData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("slave_1")
public interface HistoricalCurveDao {

    /**
     * @Date 2021/7/14 8:57
     * @Description 焊机历史曲线
     * @Params startTime 开始时间  endTime 结束时间  taskId 任务id  welderId 焊工id  weldMachineId 焊机id
     */
    List<RtData> getList(@Param("startTime") String startTime, @Param("endTime") String endTime,
                         @Param("taskId")Long taskId,@Param("welderId")Long welderId,
                         @Param("weldMachineId")Long weldMachineId,@Param("tableName") String tableName);

}
