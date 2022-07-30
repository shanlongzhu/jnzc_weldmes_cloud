package com.gw.sys.service;


import com.gw.entities.SysLog;

import java.util.List;

public interface SysLogService {

    /**
     * @Date 2021/7/14 12:43
     * @Description 日志信息入库
     * @Params sysLog 日志信息
     */
    public void addSysLogInfo(SysLog sysLog);

    /**
     * @Date 2021/7/14 12:52
     * @Description 日志信息列表查询
     * @Params
     */
    public List<SysLog>  getSysLogInfos();

    /**
     * @Date 2021/7/14 12:52
     * @Description 根据id删除日志信息
     * @Params id  日志id
     */
    public void delSysLogInfoById(Long id);

}
