package com.gw.process.craft.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;

import com.gw.entities.MachineWeldsxInfo;
import com.gw.process.craft.service.MachineWeldsxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/25 15:22
 * @Description 松下工艺库控制器
 */
@CrossOrigin
@RestController
public class MachineWeldsxController {

    @Autowired
    MachineWeldsxService machineWeldsxService;

    /**
     * @Date 2021/8/25 15:23
     * @Description 工艺库列表查询
     * @Params
     */
    @RequestMapping(value = "sx/getSxProcessIssueInfos", method = RequestMethod.GET)
    public HttpResult getSxProcessIssueList(@RequestParam(value="pn",defaultValue = "1") Integer pn){

        PageHelper.startPage(pn,10);

        List<MachineWeldsxInfo> list = machineWeldsxService.getMachineWeldsxInfos();

        PageInfo page=new PageInfo(list,5);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/8/25 16:20
     * @Description 工艺库信息新增
     * @Params
     */
    @RequestMapping(value = "sx/addSxProcessIssueInfos", method = RequestMethod.POST)
    public HttpResult addSxProcessIssueInfo(@RequestBody MachineWeldsxInfo machineWeldsxInfo){

        machineWeldsxService.addMachineWeldsxInfo(machineWeldsxInfo);

        return HttpResult.ok("新增成功");
    }

    /**
     * @Date 2021/8/25 16:34
     * @Description 根据 id  查询 工艺库信息
     * @Params
     */
    @RequestMapping(value = "sx/getSxProcessIssueInfoById", method = RequestMethod.GET)
    public HttpResult getSxProcessIssueInfoById(Long id){

        MachineWeldsxInfo machineWeldsxInfo = machineWeldsxService.getMachineWeldsxInfoById(id);

        return HttpResult.ok(machineWeldsxInfo);
    }

    /**
     * @Date 2021/8/25 16:43
     * @Description 修改工艺库信息
     * @Params
     */
    @RequestMapping(value = "sx/updateSxProcessIssueInfo", method = RequestMethod.PUT)
    public HttpResult updateSxProcessIssueInfo(@RequestBody MachineWeldsxInfo machineWeldsxInfo){

        machineWeldsxService.updateMachineWeldsxInfo(machineWeldsxInfo);

        return HttpResult.ok("修改成功");
    }

    /**
     * @Date 2021/8/25 16:57
     * @Description 删除工艺库信息
     * @Params
     */
    @RequestMapping(value = "sx/delSxProcessIssueInfo", method = RequestMethod.DELETE)
    public HttpResult delSxProcessIssueInfo(Long id){

        machineWeldsxService.delMachineWeldsxInfo(id);

        return HttpResult.ok("删除成功");
    }

}
