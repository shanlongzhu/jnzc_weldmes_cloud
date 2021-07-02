package com.gw.sys.service;

import com.gw.common.PageInfo;
import com.gw.entities.SysLog;

import java.math.BigInteger;
import java.util.List;

public interface SysLogService {

    PageInfo<SysLog> getSysLogPage(int draw, int start, int length, SysLog sysLog);

    int addSysLog(SysLog sysLog);

    int updateSysLog(SysLog sysLog);

    int deleteSysLog(List<BigInteger> ids);
}
