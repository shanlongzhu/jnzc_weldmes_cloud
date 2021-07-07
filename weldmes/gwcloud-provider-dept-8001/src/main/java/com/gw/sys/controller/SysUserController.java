package com.gw.sys.controller;

import com.gw.common.*;
import com.gw.entities.SysDeptInfo;
import com.gw.entities.SysUser;
import com.gw.entities.UpdateUserInfo;
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
     * @Description 通过部门id查询用户信息
     * @Params id 部门id
     */
    @RequestMapping(value = "user/getUserInfosByDeptId")
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
     * @Params sysUser 用户信息
     */
    @RequestMapping(value = "user/updateUserInfo")
    public HttpResult updateUserInfos(@RequestBody UpdateUserInfo updateUserInfo) {

        sysUserService.updateUserInfo(updateUserInfo);

        return HttpResult.ok("用户信息修改成功!");

    }

    /**
     * @Date 2021/7/7 18:04
     * @Description 删除用户信息    逻辑删除
     * @Params id 用户id
     */
    @RequestMapping(value = "user/delUserInfo")
    public HttpResult delUserInfo(Long id) {



        return HttpResult.ok("用户信息删除成功!");

    }

    /**
     * @Date 2021/7/7 18:04
     * @Description 拉取角色列表
     * @Params id 用户id
     */

}
