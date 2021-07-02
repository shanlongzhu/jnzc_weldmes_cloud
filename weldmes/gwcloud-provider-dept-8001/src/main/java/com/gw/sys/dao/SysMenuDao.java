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

}
