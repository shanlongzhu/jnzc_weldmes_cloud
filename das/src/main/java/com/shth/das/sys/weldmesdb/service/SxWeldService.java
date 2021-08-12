package com.shth.das.sys.weldmesdb.service;

import com.shth.das.pojo.db.SxWeldModel;

public interface SxWeldService {

    /**
     * 松下焊机表添加数据
     * @param sxWeldModel
     * @return
     */
    int insertSxWeld(SxWeldModel sxWeldModel);

    /**
     * 根据设备CID查询设备信息表
     * @param weldCid 设备CID
     * @return 松下焊机实体类
     */
    SxWeldModel getSxWeldByWeldCid(String weldCid);
}
