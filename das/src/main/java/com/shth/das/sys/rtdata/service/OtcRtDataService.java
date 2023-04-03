package com.shth.das.sys.rtdata.service;

import com.shth.das.pojo.jnotc.JNRtDataDB;

import java.util.List;

public interface OtcRtDataService {

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

}
