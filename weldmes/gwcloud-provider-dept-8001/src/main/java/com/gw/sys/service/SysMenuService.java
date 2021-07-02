package com.gw.sys.service;

import com.gw.common.PageInfo;
import com.gw.entities.SysMenu;

import java.math.BigInteger;
import java.util.List;

public interface SysMenuService {

    /**
     * 菜单分页查询
     * @param draw
     * @param start
     * @param length
     * @param sysMenu
     * @return
     */
    PageInfo<SysMenu> getSysMenuPage(int draw, int start, int length, SysMenu sysMenu);

    int addSysMenu(SysMenu sysMenu);

    int updateSysMenu(SysMenu sysMenu);

    int deleteSysMenu(List<BigInteger> ids);

    /**
     * 根据角色id查询角色列表
     * @param roleId
     * @return
     */
    List<SysMenu> getSysMenuByRoleId(BigInteger roleId);

    /**
     * 查询所有菜单列表，不分页
     * @return
     */
    List<SysMenu> getSysMenuAll();
}
