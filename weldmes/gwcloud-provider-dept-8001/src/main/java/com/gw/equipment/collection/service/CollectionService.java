package com.gw.equipment.collection.service;

import com.gw.entities.MachineGatherInfo;
import com.gw.entities.MachineWeldInfo;

import java.util.List;

public interface CollectionService {
    List<MachineGatherInfo> getList(Integer grade,Integer gatherNo);

    int addCollection(MachineGatherInfo machineGatherInfo);

    int deleteCollection(long id);

    List<MachineGatherInfo> getById(Long id);

    int updateCollection(MachineGatherInfo machineGatherInfo);

    void importExcel(List<MachineGatherInfo> machineGatherInfoList);

    Long getDeptId(String name);


    Integer getStatus(String valueName);

    Integer getProtocol(String valueNames);

    /**
     * @Date 2021/7/1 9:04
     * @Description  获取采集序号列表
     * @Params
     */
    List<MachineGatherInfo> queryGatherNos();

}
