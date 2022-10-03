package com.gw.service.process.craft.service.impl;

import com.gw.entities.SxAT3ProcessIssue;
import com.gw.service.process.craft.dao.SxAT3ProcessIssueDao;
import com.gw.service.process.craft.service.SxAT3ProcessIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SxAT3ProcessIssueServiceImpl implements SxAT3ProcessIssueService {

    @Autowired
    SxAT3ProcessIssueDao sxAT3ProcessIssueDao;

    /**
     * @Date 2021/8/30 14:21
     * @Description 松下AT3工艺列表查询
     * @Params  工艺库id
     */
    @Override
    public List<SxAT3ProcessIssue> getSxAT3ProcessIssueInfos(Long wpsLibraryId) {

        List<SxAT3ProcessIssue> list = sxAT3ProcessIssueDao.selectSxAT3ProcessIssueInfos(wpsLibraryId);

        return list;
    }

    /**
     * @Date 2021/8/30 14:45
     * @Description 松下AT3工艺信息新增
     * @Params
     */
    @Override
    public void addSxAT3ProcessIssueInfo(SxAT3ProcessIssue sxAT3ProcessIssue) {

        sxAT3ProcessIssueDao.insertSxAT3ProcessIssueInfo(sxAT3ProcessIssue);
    }

    /**
     * @Date 2021/8/30 15:08
     * @Description 根据 id 查询 松下AT3工艺信息
     * @Params
     */
    @Override
    public SxAT3ProcessIssue getSxAT3ProcessIssueInfoById(Long id) {

        SxAT3ProcessIssue sxAT3ProcessIssue = sxAT3ProcessIssueDao.selectSxAT3ProcessIssueById(id);

        return sxAT3ProcessIssue;
    }

    /**
     * @Date 2021/8/30 15:15
     * @Description 修改松下AT3工艺信息
     * @Params
     */
    @Override
    public void updateSxAT3ProcessIssueInfo(SxAT3ProcessIssue sxAT3ProcessIssue) {

        sxAT3ProcessIssueDao.updateSxAT3ProcessIssueInfo(sxAT3ProcessIssue);
    }

    /**
     * @Date 2021/8/30 15:25
     * @Description 删除松下AT3工艺信息
     * @Params
     */
    @Override
    public void deleteSxAT3ProcessIssueInfo(Long id) {

        sxAT3ProcessIssueDao.deleteSxAT3ProcessIssueInfo(id);
    }

    /**
     * @Date 2021/9/3 13:49
     * @Description 根据 工艺库id  查询  通道号
     * @Params
     */
    @Override
    public List<String> getChannelNos(Long id) {

        List<String> list = sxAT3ProcessIssueDao.queryChannelNos(id);

        return list;
    }

}
