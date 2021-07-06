package com.gw.sys.service;

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

}
