package com.gw.sys.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.SysLog;
import com.gw.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
     * @Description 日志信息列表查询
     * @Params
     */
    @RequestMapping(value = "sysLog/getSysLogInfos",method = RequestMethod.GET)
    public HttpResult getSysLogInfo(@RequestParam(value="pn",defaultValue = "1") Integer pn){

        PageHelper.startPage(pn,10);

        List<SysLog> list = sysLogService.getSysLogInfos();

        PageInfo<SysLog> page = new PageInfo(list,10);

        return HttpResult.ok(page);

    }

    /**
     * @Date 2021/7/14 12:52
     * @Description 根据id删除日志信息
     * @Params id  日志id
     */
    @RequestMapping(value = "sysLog/delSysLogInfoById",method = RequestMethod.DELETE)
    public HttpResult delSysLogInfoById(Long id){

        sysLogService.delSysLogInfoById(id);

        return HttpResult.ok("日志删除成功!");

    }




}
