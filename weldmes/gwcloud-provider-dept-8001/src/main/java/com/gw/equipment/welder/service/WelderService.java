package com.gw.equipment.welder.service;

import com.gw.entities.MachineWeldInfo;

import java.util.List;

/**
 * @Date 2021/10/12 15:31
 * @Description 焊机业务层
 * @Params
 */
public interface WelderService {

    /**
     * @Date 2021/10/12 15:32
     * @Description 焊机信息列表查询
     * @Params
     */
    List<MachineWeldInfo> getList(String machineNo,Integer type,Integer grade,Integer status,
                                  Integer firm,Long isNetwork,String gatherNo,String ipPath,Integer model,Integer area,Integer bay);

    /**
     * @Date 2021/10/12 15:32
     * @Description 新增焊机信息
     * @Params
     */
    int addWelder(MachineWeldInfo machineWeldInfo);

    List<MachineWeldInfo> getById(Long id);

    int updateWelder(MachineWeldInfo machineWeldInfo);

    int deleteWelder(Long id);

    Byte getTypeId(String type,String dictionaryType);

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

    /**
     * @Date 2021/7/29 13:20
     * @Description  根据部门id查询设备信息列表
     * @Params id 部门id
     */
    public List<MachineWeldInfo> getWeldInfos(Long id);

    /**
     * @Date 2021/10/12 15:34
     * @Description 焊机是否绑定任务列表查询
     * @Params
     */
    public List<MachineWeldInfo> getStatusOfMachineWeldInfos(String machineNo,Integer type,Integer grade,Integer status,
                                                             Integer firm,Long isNetwork,String gatherNo,String ipPath,
                                                             Integer model,Integer area,Integer bay);

}
