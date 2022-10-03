package com.gw.service.sys.service;

import com.gw.entities.DeptAreaBayInfo;

import java.util.List;

public interface DeptAreaBayService {

    /**
     * @Date 2021/8/26 16:43
     * @Description 班组区域跨间绑定
     * @Params
     */
    public void deptTOAreaAndBay(DeptAreaBayInfo deptAreaBayInfo);

    /**
     * @Date 2021/8/26 17:19
     * @Description 删除班组区域跨间绑定
     * @Params
     */
    public void delDeptTOAreaAndBay(Long deptId);

    /**
     * @Date 2021/8/27 16:20
     * @Description 根据作业区id、区域id获取部门列表信息
     * @Params
     */
    public List<DeptAreaBayInfo> getDeptTOAreaAndBay(Long deptId,Long areaId);
}
