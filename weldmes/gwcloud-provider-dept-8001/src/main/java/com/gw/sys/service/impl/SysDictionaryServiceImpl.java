package com.gw.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gw.common.CommonUtil;
import com.gw.common.PageInfo;
import com.gw.entities.SysDictionary;
import com.gw.sys.dao.SysDictionaryDao;
import com.gw.sys.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class SysDictionaryServiceImpl implements SysDictionaryService {


    @Autowired
    SysDictionaryDao sysDictionaryDao;

    @Override
    public PageInfo<SysDictionary> getSysDictionaryPage(int draw, int start, int length, SysDictionary sysDictionary) {
        QueryWrapper<SysDictionary> wrapper = new QueryWrapper<>();
        wrapper.like(CommonUtil.isNotEmpty(sysDictionary.getType()), "type", sysDictionary.getType()).
                or().like(CommonUtil.isNotEmpty(sysDictionary.getType()), "value", sysDictionary.getType())
                .or().like(CommonUtil.isNotEmpty(sysDictionary.getType()), "label", sysDictionary.getType());
        start = (start / length) + 1;//当前页码
        IPage<SysDictionary> page = new Page<>(start, length);
        IPage<SysDictionary> sysDictionaryIPage = sysDictionaryDao.selectPage(page, wrapper);
        PageInfo<SysDictionary> pageInfo = new PageInfo<>();
        pageInfo.setDraw(draw);
        pageInfo.setData(sysDictionaryIPage.getRecords());//数据结果
        pageInfo.setRecordsTotal(sysDictionaryIPage.getTotal());//总数
        pageInfo.setRecordsFiltered(sysDictionaryIPage.getRecords().size());////过滤后的总记录数
        return pageInfo;
    }

    @Override
    public int addSysDictionary(SysDictionary sysDictionary) {
        return sysDictionaryDao.insert(sysDictionary);
    }

    @Override
    public int updateSysDictionary(SysDictionary sysDictionary) {
        return sysDictionaryDao.updateById(sysDictionary);
    }

    @Override
    public int deleteSysDictionary(List<BigInteger> ids) {
        return sysDictionaryDao.deleteBatchIds(ids);
    }
}
