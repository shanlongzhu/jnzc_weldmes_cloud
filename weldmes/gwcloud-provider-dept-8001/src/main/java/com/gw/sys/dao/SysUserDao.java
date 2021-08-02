package com.gw.sys.dao;

import com.gw.entities.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/7/7 16:21
 * @Description  用户管理dao层
 */
@Mapper
public interface SysUserDao {

    /**
     * @Date 2021/7/7 16:29
     * @Description 条件查询用户信息
     * @Params ids 部门id列表   userName用户名 loginName登录名 mobile手机号 roleId角色id
     */
    List<SysUser> selectUserInfosByDeptId(@Param("ids")List<Long> ids,@Param("userName")String userName,
                                          @Param("loginName")String loginName,@Param("mobile")String mobile,@Param("roleId")Long roleId);

    /**
     * @Date 2021/7/7 16:29
     * @Description 通过用户id查询用户信息
     * @Params id 用户id
     */
    SysUser selectUserInfosById(@Param("id") Long id);

    /**
     * @Date 2021/7/7 18:04
     * @Description 修改用户信息
     * @Params sysUser 用户信息
     */
    void updateUserInfo(@Param("sysUser") SysUser sysUser);

    /**
     * @Date 2021/7/7 18:04
     * @Description 新增用户
     * @Params sysUser 用户信息
     */
    Long insertUserInfo(@Param("sysUser") SysUser sysUser);

    /**
     * @Date 2021/7/6 11:18
     * @Description 根据角色id查询用户信息列表
     * @Params id 角色id
     */
    public List<SysUser> selectUserInfosByRoleId(@Param("roleId")Long roleId);


}
