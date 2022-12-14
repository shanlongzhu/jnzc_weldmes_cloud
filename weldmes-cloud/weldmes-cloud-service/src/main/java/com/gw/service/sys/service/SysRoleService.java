package com.gw.service.sys.service;

import com.gw.entities.ManageRoleMenuInfo;
import com.gw.entities.SysRole;

import java.util.List;

public interface SysRoleService {

    /**
     * @Date 2021/7/6 12:51
     * @Description 角色列表查询
     * @Params
     */
    public List<SysRole> getRoleInfos();

    /**
     * @Date 2021/7/6 12:51
     * @Description 新增角色信息
     * @Params sysRole 角色信息
     */
    public void addRoleInfo(SysRole sysRole);

    /**
     * @Date 2021/7/6 11:18
     * @Description 根据角色id查询角色信息
     * @Params id 角色id
     */
    public SysRole getRoleInfoById(Long id);

    /**
     * @Date 2021/7/6 11:18
     * @Description 修改角色信息
     * @Params  sysRole 角色信息
     */
    public void updateRoleInfo(SysRole sysRole);

    /**
     * @Date 2021/7/6 11:18
     * @Description 根据id删除角色信息
     * @Params  id 角色id
     */
    public void delRoleInfoById(Long id);

    /**
     * @Date 2021/7/7 11:50
     * @Description 给角色分配权限
     * @Params  manageRoleMenuInfo 角色的菜单权限信息
     */
    public void addRoleMenuInfo(ManageRoleMenuInfo manageRoleMenuInfo);
}
