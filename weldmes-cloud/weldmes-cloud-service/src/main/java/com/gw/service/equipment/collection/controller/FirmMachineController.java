package com.gw.service.equipment.collection.controller;

import com.gw.common.HttpResult;
import com.gw.entities.IdListVO;
import com.gw.service.equipment.collection.service.FirmMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public HttpResult addBayToAreaInfo(@RequestBody IdListVO firmMachines){

        firmMachineService.addFirmMachineInfo(firmMachines.getFirmMachineInfos());

        return HttpResult.ok("成功绑定厂家设备");
    }

}
