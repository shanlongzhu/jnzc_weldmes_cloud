package com.gw.sys.controller;

import com.gw.common.*;
import com.gw.entities.SysDeptInfo;
import com.gw.entities.SysUser;
import com.gw.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  用户控制器
 * @Params
 */
@RestController
@CrossOrigin
public class SysUserController {

    @Autowired
    SysUserService sysUserService;


    /**
     * @Date 2021/7/7 9:43
     * @Description 获取 当前用户 的班组树状图信息
     * @Params
     */
    @RequestMapping(value = "user/gradeInfos")
    public HttpResult getCurrentGradeInfos() {

        SysDeptInfo sysDeptInfo = sysUserService.getGradeInfo();

        return HttpResult.ok(sysDeptInfo);

    }

    /**
     * @Date 2021/7/7 16:29
     * @Description 条件查询用户信息
     * @Params id 部门id
     */
    @RequestMapping(value = "user/getUserInfosByOpt")
    public HttpResult getUserInfosByDeptId(Long id) {

        List<SysUser> list = sysUserService.getUserInfosByDeptId(id);

        return HttpResult.ok(list);

    }

    /**
     * @Date 2021/7/7 16:29
     * @Description 通过用户id查询用户信息
     * @Params id 用户id
     */
    @RequestMapping(value = "user/getUserInfosById")
    public HttpResult getUserInfosById(Long id) {

        SysUser sysUser = sysUserService.getUserInfosById(id);

        return HttpResult.ok(sysUser);

    }

    /**
     * @Date 2021/7/7 18:04
     * @Description 修改用户信息
     * @Params sysUser 用户信息  roleId 角色id
     */
    @RequestMapping(value = "user/updateUserInfo")
    public HttpResult updateUserInfos(@RequestBody SysUser sysUser) {

        sysUserService.updateUserInfo(sysUser);

        return HttpResult.ok("用户信息修改成功!");

    }

    /**
     * @Date 2021/7/7 18:04
     * @Description 删除用户信息    逻辑删除
     * @Params id 用户id
     */
    @RequestMapping(value = "user/delUserInfo")
    public HttpResult delUserInfo(Long id) {

        sysUserService.delUserInfoById(id);

        return HttpResult.ok("用户信息删除成功!");

    }

    /**
     * @Date 2021/7/7 18:04
     * @Description 新增用户
     * @Params sysUser 用户信息
     */
    @RequestMapping(value = "user/addUserInfo")
    public HttpResult addUserInfo(@RequestBody SysUser sysUser) {

        sysUserService.addUserInfo(sysUser);

        return HttpResult.ok("用户信息添加成功!");

    }

}
