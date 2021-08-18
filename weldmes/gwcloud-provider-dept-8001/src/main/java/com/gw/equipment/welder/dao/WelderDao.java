package com.gw.equipment.welder.dao;


import com.gw.entities.MachineWeldInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WelderDao {
    List<MachineWeldInfo> getList(@Param("machineNo")String machineNo,@Param("type")Integer type,
                                  @Param("grade")Integer grade,@Param("status")Integer status,
                                  @Param("firm")Integer firm,@Param("isNetwork")Long isNetwork,
                                  @Param("gatherNo")String gatherNo,@Param("ipPath")String ipPath,
                                  @Param("model")Integer model,@Param("area")Integer area,@Param("bay")Integer bay);

    int addWelder(@Param("machineWeldInfo")MachineWeldInfo machineWeldInfo);

    List<MachineWeldInfo> getById(Long id);

    int updateWelder(@Param("machineWeldInfo")MachineWeldInfo machineWeldInfo);

    int deleteWelder(Long id);

    Byte getTypeId(@Param("type")String type,@Param("dictionaryType")String dictionaryType);

    Long getDeptId(String deptName);

    Byte getStatusId(String status);

    Byte getFirmId(String firm);

    Long getGid(String machineNo);

    Byte getModelId(String model);

    void save(MachineWeldInfo machineWeldInfo);

    /**
     * @Date 2021/6/1 16:39
     * @Description  通过焊工id 查询该焊工的部门id
     * @Params
     */
    List<Long> getDeptIdByWelderId(@Param("id")Long id);

    /**
     * @Date 2021/7/13 18:01
     * @Description 获取历史曲线中焊机id以及设备编号
     * @Params
     */
    public List<MachineWeldInfo> selectIdAndMachineNoOfWelderInfos();

    /**
     * @Date 2021/7/29 14:10
     * @Description 根据 部门d列表 查询设备信息列表
     * @Params
     */
    public List<MachineWeldInfo> selectMachineWeldInfosByDeptIds(@Param("deptIds")List<Long> deptIds);

    /**
     * @Date 2021/8/12 18:22
     * @Description 查询焊机列表(是否绑定了任务)
     * @Params
     */
    public List<MachineWeldInfo> selectMachineWeldInfosNoPage(@Param("machineNo")String machineNo,@Param("type")Integer type,
                                                              @Param("grade")Integer grade,@Param("status")Integer status,
                                                              @Param("firm")Integer firm,@Param("isNetwork")Long isNetwork,
                                                              @Param("gatherNo")String gatherNo,@Param("ipPath")String ipPath,
                                                              @Param("model")Integer model,@Param("area")Integer area,@Param("bay")Integer bay);

    /**
     * @Date 2021/8/18 17:36
     * @Description 批量插入
     * @Params
     */
    public void insertMachineWeldInfoByGroup(@Param("machineWeldInfos")List<MachineWeldInfo> machineWeldInfos);

}
