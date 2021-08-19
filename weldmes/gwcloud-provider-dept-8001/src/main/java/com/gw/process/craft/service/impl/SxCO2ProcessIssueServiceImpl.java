package com.gw.process.craft.service.impl;

import com.gw.entities.SxCO2ProcessIssue;
import com.gw.process.craft.dao.SxCO2ProcessIssueDao;
import com.gw.process.craft.service.SxCO2ProcessIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/18 10:16
 * @Description 松下CO2工艺业务实现层
 */
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
    public List<SxCO2ProcessIssue> getSxCO2ProcessIssueInfos() {

        List<SxCO2ProcessIssue> list = sxCO2ProcessIssueDao.selectSxCO2ProcessIssueInfos();

        return list;
    }
}
