package com.gw.service.sys.service;


import com.gw.entities.SysDept;
import com.gw.entities.SysUser;

import java.util.List;

public interface SysUserService {

    /**
     * @Date 2021/7/7 10:40
     * @Description  获取集团班组树状图信息
     * @Params
     */
    public SysDept getGradeInfo();

    /**
     * @Date 2021/7/7 16:29
     * @Description 通过部门查询用户信息列表
     * @Params deptId 部门id   userName用户名 loginName登录名 mobile手机号 roleId角色id
     */
    public List<SysUser> getUserInfosByDeptId(Long deptId,String userName,
                                              String loginName,String mobile,Long roleId);

    /**
     * @Date 2021/7/7 16:29
     * @Description 通过用户id查询用户信息
     * @Params id 用户id
     */
    public SysUser getUserInfosById(Long id);

    /**
     * @Date 2021/7/7 18:04
     * @Description 修改用户信息
     * @Params sysUser 用户信息
     */
    public void updateUserInfo(SysUser sysUser);

    /**
     * @Date 2021/7/7 18:04
     * @Description 删除用户信息    逻辑删除
     * @Params id 用户id
     */
    void delUserInfoById(Long id);

    /**
     * @Date 2021/7/7 18:04
     * @Description 新增用户
     * @Params sysUser 用户信息
     */
    void addUserInfo(SysUser sysUser);

    /**
     * @Date 2021/7/6 11:18
     * @Description 根据角色id查询用户信息列表
     * @Params id 角色id
     */
    public List<SysUser> getUserInfosByRoleId(Long id);

}
