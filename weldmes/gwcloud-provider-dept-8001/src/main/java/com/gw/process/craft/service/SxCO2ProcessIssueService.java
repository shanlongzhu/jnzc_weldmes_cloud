package com.gw.process.craft.service;

import com.gw.entities.SxCO2ProcessIssue;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/18 10:16
 * @Description 松下CO2工艺业务层
 */
public interface SxCO2ProcessIssueService {

    /**
     * @Date 2021/8/18 10:52
     * @Description 查询列表信息
     * @Params
     */
    public List<SxCO2ProcessIssue> getSxCO2ProcessIssueInfos();
}
