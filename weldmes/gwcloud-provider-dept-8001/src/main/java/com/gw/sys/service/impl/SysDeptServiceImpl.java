package com.gw.sys.service.impl;

import com.gw.common.DateTimeUtil;
import com.gw.entities.DeptTreeInfo;
import com.gw.entities.SysDept;
import com.gw.sys.dao.SysDeptDao;
import com.gw.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  组织机构管理业务层
 * @Params
 */
@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    SysDeptDao sysDeptDao;


    /**
     * @Date 2021/7/8 16:38
     * @Description 查询组织机构信息列表
     * @Params  id 部门id   name 部门名称
     */
    @Override
    public List<DeptTreeInfo> getDeptInfos(Long id,String name) {

        List<DeptTreeInfo> list = sysDeptDao.selectDeptInfos(id,name);

        return list;
    }

    /**
     * @Date 2021/7/8 16:38
     * @Description 根据id查询组织机构信息
     * @Params id 组织机构id
     */
    @Override
    public SysDept getDeptInfoById(Long id) {

        SysDept sysDept = sysDeptDao.selectDeptInfoById(id);

        return sysDept;
    }

    /**
     * @Date 2021/7/8 16:38
     * @Description 修改组织机构信息
     * @Params sysDept 组织机构信息
     */
    @Override
    public void updateDeptInfo(SysDept sysDept) {

        String time = DateTimeUtil.getCurrentTime();

        sysDept.setLastUpdateTime(time);

        sysDeptDao.updateDeptInfo(sysDept);

    }

    /**
     * @Date 2021/7/8 16:38
     * @Description 根据id删除组织机构信息
     * @Params id 组织机构信息id
     */
    @Override
    public void delDeptInfoById(Long id) {

        sysDeptDao.deleteDeptInfoById(id);
    }

    /**
     * @Date 2021/7/8 16:38
     * @Description 新增组织机构信息
     * @Params sysDept 组织机构信息
     */
    @Override
    public void addDeptInfo(SysDept sysDept) {

        String time = DateTimeUtil.getCurrentTime();

        sysDept.setCreateTime(time);

        sysDeptDao.insertDeptInfo(sysDept);

    }

    /**
     * @Date 2021/7/8 16:38
     * @Description 树状图-查询组织机构信息
     * @Params id 部门id   name 部门名称
     *//*
    @Override
    public List<DeptTreeInfo> getTreeDeptInfos(Long id,String name) {

        List<DeptTreeInfo> list = sysDeptDao.selectDeptInfos(id,name);

        return list;
    }*/
}
