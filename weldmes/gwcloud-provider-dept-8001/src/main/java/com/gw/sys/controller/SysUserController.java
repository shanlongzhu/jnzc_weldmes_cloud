package com.gw.sys.controller;

import com.gw.common.*;
import com.gw.entities.SysUser;
import com.gw.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "user")
@CrossOrigin
public class SysUserController {

    @Autowired
    SysUserService sysUserService;

    @GetMapping
    public PageInfo<SysUser> getUser(HttpServletRequest request, SysUser sysUser) {
        //DataTables参数
        String draw = request.getParameter("draw");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        int intDraw = draw == null ? 0 : Integer.parseInt(draw);
        int intStart = start == null ? 0 : Integer.parseInt(start);
        int intLength = length == null ? 10 : Integer.parseInt(length);
        //封装 DataTables 需要的数据
        return sysUserService.UserPage(intDraw, intStart, intLength, sysUser);
    }

    @PostMapping
    public HttpResult addUser(SysUser sysUser) {
        try {
            HttpResult result = new HttpResult();
            if (sysUser != null) {
                sysUser.setCreateBy("admin");
                sysUser.setCreateTime(DateTimeUtil.sdf.format(System.currentTimeMillis()));
                sysUser.setStatus(sysUser.getUserStatus().equals("0") ? BigInteger.ZERO : BigInteger.ONE);
                int addUser = sysUserService.addUser(sysUser);
                if (addUser > 0) {
                    result.setMsg("新增用户成功!");
                } else {
                    result.setMsg("新增用户失败!");
                }
                return result;
            }
            return HttpResult.error("用户信息为空");
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    @PutMapping
    public HttpResult patchUser(SysUser sysUser){
        try {
            HttpResult result = new HttpResult();
            if (sysUser != null) {
                sysUser.setLastUpdateTime(DateTimeUtil.sdf.format(System.currentTimeMillis()));
                sysUser.setStatus(sysUser.getUserStatus().equals("0") ? BigInteger.ZERO : BigInteger.ONE);
                int addUser = sysUserService.updateUser(sysUser);
                if (addUser == 1) {
                    result.setMsg("修改用户成功!");
                } else {
                    result.setMsg("修改用户失败!");
                }
                return result;
            }
            return HttpResult.error("用户信息为空");
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    @DeleteMapping("/{ids}")
    public HttpResult deleteUser(@PathVariable("ids") String ids){
        try {
            HttpResult result = new HttpResult();
            List<BigInteger> idss = new ArrayList<>();
            if (CommonUtil.isNotEmpty(ids)){
                String[] idList = ids.split(",");
                for (String id : idList) {
                    idss.add(new BigInteger(id));
                }
                int addUser = sysUserService.deleteUser(idss);
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
