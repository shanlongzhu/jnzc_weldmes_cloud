package com.gw.sys.dao;

import com.gw.entities.DeptAreaBayInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/26 16:49
 * @Description
 */
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
}
