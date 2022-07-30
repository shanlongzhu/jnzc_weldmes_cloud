package com.gw.sys.service.impl;

import com.gw.common.DateTimeUtils;
import com.gw.entities.DeptAreaBayInfo;
import com.gw.sys.dao.DeptAreaBayDao;
import com.gw.sys.service.DeptAreaBayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptAreaBayServiceImpl implements DeptAreaBayService {

    @Autowired
    DeptAreaBayDao deptAreaBayDao;

    /**
     * @Date 2021/8/26 16:43
     * @Description 班组区域跨间绑定
     * @Params
     */
    @Override
    public void deptTOAreaAndBay(DeptAreaBayInfo deptAreaBayInfo) {

        String time = DateTimeUtils.getNowDateTime();

        deptAreaBayDao.insertDeptTOAreaAndBay(deptAreaBayInfo.getDeptId(), deptAreaBayInfo.getAreaId(),
                deptAreaBayInfo.getBayIds(), time);

    }

    /**
     * @Date 2021/8/26 17:21
     * @Description 删除班组区域跨间绑定
     * @Params
     */
    @Override
    public void delDeptTOAreaAndBay(Long deptId) {

        deptAreaBayDao.deleteDeptTOAreaAndBay(deptId);
    }

    /**
     * @Date 2021/8/27 16:22
     * @Description 根据作业区id、区域id获取部门列表信息
     * @Params
     */
    @Override
    public List<DeptAreaBayInfo> getDeptTOAreaAndBay(Long deptId, Long areaId) {

        List<DeptAreaBayInfo> list = deptAreaBayDao.selectDeptAreaBayInfos(deptId, areaId);

        return list;
    }
}
