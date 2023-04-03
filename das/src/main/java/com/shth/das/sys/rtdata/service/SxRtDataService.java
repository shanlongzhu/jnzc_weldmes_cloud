package com.shth.das.sys.rtdata.service;


import com.shth.das.pojo.jnsx.SxRtDataDb;

import java.util.List;

public interface SxRtDataService {

    /**
     * 查询表名是否存在
     *
     * @param tableName 表名
     * @return
     */
    Integer selectTableName(String tableName);

    /**
     * 创建松下实时数据表结构
     *
     * @param tableName 表名
     * @return 创建结果
     */
    Integer createNewTable(String tableName);

    /**
     * 松下实时数据批量存入数据库
     *
     * @param list 集合
     */
    void insertSxRtDataList(List<SxRtDataDb> list);

}
