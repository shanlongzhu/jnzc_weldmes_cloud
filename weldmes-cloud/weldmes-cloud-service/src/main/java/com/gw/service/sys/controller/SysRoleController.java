package com.gw.service.sys.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.ManageRoleMenuInfo;
import com.gw.entities.SysRole;
import com.gw.service.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class SysRoleController {

    @Autowired
    SysRoleService sysRoleService;

    /**
     * @Date 2021/7/6 11:18
     * @Description 角色列表查询-分页
     * @Params
     */
    @RequestMapping(value = "role/list", method = RequestMethod.GET)
    public HttpResult getRolesInfos(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {

        PageHelper.startPage(pn, 10);

        List<SysRole> list = sysRoleService.getRoleInfos();

        PageInfo<SysRole> page = new PageInfo<>(list, 10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/7/6 11:18
     * @Description 角色列表查询
     * @Params
     */
    @RequestMapping(value = "role/roleInfos")
    public HttpResult roleInfos() {

        List<SysRole> list = sysRoleService.getRoleInfos();

        return HttpResult.ok(list);
    }

    /**
     * @Date 2021/7/6 11:18
     * @Description 角色新增
     * @Params
     */
    @RequestMapping(value = "role/addRoleInfo")
    public HttpResult addRolesInfos(@RequestBody SysRole sysRole) {

        sysRoleService.addRoleInfo(sysRole);

        return HttpResult.ok("角色新增成功!");
    }

    /**
     * @Date 2021/7/6 11:18
     * @Description 根据角色id查询角色信息
     * @Params id 角色id
     */
    @RequestMapping(value = "role/getRoleInfoById")
    public HttpResult getRolesInfoById(Long id) {

        SysRole sysRole = sysRoleService.getRoleInfoById(id);

        return HttpResult.ok(sysRole);
    }

    /**
     * @Date 2021/7/6 11:18
     * @Description 修改角色信息
     * @Params sysRole 角色信息
     */
    @RequestMapping(value = "role/updateRoleInfo", method = RequestMethod.PUT)
    public HttpResult updateRoleInfo(@RequestBody SysRole sysRole) {

        sysRoleService.updateRoleInfo(sysRole);

        return HttpResult.ok("角色信息修改成功");
    }

    /**
     * @Date 2021/7/6 11:18
     * @Description 根据id删除角色信息
     * @Params id 角色id
     */
    @RequestMapping(value = "role/delRoleInfoById", method = RequestMethod.DELETE)
    public HttpResult delRolesInfoById(Long id) {

        sysRoleService.delRoleInfoById(id);

        return HttpResult.ok("角色信息删除成功");
    }

    /**
     * @Date 2021/7/7 11:50
     * @Description 给角色分配权限
     * @Params manageRoleMenuInfo 角色的菜单权限信息
     */
    @RequestMapping(value = "role/manageRoleMenuAndButtonInfo")
    public HttpResult manageRoleMenuAndButtonInfo(@RequestBody ManageRoleMenuInfo manageRoleMenuInfo) {

        sysRoleService.addRoleMenuInfo(manageRoleMenuInfo);

        return HttpResult.ok("角色成功绑定菜单信息");
    }


}
