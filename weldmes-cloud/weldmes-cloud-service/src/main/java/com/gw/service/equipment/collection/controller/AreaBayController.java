package com.gw.service.equipment.collection.controller;

import com.gw.common.HttpResult;
import com.gw.entities.AreaBayInfo;
import com.gw.entities.IdListVO;
import com.gw.service.equipment.collection.service.AreaBayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
public class AreaBayController {

    @Autowired
    AreaBayService areaBayService;

    /**
     * @Date 2021/7/15 16:19
     * @Description  绑定区域跨间
     * @Params areaBayInfo 区域跨间实体类
     */
    @RequestMapping(value = "areaBay/addBayToAreaInfo",method = RequestMethod.POST)
    public HttpResult addBayToAreaInfo(@RequestBody IdListVO areaBays){

        areaBayService.addAreaBayInfo(areaBays.getAreaBayInfos());

        return HttpResult.ok("成功绑定区域跨间");
    }

    /**
     * @Date 2021/8/17 11:17
     * @Description  查询区域跨间树状图
     * @Params
     */
    @RequestMapping(value = "areaBay/getAreaBayTreeInfo",method = RequestMethod.POST)
    public HttpResult getAreaBayTreeInfo(){

        List<AreaBayInfo> list = areaBayService.getAreaBayTreeInfo();

        return HttpResult.ok(list);
    }
}
