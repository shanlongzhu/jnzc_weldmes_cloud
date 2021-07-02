package com.gw.sys.controller;

import com.gw.common.CommonUtil;
import com.gw.common.DateTimeUtil;
import com.gw.common.HttpResult;
import com.gw.common.PageInfo;
import com.gw.entities.SysDictionary;
import com.gw.entities.SysRole;
import com.gw.sys.service.SysDictionaryService;
import com.gw.sys.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "sysDictionary")
@CrossOrigin
public class SysDictionaryController {

    @Autowired
    SysDictionaryService sysDictionaryService;

    @GetMapping
    public PageInfo<SysDictionary> getSysDictionaryPage(HttpServletRequest request, SysDictionary sysDictionary) {
        //DataTables参数
        String draw = request.getParameter("draw");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        int intDraw = draw == null ? 0 : Integer.parseInt(draw);
        int intStart = start == null ? 0 : Integer.parseInt(start);
        int intLength = length == null ? 10 : Integer.parseInt(length);
        //封装 DataTables 需要的数据
        return sysDictionaryService.getSysDictionaryPage(intDraw, intStart, intLength, sysDictionary);
    }

    @PostMapping
    public HttpResult addSysDictionary(SysDictionary sysDictionary){
        try {
            HttpResult result = new HttpResult();
            if (null != sysDictionary) {
                sysDictionary.setCreateBy("admin");
                sysDictionary.setCreateTime(DateTimeUtil.sdf.format(System.currentTimeMillis()));
                int i = sysDictionaryService.addSysDictionary(sysDictionary);
                if (i > 0) {
                    result.setMsg("新增字典成功");
                } else {
                    result.setMsg("新增字典失败");
                }
            } else {
                result.setMsg("字典信息为空，新增失败");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    @PutMapping
    public HttpResult updateSysDictionary(SysDictionary sysDictionary){
        try {
            HttpResult result = new HttpResult();
            if (null != sysDictionary) {
                int i = sysDictionaryService.updateSysDictionary(sysDictionary);
                if (i > 0) {
                    result.setMsg("修改字典成功");
                } else {
                    result.setMsg("修改字典失败");
                }
            } else {
                result.setMsg("字典信息为空，修改失败");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    @DeleteMapping("/{ids}")
    public HttpResult deleteSysDictionary(@PathVariable("ids") String ids){
        try {
            HttpResult result = new HttpResult();
            List<BigInteger> idss = new ArrayList<>();
            if (CommonUtil.isNotEmpty(ids)){
                String[] idList = ids.split(",");
                for (String id : idList) {
                    idss.add(new BigInteger(id));
                }
                int addUser = sysDictionaryService.deleteSysDictionary(idss);
                if (addUser > 0) {
                    result.setMsg("删除字典成功!");
                } else {
                    result.setMsg("删除字典失败!");
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
