package com.gw.process.craft.dao;

import com.gw.entities.SxCO2ProcessIssue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/18 14:41
 * @Description
 */
@Mapper
public interface SxCO2ProcessIssueDao {

    /**
     * @Date 2021/8/19 9:56
     * @Description 松下co2列表查询
     * @Params
     */
    public List<SxCO2ProcessIssue> selectSxCO2ProcessIssueInfos();
}
