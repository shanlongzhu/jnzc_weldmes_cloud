package com.gw.equipment.welder.dao;


import com.gw.entities.EquipFeatureInfo;
import com.gw.entities.MachineWeldInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WelderDao {

    /**
     * @Date 2021/10/11 17:35
     * @Description 添加焊工信息
     * @Params
     */
    int addWelder(@Param("machineWeldInfo")MachineWeldInfo machineWeldInfo);


    List<MachineWeldInfo> getById(Long id);

    /**
     * @Date 2021/10/11 17:36
     * @Description 修改焊工信息
     * @Params
     */
    int updateWelder(@Param("machineWeldInfo")MachineWeldInfo machineWeldInfo);

    /**
     * @Date 2021/10/11 17:36
     * @Description 删除焊工信息
     * @Params
     */
    int deleteWelder(Long id);

    /**
     * @Date 2021/10/11 17:41
     * @Description
     * @Params
     */
    Byte getTypeId(@Param("type")String type,@Param("dictionaryType")String dictionaryType);

    /**
     * @Date 2021/10/11 17:37
     * @Description 根据部门名称获取部门id
     * @Params
     */
    Long getDeptId(String deptName);

    Byte getStatusId(String status);

    /**
     * @Date 2021/10/11 17:38
     * @Description
     * @Params
     */
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
     * @Description 查询焊机列表
     * @Params
     */
    public List<MachineWeldInfo> selectMachineWeldInfos(@Param("machineNo")String machineNo,@Param("type")Integer type,
                                                              @Param("grade")Integer grade,@Param("status")Integer status,
                                                              @Param("firm")Integer firm,@Param("isNetwork")Long isNetwork,
                                                              @Param("gatherNo")String gatherNo,@Param("ipPath")String ipPath,
                                                              @Param("model")Integer model,@Param("area")Integer area,@Param("bay")Integer bay);

    /**
     * @Date 2021/8/18 17:36
     * @Description 批量插入
     * @Params
     */
    public void insertMachineWeldInfoByGroup(@Param("list")List<MachineWeldInfo> list);

    /**
     * @Date 2021/10/12 15:40
     * @Description 焊机是否绑定任务列表查询
     * @Params
     */
    public List<MachineWeldInfo> getStatusOfMachineWeldInfos(@Param("machineNo")String machineNo,@Param("type")Integer type,
                                                              @Param("grade")Integer grade,@Param("status")Integer status,
                                                              @Param("firm")Integer firm,@Param("isNetwork")Long isNetwork,
                                                              @Param("gatherNo")String gatherNo,@Param("ipPath")String ipPath,
                                                              @Param("model")Integer model,@Param("area")Integer area,
                                                             @Param("bay")Integer bay,@Param("time1")String time1,
                                                             @Param("time2")String time2);

    /**
     * @Date 2021/10/22 9:23
     * @Description 根据焊机标识、焊机id 获取OTC焊机特征信息
     * @Params
     */
    public EquipFeatureInfo selectOTCEquipFeatureInfo(@Param("macFlag")Integer macFlag, @Param("id")Long id);

    /**
     * @Date 2021/10/22 9:23
     * @Description 根据焊机标识、焊机id 获取松下焊机特征信息
     * @Params
     */
    public EquipFeatureInfo selectSXEquipFeatureInfo(@Param("macFlag")Integer macFlag, @Param("weldCid")String weldCid);

    /**
     * @Date 2021/10/22 9:47
     * @Description 根据焊机Id 查询 OTC焊机 焊接时长
     * @Params
     */
    public String selectOTCMachineWeldDuration(@Param("id")Long id);

    /**
     * @Date 2021/10/22 10:40
     * @Description 根据焊机设备CID 查询 松下焊机 焊接时长
     * @Params
     */
    public String selectSXMachineWeldDuration(@Param("weldCid")String weldCid);




}
