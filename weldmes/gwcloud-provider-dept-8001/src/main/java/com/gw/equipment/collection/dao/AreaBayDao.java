package com.gw.equipment.collection.dao;

import com.gw.entities.AreaBayInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
