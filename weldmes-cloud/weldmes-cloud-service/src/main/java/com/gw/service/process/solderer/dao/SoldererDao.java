package com.gw.service.process.solderer.dao;

import com.gw.entities.WelderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SoldererDao {

    /**
     * @Date 2021/10/13 10:33
     * @Description 焊工信息列表查询
     * @Params
     */
    List<WelderInfo> getList(@Param("welderName") String welderName, @Param("welderNo")String welderNo, @Param("rate")Integer rate,
                             @Param("talent")Integer talent,@Param("grade")Integer grade);

    /**
     * @Date 2021/10/13 10:30
     * @Description 添加焊工信息
     * @Params
     */
    int addSolderer(@Param("welderInfo")WelderInfo welderInfo);

    /**
     * @Date 2021/10/13 10:31
     * @Description 根据id查询焊工信息
     * @Params
     */
    List<WelderInfo> getById(Long id);

    /**
     * @Date 2021/10/13 10:31
     * @Description 修改焊工信息
     * @Params
     */
    int updateSolderer(WelderInfo welderInfo);

    /**
     * @Date 2021/10/13 10:31
     * @Description 删除焊工信息
     * @Params
     */
    int deleteSolderer(Long id);

    /**
     * @Date 2021/10/13 10:31
     * @Description 通过级别信息 获取级别字典id
     * @Params
     */
    Byte getRankId(String rank);

    /**
     * @Date 2021/10/13 10:32
     * @Description 通过资质信息 获取资质字典id
     * @Params
     */
    Byte getCertificationId(String certification);

    /**
     * @Date 2021/10/13 10:32
     * @Description 通过部门信息 获取部门字典id
     * @Params
     */
    Long getDeptId(String deptName);

    /**
     * @Date 2021/10/13 10:33
     * @Description 添加焊工信息
     * @Params
     */
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

    /**
     * @Date 2021/10/11 17:46
     * @Description 根据焊工编号 查询 焊工信息
     * @Params
     */
    public Long selectWelderInfoByWelderNo(@Param("WelderNo") String WelderNo);

    /**
     * @Date 2021/10/13 10:30
     * @Description 焊工信息批量插入
     * @Params
     */
    public void insertWelderInfos(@Param("list")List<WelderInfo> list);
}
