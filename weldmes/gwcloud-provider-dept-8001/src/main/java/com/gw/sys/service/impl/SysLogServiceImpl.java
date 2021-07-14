package com.gw.sys.service.impl;

import com.gw.entities.SysLog;
import com.gw.sys.dao.SysLogDao;
import com.gw.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhanghan
 * @Date 2021/7/14 10:12
 * @Description  日志业务实现层
 * @Params
 */
@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    SysLogDao sysLogDao;

    /**
     * @Date 2021/7/14 12:43
     * @Description 日志信息入库
     * @Params
     */
    @Override
    public void addSysLogInfo(SysLog sysLog) {

        sysLogDao.insertSysLogInfo(sysLog);

    }
}
