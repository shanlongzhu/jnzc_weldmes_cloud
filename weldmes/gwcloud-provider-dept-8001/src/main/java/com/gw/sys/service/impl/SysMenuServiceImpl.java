package com.gw.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gw.common.CommonUtil;
import com.gw.common.PageInfo;
import com.gw.entities.SysMenu;
import com.gw.sys.dao.SysMenuDao;
import com.gw.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    SysMenuDao sysMenuDao;

    @Override
    public PageInfo<SysMenu> getSysMenuPage(int draw, int start, int length, SysMenu sysMenu) {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.like(CommonUtil.isNotEmpty(sysMenu.getName()), "name", sysMenu.getName());
        start = (start / length) + 1;//当前页码
        IPage<SysMenu> page = new Page<>(start, length);
        IPage<SysMenu> sysDictionaryIPage = sysMenuDao.selectPage(page, wrapper);
        PageInfo<SysMenu> pageInfo = new PageInfo<>();
        pageInfo.setDraw(draw);
        pageInfo.setData(sysDictionaryIPage.getRecords());//数据结果
        pageInfo.setRecordsTotal(sysDictionaryIPage.getTotal());//总数
        pageInfo.setRecordsFiltered(sysDictionaryIPage.getRecords().size());////过滤后的总记录数
        return pageInfo;
    }

    @Override
    public int addSysMenu(SysMenu sysMenu) {
        return sysMenuDao.insert(sysMenu);
    }

    @Override
    public int updateSysMenu(SysMenu sysMenu) {
        return sysMenuDao.updateById(sysMenu);
    }

    @Override
    public int deleteSysMenu(List<BigInteger> ids) {
        return sysMenuDao.deleteBatchIds(ids);
    }

    @Override
    public List<SysMenu> getSysMenuByRoleId(BigInteger roleId) {
        return sysMenuDao.getSysMenuByRoleId(roleId);
    }

    @Override
    public List<SysMenu> getSysMenuAll() {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        return sysMenuDao.selectList(wrapper);
    }
}
