package com.gw.process.craft.service;

import com.gw.entities.SxAT3ProcessIssue;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/30 14:20
 * @Description 松下AT3工艺业务层
 */
public interface SxAT3ProcessIssueService {

    /**
     * @Date 2021/8/30 14:21
     * @Description 松下AT3工艺列表查询
     * @Params  工艺库id
     */
    public List<SxAT3ProcessIssue> getSxAT3ProcessIssueInfos(Long wpsLibraryId);

    /**
     * @Date 2021/8/30 14:44
     * @Description 松下AT3工艺信息新增
     * @Params
     */
    public void addSxAT3ProcessIssueInfo(SxAT3ProcessIssue sxAT3ProcessIssue);

    /**
     * @Date 2021/8/30 15:07
     * @Description 根据 id 查询 松下AT3工艺信息
     * @Params
     */
    public SxAT3ProcessIssue getSxAT3ProcessIssueInfoById(Long id);

    /**
     * @Date 2021/8/30 15:14
     * @Description 修改松下AT3工艺信息
     * @Params
     */
    public void updateSxAT3ProcessIssueInfo(SxAT3ProcessIssue sxAT3ProcessIssue);

    /**
     * @Date 2021/8/30 15:23
     * @Description 删除松下AT3工艺信息
     * @Params
     */
    public void deleteSxAT3ProcessIssueInfo(Long id);

    /**
     * @Date 2021/9/3 13:26
     * @Description 根据 工艺库id  查询  通道号
     * @Params
     */
    public List<Integer> getChannelNos(Long id);
}
