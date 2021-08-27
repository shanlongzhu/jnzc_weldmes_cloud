package com.gw.sys.controller;

import com.gw.common.HttpResult;
import com.gw.entities.DeptAreaBayInfo;
import com.gw.sys.service.DeptAreaBayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/26 16:46
 * @Description
 */
@CrossOrigin
@RestController
public class DeptTOAreaAndBayController {

    @Autowired
    DeptAreaBayService deptAreaBayService;

    /**
     * @Date 2021/8/26 11:05
     * @Description 作业区区域跨间绑定
     * @Params
     */
    @RequestMapping(value = "deptTo/addDeptTOAreaAndBay",method = RequestMethod.POST)
    public HttpResult deptTOAreaAndBay(@RequestBody DeptAreaBayInfo deptAreaBayInfo){

        //删除原有的绑定信息
        deptAreaBayService.delDeptTOAreaAndBay(deptAreaBayInfo.getDeptId());

        //添加绑定信息
        deptAreaBayService.deptTOAreaAndBay(deptAreaBayInfo);

        return HttpResult.ok("绑定成功!");
    }

    /**
     * @Date 2021/8/27 16:15
     * @Description 根据作业区id、区域id获取部门列表信息
     * @Params
     */
    @RequestMapping(value = "deptTo/getDeptTOAreaAndBayInfo",method = RequestMethod.GET)
    public HttpResult getDeptTOAreaAndBayInfo(Long deptId,Long areaId){

        List<DeptAreaBayInfo> list = deptAreaBayService.getDeptTOAreaAndBay(deptId,areaId);

        return HttpResult.ok(list);
    }


}
