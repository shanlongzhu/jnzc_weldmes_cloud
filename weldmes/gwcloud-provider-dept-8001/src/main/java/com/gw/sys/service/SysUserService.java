package com.gw.sys.service;

import com.gw.common.PageInfo;
import com.gw.entities.SysUser;
import java.math.BigInteger;
import java.util.List;

public interface SysUserService {

    /**
     * 查询用户分页
     * @param draw
     * @param start
     * @param length
     * @param sysUser
     * @return
     */
    PageInfo<SysUser> UserPage(int draw, int start, int length, SysUser sysUser);

    int addUser(SysUser sysUser);

    SysUser getUserById(SysUser sysUser);

    int updateUser(SysUser sysUser);

    int deleteUser(List<BigInteger> ids);

    boolean login(SysUser sysUser);

}
