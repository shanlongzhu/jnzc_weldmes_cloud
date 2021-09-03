package com.gw.process.craft.dao;

import com.gw.entities.SxAT3ProcessIssue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/30 14:23
 * @Description
 */
@Mapper
public interface SxAT3ProcessIssueDao {

    /**
     * @Date 2021/8/30 14:21
     * @Description 松下AT3工艺列表查询
     * @Params  工艺库id
     */
    public List<SxAT3ProcessIssue> selectSxAT3ProcessIssueInfos(@Param("wpsLibraryId")Long wpsLibraryId);

    /**
     * @Date 2021/8/30 14:45
     * @Description 松下AT3工艺信息新增
     * @Params
     */
    public void insertSxAT3ProcessIssueInfo(@Param("sxAT3ProcessIssue")SxAT3ProcessIssue sxAT3ProcessIssue);

    /**
     * @Date 2021/8/30 15:08
     * @Description 根据 id 查询 松下AT3工艺信息
     * @Params
     */
    public SxAT3ProcessIssue selectSxAT3ProcessIssueById(@Param("id")Long id);

    /**
     * @Date 2021/8/30 15:15
     * @Description 修改松下AT3工艺信息
     * @Params
     */
    public void updateSxAT3ProcessIssueInfo(@Param("sxAT3ProcessIssue")SxAT3ProcessIssue sxAT3ProcessIssue);

    /**
     * @Date 2021/8/30 15:26
     * @Description 删除松下AT3工艺信息
     * @Params
     */
    public void deleteSxAT3ProcessIssueInfo(@Param("id")Long id);

    /**
     * @Date 2021/9/3 13:27
     * @Description 根据 工艺库id  查询  通道号
     * @Params
     */
    List<String> queryChannelNos(@Param("id") Long id);

}
