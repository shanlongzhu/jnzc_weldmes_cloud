package com.gw.equipment.collection.controller;

import com.gw.common.HttpResult;
import com.gw.entities.AreaBayInfo;
import com.gw.entities.FirmMachineInfo;
import com.gw.equipment.collection.service.FirmMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author zhanghan
 * @Date 2021/7/15 16:42
 * @Description  厂家设备关联控制器
 */
@CrossOrigin
@RestController
public class FirmMachineController {

    @Autowired
    FirmMachineService firmMachineService;

    /**
     * @Date 2021/7/15 16:44
     * @Description 绑定厂家、设备
     * @Params firmMachineInfo 厂家设备信息
     */
    @RequestMapping(value = "firmMachine/addMachineToFirmInfo",method = RequestMethod.POST)
    public HttpResult addBayToAreaInfo(@RequestBody FirmMachineInfo firmMachineInfo){

        firmMachineService.addFirmMachineInfo(firmMachineInfo);

        return HttpResult.ok("成功绑定厂家设备");
    }

}
