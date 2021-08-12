package com.shth.das.sys.weldmesdb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shth.das.pojo.db.WeldOnOffTime;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WeldOnOffTimeMapper extends BaseMapper<WeldOnOffTime> {

    /**
     * 根据采集编号查询最新的开机时间
     * @param weldOnOffTime 入参实体类
     * @return 返回实体类
     */
    WeldOnOffTime selectWeldByGatherNo(WeldOnOffTime weldOnOffTime);

    /**
     * 根据设备IP查询最新的开机时间
     * @param weldOnOffTime 入参实体类
     * @return 返回实体类
     */
    WeldOnOffTime selectWeldByWeldCid(WeldOnOffTime weldOnOffTime);

}
