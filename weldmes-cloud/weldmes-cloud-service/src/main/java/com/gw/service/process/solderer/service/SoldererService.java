package com.gw.service.process.solderer.service;

import com.gw.entities.WelderInfo;

import java.util.List;
import java.util.Set;

public interface SoldererService {

    /**
     * @Date 2021/10/12 17:29
     * @Description 焊工列表查询
     * @Params
     */
    List<WelderInfo> getList(String welderName,String welderNo,Integer rate,
                             Integer talent,Integer grade);

    /**
     * @Date 2021/10/12 17:30
     * @Description 添加焊工信息
     * @Params
     */
    void addSolderer(WelderInfo welderInfo);

    /**
     * @Date 2021/10/13 10:27
     * @Description 根据id查询焊工信息
     * @Params
     */
    List<WelderInfo> getById(Long id);

    /**
     * @Date 2021/10/13 10:24
     * @Description 修改焊工信息
     * @Params
     */
    void updateSolderer(WelderInfo welderInfo);

    /**
     * @Date 2021/10/13 10:24
     * @Description 删除焊工信息
     * @Params
     */
    void deleteSolderer(Long id);

    /**
     * @Date 2021/10/13 10:28
     * @Description 根据部门名称查询部门id
     * @Params
     */
    Long getDeptId(String deptName);

    /**
     * @Date 2021/10/13 10:11
     * @Description 对焊工信息进行码值转换
     * @Params
     */
    WelderInfo importExcel(WelderInfo data);

    /**
     * @Date 2021/7/13 18:01
     * @Description 获取历史曲线中焊工id、姓名以及编号
     * @Params
     */
    public Set<WelderInfo> getHistoryWelderInfos();

    /**
     * @Date 2021/10/13 10:29
     * @Description 焊工信息批量插入
     * @Params
     */
    public void addWelderInfos(List<WelderInfo> list);
}
