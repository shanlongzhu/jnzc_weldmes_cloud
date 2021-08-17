package com.gw.equipment.collection.dao;

import com.gw.entities.AreaBayInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/7/15 16:28
 * @Description  区域跨间dao层
 */
@Mapper
public interface AreaBayDao {

    /**
     * @Date 2021/7/15 16:19
     * @Description  绑定区域跨间
     * @Params areaBayInfo 区域跨间实体类
     */
    public void insertAreaBayInfo(@Param("areaBayInfo") AreaBayInfo areaBayInfo);

    /**
     * @Date 2021/8/10 14:50
     * @Description 根据区域id 删除区域跨间信息
     * @Params
     */
    public void deleteAreaBayInfoById(@Param("areaId") Long areaId);

    /**
     * @Date 2021/8/17 14:36
     * @Description 查询字典表中所有的区域信息
     * @Params
     */
    public List<AreaBayInfo> selectAreaInfos();

    /**
     * @Date 2021/8/17 15:12
     * @Description 根据areaId查询跨甲id列表
     * @Params
     */
    public List<AreaBayInfo> selectBayInfos(@Param("id") Long id);

}
