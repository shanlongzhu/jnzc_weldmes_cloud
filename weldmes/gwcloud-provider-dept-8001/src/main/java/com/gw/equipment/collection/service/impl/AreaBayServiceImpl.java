package com.gw.equipment.collection.service.impl;

import com.gw.entities.AreaBayInfo;
import com.gw.equipment.collection.dao.AreaBayDao;
import com.gw.equipment.collection.service.AreaBayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/7/15 16:27
 * @Description 区域跨间业务实现层
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
    public void addAreaBayInfo(List<AreaBayInfo> areaBayInfos) {

        //删除原有的区域跨间信息
        areaBayDao.deleteAreaBayInfoById(areaBayInfos.get(0).getAreaId());

        //添加区域跨间信息
        for (AreaBayInfo areaBayInfo : areaBayInfos) {

            if(!ObjectUtils.isEmpty(areaBayInfo.getAreaId()) && !ObjectUtils.isEmpty(areaBayInfo.getBayId())){

                areaBayDao.insertAreaBayInfo(areaBayInfo);
            }
        }
    }

    /**
     * @Date 2021/8/17 11:17
     * @Description  查询区域跨间树状图
     * @Params
     */
    @Override
    public List<AreaBayInfo> getAreaBayTreeInfo() {

        //获取区域信息列表
        List<AreaBayInfo> areaInfos = areaBayDao.selectAreaInfos();

        for (AreaBayInfo areaInfo : areaInfos) {

            //根据区域id拿到跨间信息
            List<AreaBayInfo> list = areaBayDao.selectBayInfos(areaInfo.getAreaId());

            areaInfo.setList(list);

        }

        return areaInfos;
    }
}
