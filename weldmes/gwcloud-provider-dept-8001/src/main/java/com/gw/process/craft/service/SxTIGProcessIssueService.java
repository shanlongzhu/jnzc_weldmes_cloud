package com.gw.process.craft.service;

import com.gw.entities.SxTIGProcessIssue;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/19 11:03
 * @Description 松下TIG工艺业务层
 */
public interface SxTIGProcessIssueService {

    /**
     * @Date 2021/8/19 14:33
     * @Description 查询松下TIG列表信息
     * @Params
     */
    public List<SxTIGProcessIssue> getSxTIGProcessIssueInfos();
}
