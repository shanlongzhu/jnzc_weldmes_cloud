package com.shth.das.sys.rtdata.service;


import com.shth.das.pojo.jnsx.SxRtDataDb;

import java.util.List;

public interface SxRtDataService {

    /**
     * 创建松下实时数据表结构
     *
     * @param tableName 表名
     * @return 创建结果
     */
    int createNewTable(String tableName);

    /**
     * 松下实时数据批量存入数据库
     *
     * @param list 集合
     */
    void insertSxRtDataList(List<SxRtDataDb> list);

}
