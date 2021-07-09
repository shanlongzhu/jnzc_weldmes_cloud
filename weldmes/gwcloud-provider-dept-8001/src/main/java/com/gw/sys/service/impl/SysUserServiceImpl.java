package com.gw.sys.service.impl;


import com.gw.common.DateTimeUtil;
import com.gw.entities.*;
import com.gw.process.dispatch.dao.DispatchDao;
import com.gw.sys.dao.SysUserDao;
import com.gw.sys.dao.UserRolesAndPerDao;
import com.gw.sys.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  用户业务实现层
 * @Params
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserDao sysUserDao;

    @Autowired
    DispatchDao dispatchDao;

    @Autowired
    UserRolesAndPerDao userRolesAndPerDao;


    /**
     * @Date 2021/7/7 10:40
     * @Description  获取集团班组树状图信息
     * @Params
     */
    @Override
    public SysDept getGradeInfo() {

        Subject currentUser = SecurityUtils.getSubject();

        //获取到当前用户
        UserLoginInfo userLoginInfo = (UserLoginInfo)currentUser.getPrincipal();

        //获取到当前用户所属的机构信息
        SysDept sysDeptInfo = dispatchDao.queryDeptNameListById(userLoginInfo.getDeptId());

        //获取二级部门信息列表
        List<SysDept> sysSecondDeptInfos = dispatchDao.queryGradeList(sysDeptInfo.getId());

        if(!ObjectUtils.isEmpty(sysSecondDeptInfos)){

            for (SysDept sysSecondDeptInfo : sysSecondDeptInfos) {

                //获取三级部门信息列表
                List<SysDept> sysThirdDeptInfos = dispatchDao.queryGradeList(sysSecondDeptInfo.getId());

                if(!ObjectUtils.isEmpty(sysThirdDeptInfos)){

                    for (SysDept sysThirdDeptInfo : sysThirdDeptInfos) {

                        //获取四级部门信息列表
                        List<SysDept> sysFourthDeptInfos = dispatchDao.queryGradeList(sysThirdDeptInfo.getId());

                        if(!ObjectUtils.isEmpty(sysFourthDeptInfos)){

                            //将四级部门信息放入三级部门信息中
                            sysThirdDeptInfo.setList(sysFourthDeptInfos);
                        }
                    }
                    sysSecondDeptInfo.setList(sysThirdDeptInfos);
                }
            }
            sysDeptInfo.setList(sysSecondDeptInfos);
        }

        return sysDeptInfo;
    }

    /**
     * @Date 2021/7/7 16:29
     * @Description 条件查询用户信息
     * @Params deptId 部门id   userName用户名 loginName登录名 mobile手机号 roleId角色id
     */
    @Override
    public List<SysUser> getUserInfosByDeptId(Long deptId,String userName,
                                              String loginName,String mobile,Long roleId) {

        List<SysUser> sysUsers = sysUserDao.selectUserInfosByDeptId(deptId,userName,loginName,mobile,roleId);

        return sysUsers;
    }

    /**
     * @Date 2021/7/7 16:29
     * @Description 通过用户id查询用户信息
     * @Params id 用户id
     */
    @Override
    public SysUser getUserInfosById(Long id) {

        SysUser sysUser = sysUserDao.selectUserInfosById(id);

        return sysUser;
    }

    /**
     * @Date 2021/7/7 18:04
     * @Description 修改用户信息
     * @Params sysUser 用户信息  roleId 角色id
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void updateUserInfo(SysUser sysUser) {

        SysUserRole sysUserRole = new SysUserRole();

        //获取当前系统时间
        String time = DateTimeUtil.getCurrentTime();

        sysUser.setLastUpdateTime(time);

        sysUserRole.setUserId(sysUser.getId());

        sysUserRole.setRoleId(sysUser.getRoleId());

        sysUserRole.setLastUpdateTime(time);

        //修改用户信息
        sysUserDao.updateUserInfo(sysUser);

        //修改用户角色信息
        userRolesAndPerDao.updateRoleIdByUserId(sysUserRole);

    }

    /**
     * @Date 2021/7/7 18:04
     * @Description 删除用户信息    逻辑删除
     * @Params id 用户id
     */
    @Override
    public void delUserInfoById(Long id) {

        //获取当前系统时间
        String time = DateTimeUtil.getCurrentTime();

        SysUser sysUser = new SysUser();

        sysUser.setId(id);

        sysUser.setLastUpdateTime(time);

        //是否删除  -1：已删除  0：正常
        sysUser.setDelFlag(-1);

        sysUserDao.updateUserInfo(sysUser);

    }

    /**
     * @Date 2021/7/7 18:04
     * @Description 新增用户
     * @Params sysUser 用户信息 roleId 角色id
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void addUserInfo(SysUser sysUser) {

        //获取当前系统时间
        String time = DateTimeUtil.getCurrentTime();

        //将时间放入用户信息
        sysUser.setCreateTime(time);

        //新增用户信息
        sysUserDao.insertUserInfo(sysUser);

        //获取新增用户的id
        SysUser addUser = userRolesAndPerDao.queryUserInfoByUserNameAndPwd(sysUser.getName());

        SysUserRole sysUserRole = new SysUserRole();

        //将新增角色信息时需要的信息 放入用户角色实体类
        sysUserRole.setUserId(addUser.getId());

        sysUserRole.setRoleId(sysUser.getRoleId());

        sysUserRole.setCreateTime(time);

        //新增用户角色信息
        userRolesAndPerDao.insertUserRoleInfo(sysUserRole);

    }
}
