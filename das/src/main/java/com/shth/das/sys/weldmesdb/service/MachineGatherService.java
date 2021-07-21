package com.shth.das.sys.weldmesdb.service;

import com.shth.das.pojo.db.GatherModel;

import java.util.List;

public interface MachineGatherService {

    /**
     * 查询所有采集模块数据不分页
     * @return
     */
    List<GatherModel> getMachineGatherAll();

    /**
     * 根据采集编号修改IP
     * @param gatherNo 采集编号
     * @param weldIp 采集盒IP
     */
    void updateGatherIpByNumber(String gatherNo,String weldIp);
}
