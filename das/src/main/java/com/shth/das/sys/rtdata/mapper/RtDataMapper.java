package com.shth.das.sys.rtdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shth.das.pojo.JNRtDataDB;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface RtDataMapper extends BaseMapper<JNRtDataDB> {

    int createNewTable(@Param("tableName") String tableName);

    int insertRtDataList(Map<String,Object> map);

}
