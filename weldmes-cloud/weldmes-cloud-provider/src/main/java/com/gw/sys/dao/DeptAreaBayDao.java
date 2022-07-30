package com.gw.sys.dao;

import com.gw.entities.DeptAreaBayInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeptAreaBayDao {

    /**
     * @Date 2021/8/26 16:44
     * @Description 班组区域跨间绑定
     * @Params
     */
    public void insertDeptTOAreaAndBay(@Param("deptId")Long deptId, @Param("areaId")Long areaId,
                                       @Param("bayIds")List<Long> bayIds,@Param("time")String time);

    /**
     * @Date 2021/8/26 17:21
     * @Description 删除班组区域跨间绑定
     * @Params
     */
    public void deleteDeptTOAreaAndBay(@Param("deptId")Long deptId);

    /**
     * @Date 2021/8/27 16:22
     * @Description 根据作业区id、区域id获取部门列表信息
     * @Params
     */
    public List<DeptAreaBayInfo> selectDeptAreaBayInfos(@Param("deptId")Long deptId, @Param("areaId")Long areaId);
}
