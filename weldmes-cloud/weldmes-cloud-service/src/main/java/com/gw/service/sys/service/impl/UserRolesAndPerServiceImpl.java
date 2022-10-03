package com.gw.service.sys.service.impl;

import com.gw.entities.SysUser;
import com.gw.service.sys.dao.UserRolesAndPerDao;
import com.gw.service.sys.service.UserRolesAndPerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserRolesAndPerServiceImpl implements UserRolesAndPerService {

    @Autowired
    UserRolesAndPerDao userRolesAndPerDao;

    /**
     * @Date 2021/6/4 13:20
     * @Description  通过用户名 查询用户信息
     * @Params userName 用户名
     */
    @Override
    public SysUser queryUserInfoByUserNameAndPwd(String userName) {

        SysUser sysUser = userRolesAndPerDao.queryUserInfoByUserNameAndPwd(userName);

        return sysUser;
    }

    /**
     * @Date 2021/6/7 11:19
     * @Description  通过 用户角色id 查询 用户菜单id
     * @Params  id   用户角色id
     */
    @Override
    public List<Long> queryUserMenuIdList(Long id) {

        //获取用户菜单id
        List<Long> menuIds = userRolesAndPerDao.queryMenuIdByRoleId(id);

        return menuIds;
    }

    /**
     * @Date 2021/6/7 16:55
     * @Description  通过用户id查询 用户角色id
     * @Params
     */
    @Override
    public List<Long> queryUserRoleIdList(Long id) {

        List<Long> roleIds = userRolesAndPerDao.queryUserRoleIds(id);

        return roleIds;
    }

    /**
     * @Date 2021/6/7 17:59
     * @Description 通过用户角色id 获取用户角色名称列表
     * @Params
     */
    @Override
    public List<String> queryUserRoles(List<Long> ids) {

        List<String> list = new ArrayList<>();

        for (Long id:ids) {

            String roleName = userRolesAndPerDao.queryUserRoleName(id);

            list.add(roleName);
        }

        return list;
    }
}
