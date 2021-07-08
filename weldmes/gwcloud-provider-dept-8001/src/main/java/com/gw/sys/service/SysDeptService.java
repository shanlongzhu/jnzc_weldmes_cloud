package com.gw.sys.service;

import com.gw.entities.SysDept;
import org.springframework.web.bind.annotation.RequestParam;

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
     * @Params
     */
    public List<SysDept> getDeptInfos();

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
     * @Date 2021/7/8 16:38
     * @Description 树状图-查询组织机构信息
     * @Params id 组织机构id
     */
    public SysDept getTreeDeptInfos(Long id);

}
