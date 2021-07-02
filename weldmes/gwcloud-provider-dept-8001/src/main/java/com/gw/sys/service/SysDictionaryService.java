package com.gw.sys.service;

import com.gw.common.PageInfo;
import com.gw.entities.SysDictionary;

import java.math.BigInteger;
import java.util.List;

public interface SysDictionaryService {

    /**
     * 查询字典分页
     * @param draw
     * @param start
     * @param length
     * @param sysDictionary
     * @return
     */
    PageInfo<SysDictionary> getSysDictionaryPage(int draw, int start, int length, SysDictionary sysDictionary);

    int addSysDictionary(SysDictionary sysDictionary);

    int updateSysDictionary(SysDictionary sysDictionary);

    int deleteSysDictionary(List<BigInteger> ids);
}
