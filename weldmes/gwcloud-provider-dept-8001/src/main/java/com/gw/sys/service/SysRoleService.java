package com.gw.sys.service;

import com.gw.common.PageInfo;
import com.gw.entities.SysRole;

import java.math.BigInteger;
import java.util.List;

public interface SysRoleService {

    /**
     * 查询角色分页
     * @param draw
     * @param start
     * @param length
     * @param sysRole
     * @return
     */
    PageInfo<SysRole> getSysRolePage(int draw, int start, int length, SysRole sysRole);

    int addSysRole(SysRole sysRole);

    int updateSysRole(SysRole sysRole);

    int deleteSysRole(List<BigInteger> idss);
}
