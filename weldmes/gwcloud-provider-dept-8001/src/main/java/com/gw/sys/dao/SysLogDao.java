package com.gw.sys.dao;

import com.gw.entities.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author zhanghan
 * @Date 2021/7/14 10:12
 * @Description  日志管理dao层
 * @Params
 */
@Mapper
public interface SysLogDao {

    /**
     * @Date 2021/7/14 12:51
     * @Description 新增日志信息
     * @Params
     */
    public void insertSysLogInfo(@Param("sysLog")SysLog sysLog);
}
