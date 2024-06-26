package com.shth.das.sys.rtdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shth.das.pojo.db.OtcWeldStatisticsData;
import com.shth.das.pojo.jnotc.JNRtDataDB;

import java.util.List;

public interface OtcRtDataService extends IService<JNRtDataDB> {

    /**
     * 查询表名是否存在
     *
     * @param tableName 表名
     * @return
     */
    Integer selectTableName(String tableName);

    /**
     * 创建OTC实时数据表结构
     *
     * @param tableName 表名
     * @return 创建结果
     */
    Integer createNewTable(String tableName);

    /**
     * 实时数据存入数据库
     *
     * @param list 数据集合
     */
    void insertRtDataList(List<JNRtDataDB> list);

    /**
     * 按时间段统计OTC数据
     *
     * @param startTime
     * @param endTime
     * @param tableName
     * @param offset
     * @param size
     * @return
     */
    List<OtcWeldStatisticsData> selectOtcRtDataByDateTime(String startTime, String endTime, String tableName, Integer offset, Integer size);

}
