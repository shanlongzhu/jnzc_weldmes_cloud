package com.gw.sys.dao;

import com.gw.entities.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  组织机构管理dao层
 * @Params
 */
@Mapper
public interface SysDeptDao{

    /**
     * @Date 2021/7/8 16:38
     * @Description 查询组织机构信息列表
     * @Params  id 部门id
     */
    public List<SysDept> selectDeptInfosByParentId(@Param("id")Long id);

    /**
     * @Date 2021/7/8 16:38
     * @Description 根据id查询组织机构信息
     * @Params id 组织机构id
     */
    public SysDept selectDeptInfoById(@Param("id")Long id);

    /**
     * @Date 2021/7/8 16:38
     * @Description 修改组织机构信息
     * @Params sysDept 组织机构信息
     */
    public void updateDeptInfo(@Param("sysDept")SysDept sysDept);

    /**
     * @Date 2021/7/8 16:38
     * @Description 根据id删除组织机构信息
     * @Params id 组织机构信息id
     */
    public void deleteDeptInfoById(@Param("id")Long id);

    /**
     * @Date 2021/7/8 16:38
     * @Description 新增组织机构信息
     * @Params sysDept 组织机构信息
     */
    public void insertDeptInfo(@Param("sysDept")SysDept sysDept);

    /**
     * @Date 2021/7/13 13:28
     * @Description  根据部门id以及部门名称筛选信息列表
     * @Params id 部门id   name  部门名称
     */
    public List<SysDept> selectDeptInfosByName(@Param("deptName")String deptName);

    /**
     * @Date 2021/8/27 13:55
     * @Description 根据用户机构id获取作业区
     * @Params
     */
    public SysDept selectWorkAreaDeptInfo(@Param("deptId")Long deptId);

    /**
     * @Date 2021/8/27 13:55
     * @Description 获取作业区信息
     * @Params
     */
    public List<SysDept> selectWorkAreaSysDeptInfo();

    /**
     * @Date 2021/10/20 15:45
     * @Description 通过组织机构id 查询 该部门下所有的班组id
     * @Params
     */
    public List<Long> selectNextDeptIdsById(@Param("deptId")String id);


}
