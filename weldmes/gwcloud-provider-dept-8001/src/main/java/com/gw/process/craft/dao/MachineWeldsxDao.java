package com.gw.process.craft.dao;

import com.gw.entities.MachineWeldsxInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/25 15:27
 * @Description 松下设备Dao层
 */
@Mapper
public interface MachineWeldsxDao {

    /**
     * @Date 2021/8/25 15:33
     * @Description 松下设备列表查询
     * @Params
     */
    public List<MachineWeldsxInfo> selectMachineWeldsxInfos(@Param("weldModel")String weldModel);

    /**
     * @Date 2021/8/25 16:23
     * @Description 松下设备信息新增
     * @Params
     */
    public void insertMachineWeldsxInfo(@Param("machineWeldsxInfo")MachineWeldsxInfo machineWeldsxInfo);

    /**
     * @Date 2021/8/25 16:37
     * @Description 根据 id  查询 松下设备信息
     * @Params
     */
    public MachineWeldsxInfo selectMachineWeldsxInfoById(@Param("id") Long id);

    /**
     * @Date 2021/8/25 16:45
     * @Description 修改松下设备信息
     * @Params
     */
    public void updateMachineWeldsxInfo(@Param("machineWeldsxInfo")MachineWeldsxInfo machineWeldsxInfo);

    /**
     * @Date 2021/8/25 16:59
     * @Description 删除松下设备信息
     * @Params
     */
    public void deleteMachineWeldsxInfoById(@Param("id") Long id);

    /**
     * @Date 2021/8/26 10:07
     * @Description 设备序号 唯一性判断
     * @Params
     */
    public Integer judgeWeldNoYesOrNo(@Param("weldNo")String weldNo);

    /**
     * @Date 2021/8/26 10:07
     * @Description 设备CID 唯一性判断
     * @Params
     */
    public Integer judgeWeldCidYesOrNo(@Param("weldCid")String weldCid);
}
