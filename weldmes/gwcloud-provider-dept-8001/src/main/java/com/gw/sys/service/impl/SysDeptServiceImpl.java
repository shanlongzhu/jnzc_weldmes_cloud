package com.gw.sys.service.impl;

import com.gw.common.DateTimeUtil;
import com.gw.entities.SysDept;
import com.gw.sys.dao.SysDeptDao;
import com.gw.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * @Params  id 部门id
     */
    @Override
    public List<SysDept> getDeptInfos(Long id) {

        List<SysDept> list = new ArrayList<>();

        //根据parentId查询部门信息
        List<SysDept> sysDeptInfos = sysDeptDao.selectDeptInfosByParentId(id);

        //根据id查询部门信息
        SysDept sysDept = sysDeptDao.selectDeptInfoById(id);

        list.add(sysDept);

        list.addAll(sysDeptInfos);

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
     * @Date 2021/7/13 13:28
     * @Description  根据部门id以及部门名称筛选信息列表
     * @Params id 部门id   name  部门名称
     */
    @Override
    public List<SysDept> getDeptInfosByIdAndName(Long id, String name) {

        //通过部门名称模糊 获取部门信息列表
        List<SysDept> deptInfos = sysDeptDao.selectDeptInfosByName(name);

        List<SysDept> list = new ArrayList<>();

        //根据父级id进行筛选
        for (SysDept deptInfo : deptInfos)
        {
            if(deptInfo.getParentId() == id){

                list.add(deptInfo);

            }
        }

        return list;
    }

}
