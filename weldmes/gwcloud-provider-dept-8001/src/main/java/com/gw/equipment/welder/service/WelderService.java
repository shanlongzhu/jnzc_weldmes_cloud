package com.gw.equipment.welder.service;

import com.gw.entities.MachineWeldInfo;

import java.util.List;

public interface WelderService {
    List<MachineWeldInfo> getList(String machineNo,Integer type,Integer grade,Integer status,
                                  Integer firm,Long isNetwork,String gatherNo,String ipPath,Integer model);

    int addWelder(MachineWeldInfo machineWeldInfo);

    List<MachineWeldInfo> getById(Long id);

    int updateWelder(MachineWeldInfo machineWeldInfo);

    int deleteWelder(Long id);

    Byte getTypeId(String type);


    Long getDeptId(String deptName);

    Byte getStatusId(String status);

    Byte getFirmId(String firm);

    Long getGid(String machineNo);

    Byte getModelId(String model);

    void importExcel(List<MachineWeldInfo> machineWeldInfoArrayList);

    /**
     * @Date 2021/7/13 18:01
     * @Description 获取历史曲线中焊机id以及设备编号
     * @Params
     */
    public List<MachineWeldInfo> getIdAndMachineNoOfWelderInfos();
}
