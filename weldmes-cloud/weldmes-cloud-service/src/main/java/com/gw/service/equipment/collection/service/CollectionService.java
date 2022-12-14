package com.gw.service.equipment.collection.service;

import com.gw.entities.MachineGatherInfo;

import java.util.List;

public interface CollectionService {

    /**
     * @Date 2021/10/14 10:47
     * @Description 获取采集信息列表
     * @Params
     */
    List<MachineGatherInfo> getList(Integer grade,Integer gatherNo);

    /**
     * @Date 2021/10/14 10:47
     * @Description 添加采集信息
     * @Params
     */
    void addCollection(MachineGatherInfo machineGatherInfo);

    /**
     * @Date 2021/10/14 10:48
     * @Description 删除采集信息
     * @Params
     */
    void deleteCollection(long id);

    /**
     * @Date 2021/10/14 10:48
     * @Description 根据id 获取 采集信息
     * @Params
     */
    List<MachineGatherInfo> getById(Long id);

    /**
     * @Date 2021/10/14 10:48
     * @Description 修改采集信息
     * @Params
     */
    void updateCollection(MachineGatherInfo machineGatherInfo);

    /**
     * @Date 2021/10/14 10:48
     * @Description 采集信息码值转换
     * @Params
     */
    MachineGatherInfo importExcel(MachineGatherInfo data);

    /**
     * @Date 2021/10/14 10:50
     * @Description 根据部门名称 查询部门id
     * @Params
     */
    Long getDeptId(String name);

    /**
     * @Date 2021/7/1 9:04
     * @Description  获取采集序号列表
     * @Params
     */
    List<MachineGatherInfo> queryGatherNos();

    /**
     * @Date 2021/10/14 11:00
     * @Description 批量插入采集信息
     * @Params
     */
    public void addMachineGatherInfos(MachineGatherInfo machineGatherInfo);

    /**
     * 根据焊机ID查询采集编号
     *
     * @param weldId 焊机ID
     * @return 采集编号集合
     */
    List<MachineGatherInfo> getGatherNoByWeldId(Long weldId);

}
