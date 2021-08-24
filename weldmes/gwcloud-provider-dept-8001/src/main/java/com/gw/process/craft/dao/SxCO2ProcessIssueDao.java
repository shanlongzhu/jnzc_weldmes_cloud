package com.gw.process.craft.dao;

import com.gw.entities.SxCO2ProcessIssue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    public List<SxCO2ProcessIssue> selectSxCO2ProcessIssueInfos(@Param("wpsLibraryId")Long wpsLibraryId);

    /**
     * @Date 2021/8/19 16:34
     * @Description 添加松下CO2工艺信息
     * @Params
     */
    public void insertSxCO2ProcessIssueInfo(@Param("sxCO2ProcessIssue")SxCO2ProcessIssue sxCO2ProcessIssue);

    /**
     * @Date 2021/8/19 15:43
     * @Description 根据id查询 松下CO2工艺信息
     * @Params
     */
    public SxCO2ProcessIssue selectSxCO2ProcessIssueInfoById(@Param("id") Long id);

    /**
     * @Date 2021/8/19 15:49
     * @Description 根据id 修改 松下CO2工艺信息
     * @Params
     */
    public void updateSxCO2ProcessIssueInfo(@Param("sxCO2ProcessIssue") SxCO2ProcessIssue sxCO2ProcessIssue);

    /**
     * @Date 2021/8/19 16:27
     * @Description  根据id删除 松下CO2工艺信息
     * @Params
     */
    public void deleteSxCO2ProcessIssueInfoById(@Param("id")Long id);
}
