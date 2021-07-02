package com.gw.sys.controller;

import com.gw.common.CommonUtil;
import com.gw.common.DateTimeUtil;
import com.gw.common.HttpResult;
import com.gw.common.PageInfo;
import com.gw.entities.SysLog;
import com.gw.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("sysLog")
public class SysLogController {
    
    @Autowired
    SysLogService sysLogService;

    @GetMapping
    public PageInfo<SysLog> getSysLogPage(HttpServletRequest request, SysLog sysLog) {
        //DataTables参数
        String draw = request.getParameter("draw");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        int intDraw = draw == null ? 0 : Integer.parseInt(draw);
        int intStart = start == null ? 0 : Integer.parseInt(start);
        int intLength = length == null ? 10 : Integer.parseInt(length);
        //封装 DataTables 需要的数据
        return sysLogService.getSysLogPage(intDraw, intStart, intLength, sysLog);
    }

    @PostMapping
    public HttpResult addSysLog(SysLog sysLog){
        try {
            HttpResult result = new HttpResult();
            if (null != sysLog) {
                sysLog.setCreateBy("admin");
                sysLog.setCreateTime(DateTimeUtil.sdf.format(System.currentTimeMillis()));
                int i = sysLogService.addSysLog(sysLog);
                if (i > 0) {
                    result.setMsg("新增日志成功");
                } else {
                    result.setMsg("新增日志失败");
                }
            } else {
                result.setMsg("日志信息为空，新增失败");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    @PutMapping
    public HttpResult updateSysLog(SysLog sysLog){
        try {
            HttpResult result = new HttpResult();
            if (null != sysLog) {
                int i = sysLogService.updateSysLog(sysLog);
                if (i > 0) {
                    result.setMsg("修改日志成功");
                } else {
                    result.setMsg("修改日志失败");
                }
            } else {
                result.setMsg("日志信息为空，修改失败");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    @DeleteMapping("/{ids}")
    public HttpResult deleteSysLog(@PathVariable("ids") String ids){
        try {
            HttpResult result = new HttpResult();
            List<BigInteger> idss = new ArrayList<>();
            if (CommonUtil.isNotEmpty(ids)){
                String[] idList = ids.split(",");
                for (String id : idList) {
                    idss.add(new BigInteger(id));
                }
                int addUser = sysLogService.deleteSysLog(idss);
                if (addUser > 0) {
                    result.setMsg("删除日志成功!");
                } else {
                    result.setMsg("删除日志失败!");
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
