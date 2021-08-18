package com.shth.das.sys.rtdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shth.das.pojo.jnotc.JNRtDataDB;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface OtcRtDataMapper extends BaseMapper<JNRtDataDB> {

    /**
     * 创建OTC实时数据表
     *
     * @param tableName 表名
     * @return 创建结果
     */
    int createNewTable(@Param("tableName") String tableName);

    /**
     * 批量存入OTC实时数据
     *
     * @param map 将数据封装到map
     * @return 返回新增结果
     */
    int insertRtDataList(Map<String, Object> map);

}
