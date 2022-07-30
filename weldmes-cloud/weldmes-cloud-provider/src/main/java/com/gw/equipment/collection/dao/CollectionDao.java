package com.gw.equipment.collection.dao;


import com.gw.entities.MachineGatherInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CollectionDao {

    /**
     * @Date 2021/10/14 9:56
     * @Description 查询 采集信息列表
     * @Params
     */
    List<MachineGatherInfo> getList(@Param("grade")Integer grade, @Param("gatherNo")Integer gatherNo);

    /**
     * @Date 2021/10/14 9:57
     * @Description 插入采集信息
     * @Params
     */
    int addCollection(@Param("machineGatherInfo")MachineGatherInfo machineGatherInfo);

    /**
     * @Date 2021/10/14 9:57
     * @Description 删除采集信息
     * @Params
     */
    int deleteCollection(long id);

    /**
     * @Date 2021/10/14 9:58
     * @Description 根据id 查询 采集信息
     * @Params
     */
    List<MachineGatherInfo> getById(Long id);

    /**
     * @Date 2021/10/14 9:58
     * @Description 修改采集信息
     * @Params
     */
    int updateCollection(@Param("machineGatherInfo")MachineGatherInfo machineGatherInfo);

    /**
     * @Date 2021/10/14 9:59
     * @Description 批量 插入/修改 采集信息、excel导入数据库  采集编号不存在-新增 存在-更新
     * @Params
     */
    void addOrUpdateMachineGatherInfos(@Param("machineGatherInfo")MachineGatherInfo machineGatherInfo);

    /**
     * @Date 2021/10/14 9:59
     * @Description 根据部门名称 查询部门id
     * @Params
     */
    Long getDeptId(String name);

    /**
     * @Date 2021/10/14 10:00
     * @Description 根据内容 查询 字典表码值
     * @Params
     */
    Integer getStatus(String valueName);

    /**
     * @Date 2021/7/1 9:06
     * @Description 获取采集序号列表
     * @Params
     */
    List<MachineGatherInfo> queryGatherNos();

    /**
     * @Date 2021/8/5 15:18
     * @Description 判断采集编号是否存在
     * @Params
     */
    public Integer judgeGatherNoYesOrNo(@Param("GatherNo") String GatherNo,@Param("id") Long id);

    /**
     * 根据焊机ID查询采集编号
     *
     * @param weldId 焊机ID
     * @return 采集编号集合
     */
    List<MachineGatherInfo> getGatherNoByWeldId(@Param("weldId") Long weldId);

}
