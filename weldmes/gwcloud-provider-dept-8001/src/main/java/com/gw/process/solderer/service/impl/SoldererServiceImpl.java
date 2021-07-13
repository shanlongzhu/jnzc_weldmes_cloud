package com.gw.process.solderer.service.impl;

import com.gw.entities.MachineWeldInfo;
import com.gw.entities.WelderInfo;
import com.gw.process.solderer.dao.SoldererDao;
import com.gw.process.solderer.service.SoldererService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SoldererServiceImpl implements SoldererService {

    @Autowired
    private SoldererDao soldererDao;

    @Override
    public List<WelderInfo> getList(String welderName,String welderNo,Integer rate,
                                    Integer talent,Integer grade) {
        return soldererDao.getList(welderName,welderNo,rate,talent,grade);
    }

    @Override
    public int addSolderer(WelderInfo welderInfo) {
        return soldererDao.addSolderer(welderInfo);
    }

    @Override
    public List<WelderInfo> getById(Long id) {
        return soldererDao.getById(id);
    }

    @Override
    public int updateSolderer(WelderInfo welderInfo) {
        return soldererDao.updateSolderer(welderInfo);
    }

    @Override
    public int deleteSolderer(Long id) {
        return soldererDao.deleteSolderer(id);
    }

    @Override
    public Byte getRankId(String rank) {
        return soldererDao.getRankId(rank);
    }

    @Override
    public Byte getCertificationId(String certification) {
        return soldererDao.getCertificationId(certification);
    }

    @Override
    public Long getDeptId(String deptName) {
        return soldererDao.getDeptId(deptName);
    }

    @Override
    public void importExcel(List<WelderInfo> welderInfoArrayList) {
        for (WelderInfo welderInfo : welderInfoArrayList) {
            soldererDao.save(welderInfo);
        }
    }

    /**
     * @Date 2021/7/13 18:01
     * @Description 获取历史曲线中焊工id、姓名以及编号
     * @Params
     */
    @Override
    public Set<WelderInfo> getHistoryWelderInfos() {

        List<WelderInfo> welderInfos = soldererDao.selectHistoryWelderInfos();

        //去重
        Set<WelderInfo> set = new HashSet<>(welderInfos);


        return set;
    }

}
