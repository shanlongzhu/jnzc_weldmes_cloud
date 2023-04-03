package com.shth.das.sys.rtdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shth.das.pojo.jnotc.JNRtDataDB;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface OtcRtDataMapper extends BaseMapper<JNRtDataDB> {

    /**
     * 查询表名是否存在
     *
     * @param tableName 表名
     * @return
     */
    Integer selectTableName(@Param("tableName") String tableName);

    /**
     * 创建OTC实时数据表
     *
     * @param tableName 表名
     * @return 创建结果
     */
    Integer createNewTable(@Param("tableName") String tableName);

    /**
     * 批量存入OTC实时数据
     *
     * @param map 将数据封装到map
     * @return 返回新增结果
     */
    Integer insertRtDataList(Map<String, Object> map);

}
