package com.gw.sys.dao;

import com.gw.entities.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * @Date 2021/7/14 12:52
     * @Description 日志信息列表查询
     * @Params
     */
    public List<SysLog> selectSysLogInfos();

    /**
     * @Date 2021/7/14 12:52
     * @Description 根据id删除日志信息
     * @Params id  日志id
     */
    public void deleteSysLogInfoById(@Param("id")Long id);

}
