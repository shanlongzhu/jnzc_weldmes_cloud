package com.gw.sys.controller;

import com.gw.common.CommonUtil;
import com.gw.common.DateTimeUtil;
import com.gw.common.HttpResult;
import com.gw.common.PageInfo;
import com.gw.entities.MenuAndButtonInfo;
import com.gw.entities.SysMenu;
import com.gw.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("sysMenu")
public class SysMenuController {

    @Autowired
    SysMenuService sysMenuService;

    @GetMapping
    public PageInfo<SysMenu> getSysMenuPage(HttpServletRequest request, SysMenu sysMenu) {
        //DataTables参数
        String draw = request.getParameter("draw");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        int intDraw = draw == null ? 0 : Integer.parseInt(draw);
        int intStart = start == null ? 0 : Integer.parseInt(start);
        int intLength = length == null ? 10 : Integer.parseInt(length);
        //封装 DataTables 需要的数据
        return sysMenuService.getSysMenuPage(intDraw, intStart, intLength, sysMenu);
    }

    @PostMapping
    public HttpResult addSysMenu(SysMenu sysMenu){
        try {
            HttpResult result = new HttpResult();
            if (null != sysMenu) {
                sysMenu.setCreateBy("admin");
                sysMenu.setCreateTime(DateTimeUtil.sdf.format(System.currentTimeMillis()));
                int i = sysMenuService.addSysMenu(sysMenu);
                if (i > 0) {
                    result.setMsg("新增菜单成功");
                } else {
                    result.setMsg("新增菜单失败");
                }
            } else {
                result.setMsg("菜单信息为空，新增失败");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    @PutMapping
    public HttpResult updateSysMenu(SysMenu sysMenu){
        try {
            HttpResult result = new HttpResult();
            if (null != sysMenu) {
                int i = sysMenuService.updateSysMenu(sysMenu);
                if (i > 0) {
                    result.setMsg("修改菜单成功");
                } else {
                    result.setMsg("修改菜单失败");
                }
            } else {
                result.setMsg("菜单信息为空，修改失败");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    @DeleteMapping("/{ids}")
    public HttpResult deleteSysMenu(@PathVariable("ids") String ids){
        try {
            HttpResult result = new HttpResult();
            List<BigInteger> idss = new ArrayList<>();
            if (CommonUtil.isNotEmpty(ids)){
                String[] idList = ids.split(",");
                for (String id : idList) {
                    idss.add(new BigInteger(id));
                }
                int addUser = sysMenuService.deleteSysMenu(idss);
                if (addUser > 0) {
                    result.setMsg("删除菜单成功!");
                } else {
                    result.setMsg("删除菜单失败!");
                }
            } else {
                result.setMsg("不能删除空数据");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    /**
     * @Date 2021/6/25 16:47
     * @Description 新增 目录/菜单/按钮信息
     * @Params
     */
    @RequestMapping("addMenuOrButtonInfo")
    public HttpResult insertMenuOrButtonInfo(@RequestBody MenuAndButtonInfo menuAndButtonInfo){


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

        List<MenuAndButtonInfo> list = sysMenuService.getMenuInfoList();

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
