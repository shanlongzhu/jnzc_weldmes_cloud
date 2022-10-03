package com.gw.service.process.craft.service;

import com.gw.entities.SxTIGProcessIssue;

import java.util.List;

public interface SxTIGProcessIssueService {

    /**
     * @Date 2021/8/19 14:33
     * @Description 查询松下TIG列表信息
     * @Params
     */
    public List<SxTIGProcessIssue> getSxTIGProcessIssueInfos(Long wpsLibraryId);

    /**
     * @Date 2021/8/23 15:38
     * @Description 松下TIG工艺信息新增
     * @Params
     */
    public void addSxTIGProcessIssueInfo(SxTIGProcessIssue sxTIGProcessIssue);

    /**
     * @Date 2021/8/23 16:11
     * @Description 根据 id 查询 松下TIG工艺信息
     * @Params
     */
    public SxTIGProcessIssue getSxTIGProcessIssueInfoById(Long id);

    /**
     * @Date 2021/8/23 16:23
     * @Description 修改 松下TIG工艺信息
     * @Params
     */
    public void updateSxTIGProcessIssueInfo(SxTIGProcessIssue sxTIGProcessIssue);

    /**
     * @Date 2021/8/23 16:52
     * @Description 删除 松下TIG工艺信息
     * @Params
     */
    public void deleteSxTIGProcessIssueInfo(Long id);

    /**
     * @Date 2021/9/3 13:26
     * @Description 根据 工艺库id  查询  通道号
     * @Params
     */
    public List<String> getChannelNos(Long id);
}
