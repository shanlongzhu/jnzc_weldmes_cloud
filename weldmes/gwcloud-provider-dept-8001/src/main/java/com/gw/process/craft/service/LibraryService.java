package com.gw.process.craft.service;

import com.gw.entities.SysDictionary;
import com.gw.entities.WpsLibrary;

import java.util.List;

public interface LibraryService {
    List<WpsLibrary> getList();

    int addLibrary(WpsLibrary wpsLibrary);

    List<WpsLibrary> getById(Long id);

    int updateLibrary(WpsLibrary wpsLibrary);

    int deleteLibrary(Long id);

    /**
     * @Date 2021/7/1 14:17
     * @Description  查询关联厂家的设备信息
     * @Params  id 厂家字典表id
     */
    List<SysDictionary> getMachineInfoByFirmId(Long id);
}
