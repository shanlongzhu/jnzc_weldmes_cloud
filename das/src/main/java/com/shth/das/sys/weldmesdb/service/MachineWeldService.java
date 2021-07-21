package com.shth.das.sys.weldmesdb.service;

import com.shth.das.pojo.db.WeldModel;

import java.util.List;

public interface MachineWeldService {

    /**
     * 查询所有焊机信息不分页
     * @return
     */
    List<WeldModel> getMachineWeldAll();
}
