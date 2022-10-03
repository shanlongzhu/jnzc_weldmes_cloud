package com.gw.service.process.solderer.service.impl;

import com.gw.entities.WelderInfo;
import com.gw.service.process.solderer.dao.SoldererDao;
import com.gw.service.process.solderer.service.SoldererService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SoldererServiceImpl implements SoldererService {

    @Autowired
    private SoldererDao soldererDao;

    /**
     * @Date 2021/10/12 17:30
     * @Description 焊工列表查询
     * @Params
     */
    @Override
    public List<WelderInfo> getList(String welderName,String welderNo,Integer rate,
                                    Integer talent,Integer grade) {

        List<WelderInfo> list = soldererDao.getList(welderName,welderNo,rate,talent,grade);

        return list;
    }

    /**
     * @Date 2021/10/12 17:30
     * @Description 添加焊工信息
     * @Params
     */
    @Override
    public void addSolderer(WelderInfo welderInfo) {

        soldererDao.addSolderer(welderInfo);
    }

    /**
     * @Date 2021/10/13 10:27
     * @Description 根据id查询焊工信息
     * @Params
     */
    @Override
    public List<WelderInfo> getById(Long id) {
        return soldererDao.getById(id);
    }

    /**
     * @Date 2021/10/13 10:24
     * @Description 修改焊工信息
     * @Params
     */
    @Override
    public void updateSolderer(WelderInfo welderInfo) {

        soldererDao.updateSolderer(welderInfo);
    }

    /**
     * @Date 2021/10/13 10:24
     * @Description 删除焊工信息
     * @Params
     */
    @Override
    public void deleteSolderer(Long id) {

        soldererDao.deleteSolderer(id);
    }

    /**
     * @Date 2021/10/13 10:28
     * @Description 根据部门名称查询部门id
     * @Params
     */
    @Override
    public Long getDeptId(String deptName) {
        return soldererDao.getDeptId(deptName);
    }

    /**
     * @Date 2021/10/13 10:11
     * @Description 对焊工信息进行码值转换
     * @Params
     */
    @Override
    public WelderInfo importExcel(WelderInfo data) {

        //组织机构
        if (!ObjectUtils.isEmpty(data.getDeptName())) {

            Long deptId = soldererDao.getDeptId(data.getDeptName());

            if (!ObjectUtils.isEmpty(deptId)) {

                data.setDeptId(deptId);
            }
        }

        //级别
        if (!ObjectUtils.isEmpty(data.getRankStr())) {

            Byte rankId = soldererDao.getRankId(data.getRankStr());

            if (!ObjectUtils.isEmpty(rankId)) {

                data.setRank(rankId);
            }
        }

        //资质
        if (!ObjectUtils.isEmpty(data.getCertificationStr())) {

            Byte certificationId = soldererDao.getCertificationId(data.getCertificationStr());

            if (!ObjectUtils.isEmpty(certificationId)) {

                data.setCertification(certificationId);
            }
        }

        return data;
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

    /**
     * @Date 2021/10/13 10:30
     * @Description 焊工信息批量插入
     * @Params
     */
    @Override
    public void addWelderInfos(List<WelderInfo> list) {

        soldererDao.insertWelderInfos(list);

    }

}
