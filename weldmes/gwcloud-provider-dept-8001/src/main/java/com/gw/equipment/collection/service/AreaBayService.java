package com.gw.equipment.collection.service;

import com.gw.entities.AreaBayInfo;

/**
 * @Author zhanghan
 * @Date 2021/7/15 16:27
 * @Description  区域跨间业务层
 */
public interface AreaBayService {

    /**
     * @Date 2021/7/15 16:19
     * @Description  绑定区域跨间
     * @Params areaBayInfo 区域跨间实体类
     */
    public void addAreaBayInfo(AreaBayInfo areaBayInfo);
}
