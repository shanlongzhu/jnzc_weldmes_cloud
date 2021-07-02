package com.gw.sys.service;

import com.gw.common.PageInfo;
import com.gw.entities.SysDept;
import java.math.BigInteger;
import java.util.List;

public interface SysDeptService {

    PageInfo<SysDept> getSysDeptPage(int draw, int start, int length, SysDept sysDept);

    int addSysDept(SysDept sysDept);

    int updateSysDept(SysDept sysDept);

    int deleteSysDept(List<BigInteger> ids);
}
