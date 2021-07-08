package com.gw.sys.service.impl;

import com.gw.entities.SysDictionary;
import com.gw.sys.dao.SysDictionaryDao;
import com.gw.sys.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  字典业务实现层
 * @Params
 */
@Service
public class SysDictionaryServiceImpl implements SysDictionaryService {


    @Autowired
    SysDictionaryDao sysDictionaryDao;

    /**
     * @Date 2021/7/8 13:39
     * @Description  条件查询字典信息列表
     * @Params sysDictionary 字典信息
     */
    @Override
    public List<SysDictionary> getDictionaryInfos(String type,String typeName,String value,String valueName) {

        List<SysDictionary> list = sysDictionaryDao.selectDictionaryInfos(type,typeName,value,valueName);

        return list;
    }

    /**
     * @Date 2021/7/8 13:39
     * @Description  根据id查询字典信息
     * @Params id 字典id
     */
    @Override
    public SysDictionary getDictionaryInfoById(Long id) {

        SysDictionary sysDictionary = sysDictionaryDao.selectDictionaryInfoById(id);

        return sysDictionary;
    }

    /**
     * @Date 2021/7/8 13:39
     * @Description  修改字典信息
     * @Params sysDictionary 字典信息
     */
    @Override
    public void updateDictionaryInfo(SysDictionary sysDictionary) {

        sysDictionaryDao.updateDictionaryInfo(sysDictionary);

    }

    /**
     * @Date 2021/7/8 13:39
     * @Description  根据id删除字典信息
     * @Params id 字典id
     */
    @Override
    public void delDictionaryInfoById(Long id) {

        sysDictionaryDao.deleteDictionaryInfoById(id);
    }

    /**
     * @Date 2021/7/8 13:39
     * @Description  新增字典信息
     * @Params sysDictionary 字典信息
     */
    @Override
    public void addDictionaryInfo(SysDictionary sysDictionary) {

        sysDictionaryDao.insertDictionaryInfo(sysDictionary);

    }
}
