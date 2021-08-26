package com.gw.sys.service.impl;

import com.gw.common.DateTimeUtil;
import com.gw.entities.DeptAreaBayInfo;
import com.gw.sys.dao.DeptAreaBayDao;
import com.gw.sys.service.DeptAreaBayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhanghan
 * @Date 2021/8/26 16:48
 * @Description
 */
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

        String time = DateTimeUtil.getCurrentTime();

        deptAreaBayDao.insertDeptTOAreaAndBay(deptAreaBayInfo.getDeptId(),deptAreaBayInfo.getAreaId(),
                deptAreaBayInfo.getBayIds(),time);

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
}
