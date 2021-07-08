package com.gw.sys.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.SysMenuInfo;
import com.gw.entities.TaskInfo;
import com.gw.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  菜单控制器
 * @Params
 */
@RestController
@CrossOrigin
@RequestMapping("sysMenu")
public class SysMenuController {

    @Autowired
    SysMenuService sysMenuService;

    /**
     * @Date 2021/6/25 16:47
     * @Description 新增 目录/菜单/按钮信息
     * @Params
     */
    @RequestMapping("addMenuOrButtonInfo")
    public HttpResult insertMenuOrButtonInfo(@RequestBody SysMenuInfo menuAndButtonInfo){


        sysMenuService.addMuenOrButtonInfo(menuAndButtonInfo);

        return HttpResult.ok("新增成功");
    }

    /**
     * @Date 2021/7/5 9:47
     * @Description  拉取菜单列表信息
     * @Params
     */
    @RequestMapping("getMenuOrButtonInfo")
    public HttpResult getMenuInfoList(@RequestParam(value="pn",defaultValue = "1") Integer pn){

        PageHelper.startPage(pn,10);

        List<SysMenuInfo> list = sysMenuService.getMenuInfoList();

        //将查询结果进行分页
        PageInfo<TaskInfo> page=new PageInfo(list,10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/7/5 15:33
     * @Description  根据id 删除菜单/按钮
     * @Params id 菜单/按钮id
     */
    @RequestMapping("delMenuOrButtonInfoById")
    public HttpResult delMenuOrButtonInfoById(Long id){

        sysMenuService.delMenuOrButoonInfoById(id);

        return HttpResult.ok("删除成功!");
    }

    /**
     * @Date 2021/7/5 16:34
     * @Description  根据当前用户角色 查询用户的菜单以及按钮权限
     * @Params
     */
    @RequestMapping("getCurrentUserMenuAndButtonInfo")
    public HttpResult getCurrentUserMenuAndButtonInfo(){

        Map<String,Object> list = sysMenuService.getCurrentUserMenuAndButtonInfos();

        return HttpResult.ok(list);
    }

    /**
     * @Date 2021/7/6 10:49
     * @Description  修改菜单/按钮信息
     * @Params
     */
    @RequestMapping("updateMenuAndButtonInfo")
    public HttpResult updateRoleMenuAndButtonInfo(@RequestBody SysMenuInfo sysMenuInfo){

        sysMenuService.updateMenuOrButtonInfo(sysMenuInfo);

        return HttpResult.ok("修改成功!");
    }

    /**
     * @Date 2021/7/6 10:49
     * @Description  根据id查询菜单/按钮信息
     * @Params id  菜单/按钮id
     */
    @RequestMapping("getMenuAndButtonInfoById")
    public HttpResult getRoleMenuAndButtonInfoById(Long id){

        SysMenuInfo sysMenuInfo = sysMenuService.getMenuOrButtonInfoById(id);

        return HttpResult.ok(sysMenuInfo);
    }

    /**
     * @Date 2021/7/6 10:49
     * @Description  根据角色  查询该角色的菜单以及按钮权限
     * @Params  id  角色id
     */
    @RequestMapping("getMenuAndButtonInfoByRoleId")
    public HttpResult getRoleMenuAndButtonInfo(Long id){

        List<SysMenuInfo> list = sysMenuService.getMenuOrButtonInfoByRole(id);

        return HttpResult.ok(list);
    }
}
