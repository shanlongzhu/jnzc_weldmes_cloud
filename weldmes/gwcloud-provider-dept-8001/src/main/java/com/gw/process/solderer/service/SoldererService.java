package com.gw.process.solderer.service;

import com.gw.entities.WelderInfo;

import java.util.List;
import java.util.Set;

/**
 * @Date 2021/10/12 17:29
 * @Description 焊工业务层
 * @Params
 */
public interface SoldererService {

    /**
     * @Date 2021/10/12 17:29
     * @Description 焊工列表查询
     * @Params
     */
    List<WelderInfo> getList(String welderName,String welderNo,Integer rate,
                             Integer talent,Integer grade);

    /**
     * @Date 2021/10/12 17:30
     * @Description 添加焊工信息
     * @Params
     */
    void addSolderer(WelderInfo welderInfo);

    List<WelderInfo> getById(Long id);

    void updateSolderer(WelderInfo welderInfo);

    void deleteSolderer(Long id);


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
