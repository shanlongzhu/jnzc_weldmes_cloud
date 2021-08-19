package com.gw.process.craft.dao;

import com.gw.entities.SxTIGProcessIssue;
import org.apache.ibatis.annotations.Mapper;

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


}
