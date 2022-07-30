package com.gw.sys.service;

import com.gw.entities.SysDictionary;

import java.util.List;
import java.util.Set;

public interface SysDictionaryService {

    /**
     * @Date 2021/7/8 13:39
     * @Description  条件查询字典信息列表
     * @Params  sysDictionary 字典信息
     */
    public List<SysDictionary> getDictionaryInfos(String type,String typeName,String value,String valueName);

    /**
     * @Date 2021/7/8 13:39
     * @Description  根据id查询字典信息
     * @Params id 字典id
     */
    public SysDictionary getDictionaryInfoById(Long id);

    /**
     * @Date 2021/7/8 13:39
     * @Description  修改字典信息
     * @Params sysDictionary 字典信息
     */
    public void updateDictionaryInfo(SysDictionary sysDictionary);

    /**
     * @Date 2021/7/8 13:39
     * @Description  根据id删除字典信息
     * @Params id 字典id
     */
    public void delDictionaryInfoById(Long id);

    /**
     * @Date 2021/7/8 13:39
     * @Description  新增字典信息
     * @Params sysDictionary 字典信息
     */
    public void addDictionaryInfo(SysDictionary sysDictionary);

    /**
     * @Date 2021/7/8 13:39
     * @Description  获取字典类型信息
     * @Params
     */
    public Set<SysDictionary> getDictionaryTypeInfo();

    /**
     * @Date 2021/7/8 13:39
     * @Description  根据字典类型查询字典信息
     * @Params type 字典类型
     */
    public List<SysDictionary> getDictionaryInfoByType(String type);

}
