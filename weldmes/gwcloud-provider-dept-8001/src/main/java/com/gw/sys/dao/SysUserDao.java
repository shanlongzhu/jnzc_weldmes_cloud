package com.gw.sys.dao;

import com.gw.entities.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
     * @Params id 部门id
     */
    List<SysUser> selectUserInfosByDeptId(@Param("id") Long id);

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
    void insertUserInfo(@Param("sysUser") SysUser sysUser);


}
