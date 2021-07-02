package com.gw.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gw.common.CommonUtil;
import com.gw.common.PageInfo;
import com.gw.entities.SysLog;
import com.gw.sys.dao.SysLogDao;
import com.gw.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    SysLogDao sysLogDao;

    @Override
    public PageInfo<SysLog> getSysLogPage(int draw, int start, int length, SysLog sysLog) {
        QueryWrapper<SysLog> wrapper = new QueryWrapper<>();
        wrapper.like(CommonUtil.isNotEmpty(sysLog.getUserName()), "user_name", sysLog.getUserName())
                .between((CommonUtil.isNotEmpty(sysLog.getStarttime()) && CommonUtil.isNotEmpty(sysLog.getEndtime())),
                        "create_time",sysLog.getStarttime(),sysLog.getEndtime());
        start = (start / length) + 1;//当前页码
        IPage<SysLog> page = new Page<>(start, length);
        IPage<SysLog> sysLogIPage = sysLogDao.selectPage(page, wrapper);
        PageInfo<SysLog> pageInfo = new PageInfo<>();
        pageInfo.setDraw(draw);
        pageInfo.setData(sysLogIPage.getRecords());//数据结果
        pageInfo.setRecordsTotal(sysLogIPage.getTotal());//总数
        pageInfo.setRecordsFiltered(sysLogIPage.getRecords().size());////过滤后的总记录数
        return pageInfo;
    }

    @Override
    public int addSysLog(SysLog sysLog) {
        return sysLogDao.insert(sysLog);
    }

    @Override
    public int updateSysLog(SysLog sysLog) {
        return sysLogDao.updateById(sysLog);
    }

    @Override
    public int deleteSysLog(List<BigInteger> ids) {
        return sysLogDao.deleteBatchIds(ids);
    }
}
