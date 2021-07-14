package com.gw.sys.service;


import com.gw.entities.SysLog;

/**
 * @Author zhanghan
 * @Date 2021/7/14 10:12
 * @Description  日志业务层
 * @Params
 */
public interface SysLogService {

    /**
     * @Date 2021/7/14 12:43
     * @Description 日志信息入库
     * @Params sysLog 日志信息
     */
    public void addSysLogInfo(SysLog sysLog);

}
