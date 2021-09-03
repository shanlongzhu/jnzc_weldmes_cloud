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
    public List<SxCO2ProcessIssue> getSxCO2ProcessIssueInfos(Long wpsLibraryId);

    /**
     * @Date 2021/8/19 16:33
     * @Description 添加松下CO2工艺信息
     * @Params
     */
    public void addSxCO2ProcessIssueInfo(SxCO2ProcessIssue sxCO2ProcessIssue);

    /**
     * @Date 2021/8/19 15:41
     * @Description 根据id查询 松下CO2工艺信息
     * @Params
     */
    public SxCO2ProcessIssue getSxCO2ProcessIssueInfoById(Long id);

    /**
     * @Date 2021/8/19 15:49
     * @Description 根据id 修改 松下CO2工艺信息
     * @Params
     */
    public void updateSxCO2ProcessIssueInfo(SxCO2ProcessIssue sxCO2ProcessIssue);

    /**
     * @Date 2021/8/19 16:26
     * @Description 根据id删除 松下CO2工艺信息
     * @Params
     */
    public void deleteSxCO2ProcessIssueInfoById(Long id);

    /**
     * @Date 2021/9/3 13:26
     * @Description 根据 工艺库id  查询  通道号
     * @Params
     */
    public List<Integer> getChannelNos(Long id);
}
