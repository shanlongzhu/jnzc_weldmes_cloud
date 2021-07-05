package com.gw.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gw.entities.MenuAndButtonInfo;
import com.gw.entities.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface SysMenuDao extends BaseMapper<SysMenu> {

    /**
     * 根据角色id查询角色菜单列表
     * @return
     */
    List<SysMenu> getSysMenuByRoleId(BigInteger roleId);

    /**
     * @Date 2021/7/2 15:57
     * @Description  新增 目录/菜单/按钮信息
     * @Params  menuAndButtonInfo 目录/菜单/按钮信息
     */
    void insertMenuAndButtonInfo(@Param("menuAndButtonInfo") MenuAndButtonInfo menuAndButtonInfo);

    /**
     * @Date 2021/7/5 9:49
     * @Description 拉取菜单列表信息
     * @Params
     */
    List<MenuAndButtonInfo> selectMenuInfoList();

    /**
     * @Date 2021/7/5 9:49
     * @Description 通过 顶级菜单id 拉取子菜单列表信息
     * @Params
     */
    List<MenuAndButtonInfo> selectChildrenMenuInfoListByMenuId(@Param("id") Long id);

    /**
     * @Date 2021/7/5 15:33
     * @Description  根据id 删除菜单/按钮
     * @Params id 菜单/按钮id
     */
    void deleteMenuOrButtonInfoById(@Param("id") Long id);

}
