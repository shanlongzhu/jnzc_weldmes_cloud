package com.gw.sys.dao;

import com.gw.entities.SysDictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  字典dao层
 * @Params
 */
@Mapper
public interface SysDictionaryDao{

    /**
     * @Date 2021/7/8 13:39
     * @Description  条件查询字典信息列表
     * @Params sysDictionary 字典信息
     */
    public List<SysDictionary> selectDictionaryInfos(@Param("type")String type,@Param("typeName")String typeName,
                                                     @Param("value")String value, @Param("valueName")String valueName);

    /**
     * @Date 2021/7/8 13:39
     * @Description  根据id查询字典信息
     * @Params id 字典id
     */
    public SysDictionary selectDictionaryInfoById(@Param("id") Long id);

    /**
     * @Date 2021/7/8 13:39
     * @Description  修改字典信息
     * @Params sysDictionary 字典信息
     */
    public void updateDictionaryInfo(@Param("sysDictionary") SysDictionary sysDictionary);

    /**
     * @Date 2021/7/8 13:39
     * @Description  根据id删除字典信息
     * @Params id 字典id
     */
    public void deleteDictionaryInfoById(@Param("id") Long id);

    /**
     * @Date 2021/7/8 13:39
     * @Description  新增字典信息
     * @Params sysDictionary 字典信息
     */
    public void insertDictionaryInfo(@Param("sysDictionary") SysDictionary sysDictionary);

}
