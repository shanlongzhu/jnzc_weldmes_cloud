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
                                  @Param("model")Integer model);

    int addWelder(@Param("machineWeldInfo")MachineWeldInfo machineWeldInfo);

    List<MachineWeldInfo> getById(Long id);

    int updateWelder(@Param("machineWeldInfo")MachineWeldInfo machineWeldInfo);

    int deleteWelder(Long id);

    Byte getTypeId(String type);

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

}
