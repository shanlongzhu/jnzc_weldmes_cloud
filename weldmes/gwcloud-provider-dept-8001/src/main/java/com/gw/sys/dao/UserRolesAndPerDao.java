package com.gw.sys.dao;

import com.gw.entities.SysMenuInfo;
import com.gw.entities.UserOfSys;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/4 13:21
 * @Description  用户角色、权限、菜单 dao层
 */
@Mapper
public interface UserRolesAndPerDao {

    /**
     * @Date 2021/6/4 13:22
     * @Description 通过 用户名 查询用户信息
     * @Params userName 用户名
     */
    public UserOfSys queryUserInfoByUserNameAndPwd(@Param("userName")String userName);

    /**
     * @Date 2021/6/7 16:56
     * @Description 通过用户id查询用户角色id
     * @Params
     */
    public List<Long> queryUserRoleIds(@Param("id")Long id);

    /**
     * @Date 2021/6/7 17:57
     * @Description 通过用户角色id 获取用户角色
     * @Params
     */
    public String queryUserRoleName(@Param("id")Long id);

    /**
     * @Date 2021/6/7 13:34
     * @Description 通过菜单id 查询 菜单目录信息
     * @Params id 菜单id
     */
    public SysMenuInfo queryMenuInfoByMenuId(@Param("id")Long id);

    /**
     * @Date 2021/6/8 11:19
     * @Description 根据菜单父级id 查询 子菜单信息
     * @Params
     */
    public List<SysMenuInfo> querySecondMenuInfo(@Param("id")Long id);

    /**
     * @Date 2021/6/8 9:03
     * @Description 通过 用户角色id 查询 用户菜单id
     * @Params
     */
    public List<Long> queryMenuIdByRoleId(@Param("id")Long id);

    /**
     * @Date 2021/6/8 15:52
     * @Description 通过菜单目录id、角色id 查询 子菜单id
     * @Params
     */
    public List<Long> queryChildrenMenuIdBySuperMenuIdAndRoleId(@Param("superMenuId")Long superMenuId,@Param("roleId")Long roleId);
}
