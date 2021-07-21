package com.shth.das.sys.rtdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shth.das.pojo.jnsx.SxRtDataDb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface SxRtDataMapper extends BaseMapper<SxRtDataDb> {

    /**
     * 创建松下设备实时数据表
     * @param tableName 表名
     * @return 创建结果
     */
    int createNewTable(@Param("tableName") String tableName);

    /**
     * 批量存入松下实时数据
     * @param map 数据
     */
    int insertSxRtDataList(Map<String,Object> map);

}
