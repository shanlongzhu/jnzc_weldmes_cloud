package com.gw.process.craft.service.impl;

import com.gw.common.DateTimeUtil;
import com.gw.entities.SysDictionary;
import com.gw.entities.WpsLibrary;
import com.gw.process.craft.dao.LibraryDao;
import com.gw.process.craft.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryServiceImpl implements LibraryService {

    @Autowired
    private LibraryDao libraryDao;

    @Override
    public List<WpsLibrary> getList() {
        return libraryDao.getList();
    }

    @Override
    public int addLibrary(WpsLibrary wpsLibrary){

        //获取当前时间
        String createTime = DateTimeUtil.getCurrentTime();
        wpsLibrary.setCreateTime(createTime);
        return libraryDao.addLibrary(wpsLibrary);
    }

    @Override
    public List<WpsLibrary> getById(Long id) {
        return libraryDao.getById(id);
    }

    @Override
    public int updateLibrary(WpsLibrary wpsLibrary) {
        return libraryDao.updateLibrary(wpsLibrary);
    }

    @Override
    public int deleteLibrary(Long id) {
        return libraryDao.deleteLibrary(id);
    }

    /**
     * @Date 2021/7/1 14:37
     * @Description 查询关联厂家的设备信息
     * @Params id 字典表厂家 id
     */
    @Override
    public List<SysDictionary> getMachineInfoByFirmId(Long id) {

        //通过 厂家字典表id 查询 字典表设备id列表
        List<Long> ids = libraryDao.queryMachineIdByFirmId(id);

        //通过 字典表设备id 查询 设备描述 value_name
        List<SysDictionary> list = libraryDao.queryMachineInfoByDictionaryId(ids);

        return list;
    }

    /**
     * @Date 2021/7/1 14:17
     * @Description  查询关联区域的跨间信息
     * @Params  id 区域字典表id
     */
    @Override
    public List<SysDictionary> getBayInfoByAreaById(Long id) {

        //通过 区域字典表id 查询 字典表跨间id列表
        List<Long> ids = libraryDao.queryBayIdByAreaId(id);

        //通过 字典表区域id 查询 跨间描述 value_name
        List<SysDictionary> list = new ArrayList<>();

        if(!ObjectUtils.isEmpty(ids)){

            list = libraryDao.queryMachineInfoByDictionaryId(ids);
        }

        return list;
    }
}
