package com.shth.das.sys.weldmesdb.service;

import com.shth.das.pojo.GatherModel;

import java.util.List;

public interface MachineGatherService {

    /**
     * 查询所有采集模块数据不分页
     * @return
     */
    List<GatherModel> getMachineGatherAll();
}
