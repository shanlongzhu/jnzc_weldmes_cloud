package com.gw.sys.service;

import com.gw.entities.SysMenuInfo;
import com.gw.entities.UserOfSys;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/4 10:47
 * @Description 用户角色权限业务层
 */
public interface UserRolesAndPerService {

    /**
     * @Date 2021/6/4 13:12
     * @Description  通过用户名 和密码 查询用户信息
     * @Params userName 用户名
     */
    public UserOfSys queryUserInfoByUserNameAndPwd(String userName);

    /**
     * @Date 2021/6/7 16:55
     * @Description 通过用户id 获取用户角色id
     * @Params
     */
    public List<Long> queryUserRoleIdList(Long id);

    /**
     * @Date 2021/6/7 17:55
     * @Description 通过用户角色id获取 用户角色
     * @Params
     */
    public List<String> queryUserRoles(List<Long> id);

    /**
     * @Date 2021/6/7 11:12
     * @Description  通过 角色id 查询 用户 对应角色的所有 菜单Id/按钮Id
     * @Params roleId 用户角色id
     */
    public List<Long> queryUserMenuIdList(Long roleId);

    /**
     * @Date 2021/6/7 14:23
     * @Description 通过用户菜单id 查询 菜单信息(菜单目录/子菜单项/菜单功能按钮)
     * @Params ids 菜单id列表  roleIds 角色id列表
     */
    public List<SysMenuInfo> queryMenuInfo(List<Long> menuIds,List<Long> roleIds);

}
