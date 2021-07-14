package com.gw.sys.controller;

import com.gw.common.HttpResult;
import com.gw.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Author zhanghan
 * @Date 2021/7/14 10:12
 * @Description  日志管理控制器
 * @Params
 */
@RestController
@CrossOrigin
public class SysLogController {

    @Autowired
    SysLogService sysLogService;

    /**
     * @Date 2021/7/14 12:52
     * @Description 新增日志信息
     * @Params
     */
    @RequestMapping("sysLog/addSysLogInfo")
    public HttpResult addSysLogInfo(){

        return HttpResult.ok();

    }



}
