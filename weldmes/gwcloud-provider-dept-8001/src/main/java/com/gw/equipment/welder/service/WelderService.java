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
    void addWelder(MachineWeldInfo machineWeldInfo);

    /**
     * @Date 2021/10/13 17:17
     * @Description 根据焊机id 查询焊机信息
     * @Params
     */
    List<MachineWeldInfo> getById(Long id);

    /**
     * @Date 2021/10/13 17:18
     * @Description 修改焊机信息
     * @Params
     */
    void updateWelder(MachineWeldInfo machineWeldInfo);

    /**
     * @Date 2021/10/13 17:18
     * @Description 删除焊机信息
     * @Params
     */
    void deleteWelder(Long id);

    /**
     * @Date 2021/10/13 17:18
     * @Description 根据部门名称查询部门id
     * @Params
     */
    Long getDeptId(String deptName);

    /**
     * @Date 2021/10/13 16:44
     * @Description 对焊机信息进行码值转换
     * @Params
     */
    MachineWeldInfo importExcel(MachineWeldInfo machineWeldInfos);

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

    /**
     * @Date 2021/10/13 17:15
     * @Description 批量插入焊机信息
     * @Params
     */
    public void addMachineWeldInfos(List<MachineWeldInfo> list);

}
