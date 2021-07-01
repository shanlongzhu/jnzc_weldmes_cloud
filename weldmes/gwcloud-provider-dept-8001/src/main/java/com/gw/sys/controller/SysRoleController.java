package com.gw.sys.controller;

import com.gw.common.CommonUtil;
import com.gw.common.DateTimeUtil;
import com.gw.common.HttpResult;
import com.gw.common.PageInfo;
import com.gw.entities.MenuAndButtonInfo;
import com.gw.entities.SysRole;
import com.gw.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "sysRole")
@CrossOrigin
public class SysRoleController {

    @Autowired
    SysRoleService sysRoleService;



    /**
     * @Date 2021/6/25 16:47
     * @Description 新增菜单/按钮信息
     * @Params
     */
    @RequestMapping("addMenuOrButtonInfo")
    public HttpResult insertMenuOrButtonInfo(@RequestBody MenuAndButtonInfo menuAndButtonInfo){
        //
        return HttpResult.ok();
    }
    /**
     * 角色查询分页
     *
     * @param request
     * @param sysRole
     * @return
     */
    @GetMapping
    public PageInfo<SysRole> getSysDictionaryPage(HttpServletRequest request, SysRole sysRole) {
        //DataTables参数
        String draw = request.getParameter("draw");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        int intDraw = draw == null ? 0 : Integer.parseInt(draw);
        int intStart = start == null ? 0 : Integer.parseInt(start);
        int intLength = length == null ? 10 : Integer.parseInt(length);
        //封装 DataTables 需要的数据
        return sysRoleService.getSysRolePage(intDraw, intStart, intLength, sysRole);
    }

    @PostMapping
    public HttpResult addRole(SysRole sysRole) {
        try {
            HttpResult result = new HttpResult();
            if (null != sysRole) {
                sysRole.setCreateBy("admin");
                sysRole.setCreateTime(DateTimeUtil.sdf.format(System.currentTimeMillis()));
                int i = sysRoleService.addSysRole(sysRole);
                if (i > 0) {
                    result.setMsg("新增角色成功");
                } else {
                    result.setMsg("新增角色失败");
                }
            } else {
                result.setMsg("角色信息为空，新增失败");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    @PutMapping
    public HttpResult updateSysRole(SysRole sysRole){
        try {
            HttpResult result = new HttpResult();
            if (null != sysRole) {
                int i = sysRoleService.updateSysRole(sysRole);
                if (i > 0) {
                    result.setMsg("修改角色成功");
                } else {
                    result.setMsg("修改角色失败");
                }
            } else {
                result.setMsg("角色信息为空，修改失败");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    @DeleteMapping("/{ids}")
    public HttpResult deleteSysRole(@PathVariable("ids") String ids){
        try {
            HttpResult result = new HttpResult();
            List<BigInteger> idss = new ArrayList<>();
            if (CommonUtil.isNotEmpty(ids)){
                String[] idList = ids.split(",");
                for (String id : idList) {
                    idss.add(new BigInteger(id));
                }
                int addUser = sysRoleService.deleteSysRole(idss);
                if (addUser > 0) {
                    result.setMsg("删除用户成功!");
                } else {
                    result.setMsg("删除用户失败!");
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
