package com.shth.das.sys.weldmesdb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shth.das.pojo.db.WeldModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MachineWeldMapper extends BaseMapper<WeldModel> {

    /**
     * 查询所有OTC焊机信息不分页
     * @return 焊机集合
     */
    List<WeldModel> getMachineWeldAll();
}
