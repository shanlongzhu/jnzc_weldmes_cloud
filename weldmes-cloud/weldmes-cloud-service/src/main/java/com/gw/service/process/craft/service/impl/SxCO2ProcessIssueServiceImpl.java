package com.gw.service.process.craft.service.impl;

import com.gw.entities.SxCO2ProcessIssue;
import com.gw.service.process.craft.dao.SxCO2ProcessIssueDao;
import com.gw.service.process.craft.service.SxCO2ProcessIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SxCO2ProcessIssueServiceImpl implements SxCO2ProcessIssueService {

    @Autowired
    SxCO2ProcessIssueDao sxCO2ProcessIssueDao;

    /**
     * @Date 2021/8/18 14:42
     * @Description 查询列表信息
     * @Params
     */
    @Override
    public List<SxCO2ProcessIssue> getSxCO2ProcessIssueInfos(Long wpsLibraryId) {

        List<SxCO2ProcessIssue> list = sxCO2ProcessIssueDao.selectSxCO2ProcessIssueInfos(wpsLibraryId);

        return list;
    }

    /**
     * @Date 2021/8/19 16:33
     * @Description 添加松下CO2工艺信息
     * @Params
     */
    @Override
    public void addSxCO2ProcessIssueInfo(SxCO2ProcessIssue sxCO2ProcessIssue) {

        sxCO2ProcessIssueDao.insertSxCO2ProcessIssueInfo(sxCO2ProcessIssue);

    }

    /**
     * @Date 2021/8/19 15:42
     * @Description 根据id查询 松下CO2工艺信息
     * @Params
     */
    @Override
    public SxCO2ProcessIssue getSxCO2ProcessIssueInfoById(Long id) {

        SxCO2ProcessIssue sxCO2ProcessIssue = sxCO2ProcessIssueDao.selectSxCO2ProcessIssueInfoById(id);

        return sxCO2ProcessIssue;
    }

    /**
     * @Date 2021/8/19 15:49
     * @Description 根据id 修改 松下CO2工艺信息
     * @Params
     */
    @Override
    public void updateSxCO2ProcessIssueInfo(SxCO2ProcessIssue sxCO2ProcessIssue) {

        sxCO2ProcessIssueDao.updateSxCO2ProcessIssueInfo(sxCO2ProcessIssue);

    }

    /**
     * @Date 2021/8/19 16:27
     * @Description 根据id删除 松下CO2工艺信息
     * @Params
     */
    @Override
    public void deleteSxCO2ProcessIssueInfoById(Long id) {

        sxCO2ProcessIssueDao.deleteSxCO2ProcessIssueInfoById(id);

    }

    /**
     * @Date 2021/9/3 13:27
     * @Description 根据 工艺库id  查询  通道号
     * @Params
     */
    @Override
    public List<String> getChannelNos(Long id) {

        List<String> list = sxCO2ProcessIssueDao.queryChannelNos(id);

        return list;
    }

}
