package com.gw.sys.service;

import com.gw.entities.DeptAreaBayInfo;

/**
 * @Author zhanghan
 * @Date 2021/8/26 16:47
 * @Description
 */
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
}
