package com.shth.das.sys.weldmesdb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shth.das.pojo.WeldModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MachineWeldMapper extends BaseMapper<WeldModel> {

    List<WeldModel> getMachineWeldAll();
}
