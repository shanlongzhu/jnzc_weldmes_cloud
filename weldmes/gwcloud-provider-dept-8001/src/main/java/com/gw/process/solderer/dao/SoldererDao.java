package com.gw.process.solderer.dao;

import com.gw.entities.WelderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SoldererDao {
    List<WelderInfo> getList(@Param("welderName") String welderName, @Param("welderNo")String welderNo, @Param("rate")Integer rate,
                             @Param("talent")Integer talent,@Param("grade")Integer grade);

    int addSolderer(@Param("welderInfo")WelderInfo welderInfo);

    List<WelderInfo> getById(Long id);

    int updateSolderer(WelderInfo welderInfo);

    int deleteSolderer(Long id);

    Byte getRankId(String rank);

    Byte getCertificationId(String certification);

    Long getDeptId(String deptName);

    void save(@Param("welderInfo")WelderInfo welderInfo);

    /**
     * @Date 2021/7/13 18:01
     * @Description 获取历史曲线中焊工id、姓名以及编号
     * @Params
     */
    public List<WelderInfo> selectHistoryWelderInfos();

    /**
     * @Date 2021/8/5 15:18
     * @Description 判断焊工编号是否存在
     * @Params
     */
    public Integer judgeWelderNoYesOrNo(@Param("WelderNo") String WelderNo,@Param("id")Long id);
}
