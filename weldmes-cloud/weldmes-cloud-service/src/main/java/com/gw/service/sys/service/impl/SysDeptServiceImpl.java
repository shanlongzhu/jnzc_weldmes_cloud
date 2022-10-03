package com.gw.service.sys.service.impl;

import com.gw.common.ConstantInfo;
import com.gw.common.DateTimeUtils;
import com.gw.entities.SysDept;
import com.gw.entities.UserLoginInfo;
import com.gw.service.process.dispatch.dao.DispatchDao;
import com.gw.service.sys.dao.SysDeptDao;
import com.gw.service.sys.service.SysDeptService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    SysDeptDao sysDeptDao;

    @Autowired
    DispatchDao dispatchDao;

    /**
     * @Date 2021/7/8 16:38
     * @Description 查询组织机构信息列表
     * @Params id 部门id
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

        String time = DateTimeUtils.getNowDateTime();

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

        //根据用户部门id、查询该部门下所有的下级部门信息
        List<SysDept> list = dispatchDao.queryGradeList(id);

        sysDeptDao.deleteDeptInfoById(id);

        if(!ObjectUtils.isEmpty(list)){
            do {
                List<SysDept> nextSysDeptInfos = new ArrayList<>();

                for (SysDept sysDeptInfo : list) {

                    //获取 当前用户所在部门 的 下级部门
                    List<SysDept> sysDeptList = sysDeptDao.selectDeptInfosByParentId(sysDeptInfo.getId());

                    nextSysDeptInfos.addAll(sysDeptList);

                    sysDeptDao.deleteDeptInfoById(sysDeptInfo.getId());

                }

                list.clear();

                list = nextSysDeptInfos;

            } while (!ObjectUtils.isEmpty(list));

        }

    }

    /**
     * @Date 2021/7/8 16:38
     * @Description 新增组织机构信息
     * @Params sysDept 组织机构信息
     */
    @Override
    public void addDeptInfo(SysDept sysDept) {

        String time = DateTimeUtils.getNowDateTime();

        sysDept.setCreateTime(time);

        sysDeptDao.insertDeptInfo(sysDept);

    }

    /**
     * @Date 2021/7/13 13:28
     * @Description 根据部门id以及部门名称筛选信息列表
     * @Params id 部门id   name  部门名称
     */
    @Override
    public List<SysDept> getDeptInfosByIdAndName(Long id, String name) {

        //通过部门名称模糊 获取部门信息列表
        List<SysDept> deptInfos = sysDeptDao.selectDeptInfosByName(name);

        List<SysDept> list = new ArrayList<>();

        //根据父级id进行筛选
        for (SysDept deptInfo : deptInfos) {
            if (deptInfo.getParentId() == id) {

                list.add(deptInfo);

            }
        }

        return list;
    }

    /**
     * @Date 2021/8/27 11:15
     * @Description 获取到作业区层级信息
     * @Params
     */
    @Override
    public List<SysDept> getDeptWorkInfos() {

        Subject currentUser = SecurityUtils.getSubject();

        //获取到当前用户
        UserLoginInfo userLoginInfo = (UserLoginInfo) currentUser.getPrincipal();

        //获取到当前用户所属的机构信息
        SysDept sysDept = sysDeptDao.selectWorkAreaDeptInfo(userLoginInfo.getDeptId());

        List<SysDept> deptInfos = new ArrayList<>();

        //用户为一级机构(集团)
        if (sysDept.getParentId() == ConstantInfo.GROUP_FLAG) {

            deptInfos = sysDeptDao.selectWorkAreaSysDeptInfo();

            return deptInfos;
        }

        //用户为二级机构(制造部)
        if (sysDept.getParentId() == ConstantInfo.MANUFACTUR_FLAG) {

            deptInfos = sysDeptDao.selectDeptInfosByParentId(sysDept.getParentId());

            return deptInfos;
        }

        //用户为三级机构(装焊区)
        deptInfos.add(sysDept);

        return deptInfos;
    }

}
