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
    public List<SxTIGProcessIssue> getSxTIGProcessIssueInfos() {

        List<SxTIGProcessIssue> list = sxTIGProcessIssueDao.selectSxTIGProcessIssueInfos();

        return list;
    }
}
