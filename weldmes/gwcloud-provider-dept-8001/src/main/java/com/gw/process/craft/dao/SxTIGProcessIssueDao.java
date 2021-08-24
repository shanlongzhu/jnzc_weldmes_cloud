package com.gw.process.craft.dao;

import com.gw.entities.SxTIGProcessIssue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/19 11:05
 * @Description
 */
@Mapper
public interface SxTIGProcessIssueDao {

    /**
     * @Date 2021/8/19 13:13
     * @Description 查询松下TIG列表信息
     * @Params
     */
    public List<SxTIGProcessIssue> selectSxTIGProcessIssueInfos();

    /**
     * @Date 2021/8/23 15:39
     * @Description 松下TIG工艺信息新增
     * @Params
     */
    public void insertSxTIGProcessIssueInfo(@Param("sxTIGProcessIssue") SxTIGProcessIssue sxTIGProcessIssue);

    /**
     * @Date 2021/8/23 16:12
     * @Description 根据 id 查询 松下TIG工艺信息
     * @Params
     */
    public SxTIGProcessIssue selectSxTIGProcessIssueInfoById(@Param("id")Long id);

    /**
     * @Date 2021/8/23 16:25
     * @Description 修改 松下TIG工艺信息
     * @Params
     */
    public void updateSxTIGProcessIssueInfo(@Param("sxTIGProcessIssue")SxTIGProcessIssue sxTIGProcessIssue);

    /**
     * @Date 2021/8/23 16:54
     * @Description 删除 松下TIG工艺信息
     * @Params
     */
    public void deleteSxTIGProcessIssueInfo(@Param("id")Long id);


}
