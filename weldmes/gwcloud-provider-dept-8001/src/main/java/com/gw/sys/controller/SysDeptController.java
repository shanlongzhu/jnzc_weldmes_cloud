package com.gw.sys.controller;

import com.gw.common.CommonUtil;
import com.gw.common.DateTimeUtil;
import com.gw.common.HttpResult;
import com.gw.common.PageInfo;
import com.gw.entities.SysDept;
import com.gw.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("sysDept")
public class SysDeptController {
    
    @Autowired
    SysDeptService sysDeptService;

    @GetMapping
    public PageInfo<SysDept> getSysDeptPage(HttpServletRequest request, SysDept sysDept) {
        //DataTables参数
        String draw = request.getParameter("draw");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        int intDraw = draw == null ? 0 : Integer.parseInt(draw);
        int intStart = start == null ? 0 : Integer.parseInt(start);
        int intLength = length == null ? 10 : Integer.parseInt(length);
        //封装 DataTables 需要的数据
        return sysDeptService.getSysDeptPage(intDraw, intStart, intLength, sysDept);
    }

    @PostMapping
    public HttpResult addSysDept(SysDept sysDept){
        try {
            HttpResult result = new HttpResult();
            if (null != sysDept) {
                sysDept.setCreateBy("admin");
                sysDept.setCreateTime(DateTimeUtil.sdf.format(System.currentTimeMillis()));
                int i = sysDeptService.addSysDept(sysDept);
                if (i > 0) {
                    result.setMsg("新增组织机构成功");
                } else {
                    result.setMsg("新增组织机构失败");
                }
            } else {
                result.setMsg("组织机构信息为空，新增失败");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    @PutMapping
    public HttpResult updateSysDept(SysDept sysDept){
        try {
            HttpResult result = new HttpResult();
            if (null != sysDept) {
                int i = sysDeptService.updateSysDept(sysDept);
                if (i > 0) {
                    result.setMsg("修改组织机构成功");
                } else {
                    result.setMsg("修改组织机构失败");
                }
            } else {
                result.setMsg("组织机构信息为空，修改失败");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    @DeleteMapping("/{ids}")
    public HttpResult deleteSysDept(@PathVariable("ids") String ids){
        try {
            HttpResult result = new HttpResult();
            List<BigInteger> idss = new ArrayList<>();
            if (CommonUtil.isNotEmpty(ids)){
                String[] idList = ids.split(",");
                for (String id : idList) {
                    idss.add(new BigInteger(id));
                }
                int addUser = sysDeptService.deleteSysDept(idss);
                if (addUser > 0) {
                    result.setMsg("删除组织机构成功!");
                } else {
                    result.setMsg("删除组织机构失败!");
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
