package com.gw.sys.controller;

import com.gw.common.HttpResult;
import com.gw.entities.SysMenuInfo;
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
    public HttpResult getMenuInfoList(){

        List<SysMenuInfo> list = sysMenuService.getMenuInfoList();

        return HttpResult.ok(list);
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

}
