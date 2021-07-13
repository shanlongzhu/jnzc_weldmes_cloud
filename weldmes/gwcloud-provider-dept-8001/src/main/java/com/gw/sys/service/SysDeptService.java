package com.gw.sys.service;

import com.gw.entities.SysDept;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  组织机构管理业务层
 * @Params
 */
public interface SysDeptService {

    /**
     * @Date 2021/7/8 16:38
     * @Description 查询组织机构信息列表
     * @Params  id 部门id
     */
    public List<SysDept> getDeptInfos(Long id);

    /**
     * @Date 2021/7/8 16:38
     * @Description 根据id查询组织机构信息
     * @Params id 组织机构id
     */
    public SysDept getDeptInfoById(Long id);

    /**
     * @Date 2021/7/8 16:38
     * @Description 修改组织机构信息
     * @Params sysDept 组织机构信息
     */
    public void updateDeptInfo(SysDept sysDept);

    /**
     * @Date 2021/7/8 16:38
     * @Description 根据id删除组织机构信息
     * @Params id 组织机构信息id
     */
    public void delDeptInfoById(Long id);

    /**
     * @Date 2021/7/8 16:38
     * @Description 新增组织机构信息
     * @Params sysDept 组织机构信息
     */
    public void addDeptInfo(SysDept sysDept);

    /**
     * @Date 2021/7/13 13:28
     * @Description  根据部门id以及部门名称筛选信息列表
     * @Params id 部门id   name  部门名称
     */
    public List<SysDept> getDeptInfosByIdAndName(Long id,String name);

}
