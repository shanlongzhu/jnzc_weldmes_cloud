package com.gw.sys.service;


import com.gw.entities.SysDeptInfo;
import com.gw.entities.SysUser;
import com.gw.entities.UpdateUserInfo;

import java.util.List;


/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  用户业务层
 * @Params
 */
public interface SysUserService {

    /**
     * @Date 2021/7/7 10:40
     * @Description  获取集团班组树状图信息
     * @Params
     */
    public SysDeptInfo getGradeInfo();

    /**
     * @Date 2021/7/7 16:29
     * @Description 通过部门id查询用户信息
     * @Params id 部门id
     */
    public List<SysUser> getUserInfosByDeptId(Long id);

    /**
     * @Date 2021/7/7 16:29
     * @Description 通过用户id查询用户信息
     * @Params id 用户id
     */
    public SysUser getUserInfosById(Long id);

    /**
     * @Date 2021/7/7 18:04
     * @Description 修改用户信息
     * @Params sysUser 用户信息
     */
    public void updateUserInfo(UpdateUserInfo updateUserInfo);

    /**
     * @Date 2021/7/7 18:04
     * @Description 删除用户信息    逻辑删除
     * @Params id 用户id
     */
    void delUserInfoById(Long id);






}
