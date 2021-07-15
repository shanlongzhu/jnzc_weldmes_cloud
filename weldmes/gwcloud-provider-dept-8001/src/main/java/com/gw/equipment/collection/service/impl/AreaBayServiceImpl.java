package com.gw.equipment.collection.service.impl;

import com.gw.entities.AreaBayInfo;
import com.gw.equipment.collection.dao.AreaBayDao;
import com.gw.equipment.collection.service.AreaBayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhanghan
 * @Date 2021/7/15 16:27
 * @Description
 */
@Service
public class AreaBayServiceImpl implements AreaBayService {

    @Autowired
    AreaBayDao areaBayDao;

    /**
     * @Date 2021/7/15 16:19
     * @Description  绑定区域跨间
     * @Params areaBayInfo 区域跨间实体类
     */
    @Override
    public void addAreaBayInfo(AreaBayInfo areaBayInfo) {

        areaBayDao.insertAreaBayInfo(areaBayInfo);
    }
}
