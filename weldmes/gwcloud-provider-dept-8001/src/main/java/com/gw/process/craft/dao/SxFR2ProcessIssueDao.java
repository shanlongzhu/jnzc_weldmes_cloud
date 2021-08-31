package com.gw.process.craft.dao;

import com.gw.entities.SxFR2ProcessIssue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/31 13:43
 * @Description
 */
@Mapper
public interface SxFR2ProcessIssueDao {

    /**
     * @Date 2021/8/31 14:04
     * @Description 松下FR2工艺信息列表查询
     * @Params
     */
    public List<SxFR2ProcessIssue> selectSxFR2ProcessIssueInfos(@Param("wpsLibraryId") Long wpsLibraryId);

    /**
     * @Date 2021/8/31 14:16
     * @Description 根据id查询松下FR2工艺信息
     * @Params
     */
    public SxFR2ProcessIssue selectSxFR2ProcessIssueInfoById(@Param("id") Long id);

    /**
     * @Date 2021/8/31 14:18
     * @Description 新增松下FR2工艺信息
     * @Params
     */
    public void insertSxFR2ProcessIssueInfo(@Param("sxFR2ProcessIssue")SxFR2ProcessIssue sxFR2ProcessIssue);

    /**
     * @Date 2021/8/31 14:50
     * @Description  修改松下FR2工艺信息
     * @Params
     */
    public void updateSxFR2ProcessIssueInfo(@Param("sxFR2ProcessIssue")SxFR2ProcessIssue sxFR2ProcessIssue);

    /**
     * @Date 2021/8/31 15:08
     * @Description 删除松下FR2工艺信息
     * @Params
     */
    public void deleteSxFR2ProcessIssueInfo(@Param("id")Long id);

}
