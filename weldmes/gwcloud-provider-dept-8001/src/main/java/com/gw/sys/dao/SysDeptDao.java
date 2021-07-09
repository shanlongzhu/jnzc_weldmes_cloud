package com.gw.sys.dao;

import com.gw.entities.DeptTreeInfo;
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
     * @Params  id 部门id   name 部门名称
     */
    public List<DeptTreeInfo> selectDeptInfos(@Param("id")Long id,@Param("name")String name);

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

}
