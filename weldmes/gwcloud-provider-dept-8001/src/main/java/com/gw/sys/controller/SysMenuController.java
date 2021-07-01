package com.gw.sys.controller;

import com.gw.common.CommonUtil;
import com.gw.common.DateTimeUtil;
import com.gw.common.HttpResult;
import com.gw.common.PageInfo;
import com.gw.entities.SysMenu;
import com.gw.entities.SysMenu;
import com.gw.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

}