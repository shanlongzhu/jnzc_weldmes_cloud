package com.gw.sys.service.impl;

import com.gw.entities.SysLog;
import com.gw.sys.dao.SysLogDao;
import com.gw.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * @Date 2021/7/14 12:52
     * @Description 日志信息列表查询
     * @Params
     */
    @Override
    public List<SysLog> getSysLogInfos() {

        List<SysLog> list = sysLogDao.selectSysLogInfos();

        return list;
    }

    /**
     * @Date 2021/7/14 12:52
     * @Description 根据id删除日志信息
     * @Params id  日志id
     */
    @Override
    public void delSysLogInfoById(Long id) {

        sysLogDao.deleteSysLogInfoById(id);

    }
}
