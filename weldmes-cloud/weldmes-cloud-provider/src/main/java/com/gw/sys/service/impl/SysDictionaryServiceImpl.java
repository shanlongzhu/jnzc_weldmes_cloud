package com.gw.sys.service.impl;

import com.gw.entities.SysDictionary;
import com.gw.sys.dao.SysDictionaryDao;
import com.gw.sys.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /**
     * @Date 2021/7/8 13:39
     * @Description  获取字典类型信息
     * @Params
     */
    @Override
    public Set<SysDictionary> getDictionaryTypeInfo() {

        List<SysDictionary> types = sysDictionaryDao.selectDictionaryTypeInfo();

        //去重
        Set<SysDictionary> set = new HashSet<>(types);

        return set;
    }

    /**
     * @Date 2021/7/8 13:39
     * @Description  根据字典类型查询字典信息
     * @Params type 字典类型
     */
    @Override
    public List<SysDictionary> getDictionaryInfoByType(String type) {

        List<SysDictionary> types = sysDictionaryDao.selectDictionaryInfoByType(type);

        return types;
    }
}
