package com.gw.sys.dao;

import com.gw.entities.SysRole;
import com.gw.entities.SysUser;
import com.gw.entities.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/4 13:21
 * @Description  用户角色、权限、dao层
 */
@Mapper
public interface UserRolesAndPerDao {


    /**
     * @Date 2021/7/6 12:51
     * @Description 角色列表查询
     * @Params
     */
    public List<SysRole> selectRolesInfos();

    /**
     * @Date 2021/7/6 12:51
     * @Description 新增角色信息
     * @Params  sysRole 角色信息
     */
    public void insertRoleInfo(@Param("sysRole")SysRole sysRole);

    /**
     * @Date 2021/7/6 11:18
     * @Description 根据角色id查询角色信息
     * @Params id 角色id
     */
    public SysRole selectRoleInfoById(@Param("id")Long id);

    /**
     * @Date 2021/7/6 11:18
     * @Description 修改角色信息
     * @Params  sysRole 角色信息
     */
    public void updateRoleInfo(@Param("sysRole")SysRole sysRole);

    /**
     * @Date 2021/7/6 11:18
     * @Description 根据id删除角色信息
     * @Params  id 角色id
     */
    public void deleteRoleInfoById(@Param("id")Long id);

    /**
     * @Date 2021/6/4 13:22
     * @Description 通过 用户名 查询用户信息
     * @Params userName 用户名
     */
    public SysUser queryUserInfoByUserNameAndPwd(@Param("userName")String userName);

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
     * @Date 2021/6/8 9:03
     * @Description 通过 用户角色id 查询 用户菜单id
     * @Params
     */
    public List<Long> queryMenuIdByRoleId(@Param("id")Long id);

    /**
     * @Date 2021/7/7 11:50
     * @Description 给角色分配权限
     * @Params  id  角色id
     */
    public void insertRoleMenuInfo(@Param("roleId")Long roleId,@Param("menuId")Long menuId,@Param("time")String time);

    /**
     * @Date 2021/7/7 11:50
     * @Description 通过角色id删除  菜单id
     * @Params  id  角色id
     */
    public void deleteMenuIdByRoleId(@Param("roleId")Long roleId);

    /**
     * @Date 2021/7/8 8:47
     * @Description 通过用户id  修改用户角色id
     * @Params sysUserRole
     */
    public void updateRoleIdByUserId(@Param("sysUserRole")SysUserRole sysUserRole);

    /**
     * @Date 2021/7/8 9:44
     * @Description 新增用户角色信息
     * @Params
     */
    void insertUserRoleInfo(@Param("sysUserRole")SysUserRole sysUserRole);





}
