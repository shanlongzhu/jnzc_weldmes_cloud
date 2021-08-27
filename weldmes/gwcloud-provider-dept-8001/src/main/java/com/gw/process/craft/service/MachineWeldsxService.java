package com.gw.process.craft.service;

import com.gw.entities.MachineWeldsxInfo;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/25 15:27
 * @Description 松下设备业务层
 */
public interface MachineWeldsxService {

    /**
     * @Date 2021/8/25 15:30
     * @Description 松下设备列表查询
     * @Params
     */
    public List<MachineWeldsxInfo> getMachineWeldsxInfos(String weldModel);

    /**
     * @Date 2021/8/25 16:22
     * @Description 松下设备新增
     * @Params
     */
    public void addMachineWeldsxInfo(MachineWeldsxInfo machineWeldsxInfo);

    /**
     * @Date 2021/8/25 16:36
     * @Description 根据 id  查询 松下设备信息
     * @Params
     */
    public MachineWeldsxInfo getMachineWeldsxInfoById(Long id);

    /**
     * @Date 2021/8/25 16:44
     * @Description 修改松下设备信息
     * @Params
     */
    public void updateMachineWeldsxInfo(MachineWeldsxInfo machineWeldsxInfo);

    /**
     * @Date 2021/8/25 16:59
     * @Description 删除松下设备信息
     * @Params
     */
    public void delMachineWeldsxInfo(Long id);
}