package com.gw.process.craft.dao;

import com.gw.entities.SysDictionary;
import com.gw.entities.WpsLibrary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LibraryDao {
    List<WpsLibrary> getList();

    int addLibrary(@Param("wpsLibrary")WpsLibrary wpsLibrary);

    List<WpsLibrary> getById(Long id);

    int updateLibrary(@Param("wpsLibrary")WpsLibrary wpsLibrary);

    int deleteLibrary(Long id);

    /**
     * @Date 2021/7/1 14:19
     * @Description  通过 厂家字典表id 查询 字典表设备id
     * @Params id  厂家字典表id
     */
    List<Long> queryMachineIdByFirmId(@Param("id") Long id);

    /**
     * @Date 2021/7/1 14:25
     * @Description   通过 字典表设备id 查询 设备描述 value_name
     * @Params
     */
    List<SysDictionary> queryMachineInfoByDictionaryId(@Param("ids")List<Long> ids);
}
