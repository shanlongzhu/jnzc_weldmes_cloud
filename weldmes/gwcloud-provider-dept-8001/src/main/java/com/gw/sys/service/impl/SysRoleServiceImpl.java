package com.gw.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gw.common.CommonUtil;
import com.gw.common.PageInfo;
import com.gw.entities.SysRole;
import com.gw.sys.dao.SysRoleDao;
import com.gw.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    SysRoleDao sysRoleDao;

    @Override
    public PageInfo<SysRole> getSysRolePage(int draw, int start, int length, SysRole sysRole) {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.like(CommonUtil.isNotEmpty(sysRole.getName()),"name", sysRole.getName()); //为true，则拼接sql
        start = (start / length) + 1;//当前页码
        IPage<SysRole> page = new Page<>(start, length);
        IPage<SysRole> sysDictionaryIPage = sysRoleDao.selectPage(page, wrapper);
        PageInfo<SysRole> pageInfo = new PageInfo<>();
        pageInfo.setDraw(draw);
        pageInfo.setData(sysDictionaryIPage.getRecords());//数据结果
        pageInfo.setRecordsTotal(sysDictionaryIPage.getTotal());//总数
        pageInfo.setRecordsFiltered(sysDictionaryIPage.getRecords().size());////过滤后的总记录数
        return pageInfo;
    }

    @Override
    public int addSysRole(SysRole sysRole) {
        return sysRoleDao.insert(sysRole);
    }

    @Override
    public int updateSysRole(SysRole sysRole) {
        return sysRoleDao.updateById(sysRole);
    }

    @Override
    public int deleteSysRole(List<BigInteger> idss) {
        return sysRoleDao.deleteBatchIds(idss);
    }
}
