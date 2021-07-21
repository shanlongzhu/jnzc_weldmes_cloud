package com.shth.das.sys.rtdata.service;

import com.shth.das.pojo.jnotc.JNRtDataDB;

import java.util.List;

public interface RtDataService {

    /**
     * 创建OTC实时数据表结构
     */
    int createNewTable(String tableName);

    /**
     * 实时数据存入数据库
     * @param list 数据集合
     */
    void insertRtDataList(List<JNRtDataDB> list);

}
