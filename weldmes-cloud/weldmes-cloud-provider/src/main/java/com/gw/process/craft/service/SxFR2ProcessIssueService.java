package com.gw.process.craft.service;

import com.gw.entities.SxFR2ProcessIssue;

import java.util.List;

public interface SxFR2ProcessIssueService {

    /**
     * @Date 2021/8/31 13:41
     * @Description 松下FR2工艺信息列表查询
     * @Params
     */
    public List<SxFR2ProcessIssue> getSxFR2ProcessIssueInfos(Long wpsLibraryId);

    /**
     * @Date 2021/8/31 14:38
     * @Description 根据id查询松下FR2工艺信息
     * @Params
     */
    public SxFR2ProcessIssue getSxFR2ProcessIssueInfoById(Long id);

    /**
     * @Date 2021/8/31 14:41
     * @Description 新增松下FR2工艺信息
     * @Params
     */
    public void addSxFR2ProcessIssueInfo(SxFR2ProcessIssue sxFR2ProcessIssue);

    /**
     * @Date 2021/8/31 14:49
     * @Description 修改松下FR2工艺信息
     * @Params
     */
    public void alterSxFR2ProcessIssueInfo(SxFR2ProcessIssue sxFR2ProcessIssue);

    /**
     * @Date 2021/8/31 15:07
     * @Description 删除松下FR2工艺信息
     * @Params
     */
    public void delSxFR2ProcessIssueInfo(Long id);

    /**
     * @Date 2021/9/3 13:26
     * @Description 根据 工艺库id  查询  通道号
     * @Params
     */
    public List<String> getChannelNos(Long id);
}
