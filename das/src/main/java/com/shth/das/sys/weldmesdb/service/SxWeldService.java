package com.shth.das.sys.weldmesdb.service;

import com.shth.das.pojo.jnsx.SxWeldModel;

public interface SxWeldService {

    /**
     * 松下焊机表添加数据
     * @param sxWeldModel
     * @return
     */
    int insertSxWeld(SxWeldModel sxWeldModel);

    /**
     * 根据设备IP查询设备信息表
     * @param weldIp 设备IP
     * @return 松下焊机实体类
     */
    SxWeldModel getSxWeldByWeldIp(String weldIp);
}
