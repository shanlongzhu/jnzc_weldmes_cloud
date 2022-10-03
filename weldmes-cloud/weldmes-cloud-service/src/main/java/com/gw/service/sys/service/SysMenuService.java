package com.gw.service.sys.service;

import com.gw.entities.SysMenuInfo;

import java.util.List;
import java.util.Map;

public interface SysMenuService {

    /**
     * @Date 2021/7/2 15:32
     * @Description  新增 目录/菜单/按钮信息
     * @Params menuAndButtonInfo 目录/菜单/按钮信息
     */
    void addMuenOrButtonInfo(SysMenuInfo menuAndButtonInfo);

    /**
     * @Date 2021/7/5 9:49
     * @Description 拉取菜单列表信息
     * @Params
     */
    List<SysMenuInfo> getMenuInfoList();

    /**
     * @Date 2021/7/5 15:33
     * @Description  根据id 删除菜单/按钮
     * @Params id 菜单/按钮id
     */
    void delMenuOrButoonInfoById(Long id);

    /**
     * @Date 2021/7/5 15:33
     * @Description  查询当前用户的菜单/按钮权限信息
     * @Params
     */
    Map<String,Object> getCurrentUserMenuAndButtonInfos();

    /**
     * @Date 2021/7/5 15:33
     * @Description  修改菜单/按钮权限信息
     * @Params  menuAndButtonInfo 菜单/按钮权限信息
     */
    public void updateMenuOrButtonInfo(SysMenuInfo menuAndButtonInfo);

    /**
     * @Date 2021/7/6 10:49
     * @Description  根据id查询菜单/按钮信息
     * @Params id  菜单/按钮id
     */
    public SysMenuInfo getMenuOrButtonInfoById(Long id);

    /**
     * @Date 2021/7/6 10:49
     * @Description  根据角色  查询该角色的菜单以及按钮权限
     * @Params  id  角色id
     */
    List<SysMenuInfo> getMenuOrButtonInfoByRole(Long id);

}
