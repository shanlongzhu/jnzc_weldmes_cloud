package com.gw.service.sys.dao;

import com.gw.entities.SysMenuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface SysMenuDao {

    /**
     * 根据角色id查询角色菜单列表
     * @return
     */
    List<SysMenuInfo> getSysMenuByRoleId(BigInteger roleId);

    /**
     * @Date 2021/7/2 15:57
     * @Description  新增 目录/菜单/按钮信息
     * @Params  menuAndButtonInfo 目录/菜单/按钮信息
     */
    void insertMenuAndButtonInfo(@Param("menuAndButtonInfo") SysMenuInfo menuAndButtonInfo);

    /**
     * @Date 2021/7/5 9:49
     * @Description 拉取菜单列表信息
     * @Params
     */
    List<SysMenuInfo> selectMenuInfoList();

    /**
     * @Date 2021/7/5 9:49
     * @Description 通过 顶级菜单id 拉取子菜单列表信息
     * @Params
     */
    List<SysMenuInfo> selectChildrenMenuInfoListByMenuId(@Param("id") Long id);

    /**
     * @Date 2021/7/5 15:33
     * @Description  根据id 删除菜单/按钮
     * @Params id 菜单/按钮id
     */
    void deleteMenuOrButtonInfoById(@Param("id") Long id);

    /**
     * @Date 2021/6/7 13:34
     * @Description 通过菜单id 查询 菜单目录信息
     * @Params ids 菜单id列表
     */
    public List<SysMenuInfo> queryMenuInfoByMenuId(@Param("ids")List<Long> ids);

    /**
     * @Date 2021/7/5 15:33
     * @Description  修改菜单/按钮权限信息
     * @Params  menuAndButtonInfo 菜单/按钮权限信息
     */
    public void updateMenuOrButtonInfo(@Param("sysMenuInfo") SysMenuInfo sysMenuInfo);


}
