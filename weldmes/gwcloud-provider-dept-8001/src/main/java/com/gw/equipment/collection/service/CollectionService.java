package com.gw.equipment.collection.service;

import com.gw.entities.MachineGatherInfo;

import java.util.List;

public interface CollectionService {
    List<MachineGatherInfo> getList(Integer grade,Integer gatherNo);

    void addCollection(MachineGatherInfo machineGatherInfo);

    void deleteCollection(long id);

    List<MachineGatherInfo> getById(Long id);

    void updateCollection(MachineGatherInfo machineGatherInfo);

    void importExcel(List<MachineGatherInfo> machineGatherInfoList);

    Long getDeptId(String name);


    Integer getStatus(String valueName);

    /**
     * @Date 2021/7/1 9:04
     * @Description  获取采集序号列表
     * @Params
     */
    List<MachineGatherInfo> queryGatherNos();

}
