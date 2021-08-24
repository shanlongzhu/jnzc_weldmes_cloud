package com.gw.process.craft.service.impl;

import com.gw.entities.SxTIGProcessIssue;
import com.gw.process.craft.dao.SxTIGProcessIssueDao;
import com.gw.process.craft.service.SxTIGProcessIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/19 11:04
 * @Description 松下TIG工艺业务实现层
 */
@Service
public class SxTIGProcessIssueServiceImpl implements SxTIGProcessIssueService {

    @Autowired
    SxTIGProcessIssueDao sxTIGProcessIssueDao;

    /**
     * @Date 2021/8/19 14:35
     * @Description 查询松下TIG列表信息
     * @Params
     */
    @Override
    public List<SxTIGProcessIssue> getSxTIGProcessIssueInfos(Long wpsLibraryId) {

        List<SxTIGProcessIssue> list = sxTIGProcessIssueDao.selectSxTIGProcessIssueInfos(wpsLibraryId);

        return list;
    }

    /**
     * @Date 2021/8/23 15:38
     * @Description 松下TIG工艺信息新增
     * @Params
     */
    @Override
    public void addSxTIGProcessIssueInfo(SxTIGProcessIssue sxTIGProcessIssue) {

        sxTIGProcessIssueDao.insertSxTIGProcessIssueInfo(sxTIGProcessIssue);

    }

    /**
     * @Date 2021/8/23 16:12
     * @Description 根据 id 查询 松下TIG工艺信息
     * @Params
     */
    @Override
    public SxTIGProcessIssue getSxTIGProcessIssueInfoById(Long id) {

        SxTIGProcessIssue sxTIGProcessIssue = sxTIGProcessIssueDao.selectSxTIGProcessIssueInfoById(id);

        return sxTIGProcessIssue;
    }

    /**
     * @Date 2021/8/23 16:24
     * @Description 修改 松下TIG工艺信息
     * @Params
     */
    @Override
    public void updateSxTIGProcessIssueInfo(SxTIGProcessIssue sxTIGProcessIssue) {

        sxTIGProcessIssueDao.updateSxTIGProcessIssueInfo(sxTIGProcessIssue);

    }

    /**
     * @Date 2021/8/23 16:54
     * @Description 删除 松下TIG工艺信息
     * @Params
     */
    @Override
    public void deleteSxTIGProcessIssueInfo(Long id) {

        sxTIGProcessIssueDao.deleteSxTIGProcessIssueInfo(id);

    }

}
