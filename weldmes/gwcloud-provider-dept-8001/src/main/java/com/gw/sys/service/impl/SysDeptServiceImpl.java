package com.gw.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gw.common.CommonUtil;
import com.gw.common.PageInfo;
import com.gw.entities.SysDept;
import com.gw.sys.dao.SysDeptDao;
import com.gw.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    SysDeptDao sysDeptDao;

    @Override
    public PageInfo<SysDept> getSysDeptPage(int draw, int start, int length, SysDept sysDept) {
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        wrapper.like(CommonUtil.isNotEmpty(sysDept.getName()), "name", sysDept.getName());
        start = (start / length) + 1;//当前页码
        IPage<SysDept> page = new Page<>(start, length);
        IPage<SysDept> sysDeptIPage = sysDeptDao.selectPage(page, wrapper);
        PageInfo<SysDept> pageInfo = new PageInfo<>();
        pageInfo.setDraw(draw);
        pageInfo.setData(sysDeptIPage.getRecords());//数据结果
        pageInfo.setRecordsTotal(sysDeptIPage.getTotal());//总数
        pageInfo.setRecordsFiltered(sysDeptIPage.getRecords().size());////过滤后的总记录数
        return pageInfo;
    }

    @Override
    public int addSysDept(SysDept sysDept) {
        return sysDeptDao.insert(sysDept);
    }

    @Override
    public int updateSysDept(SysDept sysDept) {
        return sysDeptDao.updateById(sysDept);
    }

    @Override
    public int deleteSysDept(List<BigInteger> ids) {
        return sysDeptDao.deleteBatchIds(ids);
    }
}
