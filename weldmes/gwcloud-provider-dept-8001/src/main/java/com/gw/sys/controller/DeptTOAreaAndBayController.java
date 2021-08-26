package com.gw.sys.controller;

import com.gw.common.HttpResult;
import com.gw.entities.DeptAreaBayInfo;
import com.gw.sys.service.DeptAreaBayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @Description 班组区域跨间绑定
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

}
