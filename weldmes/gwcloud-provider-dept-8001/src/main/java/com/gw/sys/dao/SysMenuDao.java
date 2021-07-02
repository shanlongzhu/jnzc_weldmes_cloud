package com.gw.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gw.entities.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import java.math.BigInteger;
import java.util.List;

@Mapper
public interface SysMenuDao extends BaseMapper<SysMenu> {

    /**
     * 根据角色id查询角色菜单列表
     * @return
     */
    List<SysMenu> getSysMenuByRoleId(BigInteger roleId);

}
