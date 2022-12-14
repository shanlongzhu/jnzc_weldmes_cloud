package com.gw.service.data.historicalCurve.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.gw.entities.RtData;
import com.gw.entities.TableInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HistoricalCurveDao {

    /**
     * @Date 2021/7/14 8:57
     * @Description 焊机历史曲线
     * @Params startTime 开始时间  endTime 结束时间  taskId 任务id  welderId 焊工id  weldMachineId 焊机id
     */
    @DS("slave_1")
    List<RtData> getRealTimeDataList(TableInfo tableInfo);

    /**
     * @Date 2021/7/14 8:57
     * @Description 焊机历史曲线信息列表
     * @Params startTime 开始时间  endTime 结束时间  taskId 任务id  welderId 焊工id  weldMachineId 焊机id
     */
    List<RtData> selectHistoryCurveInfos(TableInfo tableInfo);

    /**
     * @Date 2021/7/14 17:55
     * @Description 判断表是否存在
     * @Params
     */
    public Integer tableExistYesOrNo(@Param("tableName") String tableName);

    /**
     * @Date 2021/7/15 17:46
     * @Description 根据表名、taskId 任务id  welderId 焊工id  weldMachineId 焊机id 查询 焊机历史曲线
     * @Params
     */
    @DS("slave_1")
    public List<RtData> getHistoryCurveByTableName(TableInfo tableInfo);


}
