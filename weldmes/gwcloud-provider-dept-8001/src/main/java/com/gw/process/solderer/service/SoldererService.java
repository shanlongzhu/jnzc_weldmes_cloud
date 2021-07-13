package com.gw.process.solderer.service;

import com.gw.entities.WelderInfo;

import java.util.List;
import java.util.Set;

public interface SoldererService {
    List<WelderInfo> getList(String welderName,String welderNo,Integer rate,
                             Integer talent,Integer grade);

    int addSolderer(WelderInfo welderInfo);

    List<WelderInfo> getById(Long id);

    int updateSolderer(WelderInfo welderInfo);

    int deleteSolderer(Long id);


    Byte getRankId(String rank);

    Byte getCertificationId(String certification);

    Long getDeptId(String deptName);

    void importExcel(List<WelderInfo> welderInfoArrayList);

    /**
     * @Date 2021/7/13 18:01
     * @Description 获取历史曲线中焊工id、姓名以及编号
     * @Params
     */
    public Set<WelderInfo> getHistoryWelderInfos();
}
