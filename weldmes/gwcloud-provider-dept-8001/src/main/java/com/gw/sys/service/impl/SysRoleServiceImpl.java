package com.gw.sys.service.impl;

import com.gw.common.DateTimeUtil;
import com.gw.entities.SysRole;
import com.gw.entities.UserLoginInfo;
import com.gw.sys.dao.UserRolesAndPerDao;
import com.gw.sys.service.SysRoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/7/5 10:12
 * @Description  角色业务实现层
 * @Params
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    UserRolesAndPerDao userRolesAndPerDao;


    /**
     * @Date 2021/7/6 12:51
     * @Description 角色列表查询
     * @Params
     */
    @Override
    public List<SysRole> getRoleInfos() {

        List<SysRole> list = userRolesAndPerDao.selectRolesInfos();

        return list;
    }

    /**
     * @Date 2021/7/6 12:51
     * @Description 新增角色信息
     * @Params  sysRole 角色信息
     */
    @Override
    public void addRoleInfo(SysRole sysRole) {

        //获取系统当前时间
        String time = DateTimeUtil.getCurrentTime();

        sysRole.setCreateTime(time);

        //获取到当前用户
        /*Subject currentUser = SecurityUtils.getSubject();

        UserLoginInfo userInfo = (UserLoginInfo)currentUser.getPrincipal();

        sysRole.setCreateBy(userInfo.getUserName());*/

        userRolesAndPerDao.insertRoleInfo(sysRole);

    }

    /**
     * @Date 2021/7/6 11:18
     * @Description 根据角色id查询角色信息
     * @Params id 角色id
     */
    @Override
    public SysRole getRoleInfoById(Long id) {

        SysRole sysRole = userRolesAndPerDao.selectRoleInfoById(id);

        return sysRole;
    }

    /**
     * @Date 2021/7/6 11:18
     * @Description 修改角色信息
     * @Params  sysRole 角色信息
     */
    @Override
    public void updateRoleInfo(SysRole sysRole) {

        //获取系统当前时间
        String time = DateTimeUtil.getCurrentTime();

        //获取到当前用户
        /*Subject currentUser = SecurityUtils.getSubject();

        UserLoginInfo userInfo = (UserLoginInfo)currentUser.getPrincipal();

        sysRole.setCreateBy(userInfo.getUserName());*/

        sysRole.setLastUpdateTime(time);

        userRolesAndPerDao.updateRoleInfo(sysRole);
    }

    /**
     * @Date 2021/7/6 11:18
     * @Description 根据id删除角色信息
     * @Params  id 角色id
     */
    @Override
    public void delRoleInfoById(Long id) {

        userRolesAndPerDao.deleteRoleInfoById(id);
    }
}
