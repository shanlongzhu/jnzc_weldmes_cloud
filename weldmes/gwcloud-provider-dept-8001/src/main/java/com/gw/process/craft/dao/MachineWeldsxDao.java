package com.gw.process.craft.dao;

import com.gw.entities.MachineWeldsxInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/25 15:27
 * @Description 松下工艺库Dao层
 */
@Mapper
public interface MachineWeldsxDao {

    /**
     * @Date 2021/8/25 15:33
     * @Description 松下工艺库列表查询
     * @Params
     */
    public List<MachineWeldsxInfo> selectMachineWeldsxInfos();

    /**
     * @Date 2021/8/25 16:23
     * @Description 工艺库信息新增
     * @Params
     */
    public void insertMachineWeldsxInfo(@Param("machineWeldsxInfo")MachineWeldsxInfo machineWeldsxInfo);

    /**
     * @Date 2021/8/25 16:37
     * @Description 根据 id  查询 工艺库信息
     * @Params
     */
    public MachineWeldsxInfo selectMachineWeldsxInfoById(@Param("id") Long id);

    /**
     * @Date 2021/8/25 16:45
     * @Description 修改工艺库信息
     * @Params
     */
    public void updateMachineWeldsxInfo(@Param("machineWeldsxInfo")MachineWeldsxInfo machineWeldsxInfo);

    /**
     * @Date 2021/8/25 16:59
     * @Description 删除工艺库信息
     * @Params
     */
    public void deleteMachineWeldsxInfoById(@Param("id") Long id);
}
