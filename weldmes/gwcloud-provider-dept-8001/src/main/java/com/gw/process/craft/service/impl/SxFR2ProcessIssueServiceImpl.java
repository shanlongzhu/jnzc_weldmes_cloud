package com.gw.process.craft.service.impl;

import com.gw.entities.SxFR2ProcessIssue;
import com.gw.process.craft.dao.SxFR2ProcessIssueDao;
import com.gw.process.craft.service.SxFR2ProcessIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/31 13:39
 * @Description 松下FR2工艺业务实现层
 */
@Service
public class SxFR2ProcessIssueServiceImpl implements SxFR2ProcessIssueService {

    @Autowired
    SxFR2ProcessIssueDao sxFR2ProcessIssueDao;

    /**
     * @Date 2021/8/31 13:42
     * @Description 松下FR2工艺信息列表查询
     * @Params
     */
    @Override
    public List<SxFR2ProcessIssue> getSxFR2ProcessIssueInfos(Long wpsLibraryId) {

        List<SxFR2ProcessIssue> list = sxFR2ProcessIssueDao.selectSxFR2ProcessIssueInfos(wpsLibraryId);

        return list;
    }

    /**
     * @Date 2021/8/31 14:39
     * @Description 根据id查询松下FR2工艺信息
     * @Params
     */
    @Override
    public SxFR2ProcessIssue getSxFR2ProcessIssueInfoById(Long id) {

        SxFR2ProcessIssue sxFR2ProcessIssue = sxFR2ProcessIssueDao.selectSxFR2ProcessIssueInfoById(id);

        return sxFR2ProcessIssue;
    }

    /**
     * @Date 2021/8/31 14:42
     * @Description 新增松下FR2工艺信息
     * @Params
     */
    @Override
    public void addSxFR2ProcessIssueInfo(SxFR2ProcessIssue sxFR2ProcessIssue) {

        sxFR2ProcessIssueDao.insertSxFR2ProcessIssueInfo(sxFR2ProcessIssue);

    }

    /**
     * @Date 2021/8/31 14:50
     * @Description 修改松下FR2工艺信息
     * @Params
     */
    @Override
    public void alterSxFR2ProcessIssueInfo(SxFR2ProcessIssue sxFR2ProcessIssue) {

        sxFR2ProcessIssueDao.updateSxFR2ProcessIssueInfo(sxFR2ProcessIssue);
    }

    /**
     * @Date 2021/8/31 15:08
     * @Description 删除松下FR2工艺信息
     * @Params
     */
    @Override
    public void delSxFR2ProcessIssueInfo(Long id) {

        sxFR2ProcessIssueDao.deleteSxFR2ProcessIssueInfo(id);

    }

    @Override
    public List<String> getChannelNos(Long id) {

        List<String> list = sxFR2ProcessIssueDao.queryChannelNos(id);

        return list;
    }

}
