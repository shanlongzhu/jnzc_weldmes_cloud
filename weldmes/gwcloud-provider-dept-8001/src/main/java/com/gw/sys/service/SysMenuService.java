package com.gw.sys.service;

import com.gw.common.PageInfo;
import com.gw.entities.MenuAndButtonInfo;
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
     * @Date 2021/7/2 15:32
     * @Description  新增 目录/菜单/按钮信息
     * @Params menuAndButtonInfo 目录/菜单/按钮信息
     */
    void addMuenOrButtonInfo(MenuAndButtonInfo menuAndButtonInfo);
}
