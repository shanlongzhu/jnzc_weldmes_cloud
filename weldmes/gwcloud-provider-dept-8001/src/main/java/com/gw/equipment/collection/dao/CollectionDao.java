package com.gw.equipment.collection.dao;


import com.gw.entities.MachineGatherInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CollectionDao {

    List<MachineGatherInfo> getList(@Param("grade")Integer grade, @Param("gatherNo")Integer gatherNo);

    int addCollection(@Param("machineGatherInfo")MachineGatherInfo machineGatherInfo);

    int deleteCollection(long id);

    List<MachineGatherInfo> getById(Long id);

    int updateCollection(@Param("machineGatherInfo")MachineGatherInfo machineGatherInfo);

    void save(MachineGatherInfo machineGatherInfo);

    Long getDeptId(String name);

    Integer getStatus(String valueName);

    Integer getProtocol(String valueNames);

    /**
     * @Date 2021/7/1 9:06
     * @Description 获取采集序号列表
     * @Params
     */
    List<MachineGatherInfo> queryGatherNos();
}
