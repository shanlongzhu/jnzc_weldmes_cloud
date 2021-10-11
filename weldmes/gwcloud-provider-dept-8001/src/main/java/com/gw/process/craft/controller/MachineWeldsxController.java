package com.gw.process.craft.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;

import com.gw.entities.MachineWeldsxInfo;
import com.gw.process.craft.dao.MachineWeldsxDao;
import com.gw.process.craft.service.MachineWeldsxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/25 15:22
 * @Description 松下设备控制器
 */
@CrossOrigin
@RestController
public class MachineWeldsxController {

    @Autowired
    MachineWeldsxDao machineWeldsxDao;

    @Autowired
    MachineWeldsxService machineWeldsxService;

    /**
     * @Date 2021/8/25 15:23
     * @Description 松下设备列表查询
     * @Params
     */
    @RequestMapping(value = "sx/getSxProcessIssueInfos", method = RequestMethod.GET)
    public HttpResult getSxProcessIssueList(@RequestParam(value="pn",defaultValue = "1") Integer pn,Long equipType,
                                            @RequestParam(value="size",defaultValue = "10") Integer size){

        PageHelper.startPage(pn,size);

        //松下设备列表查询
        List<MachineWeldsxInfo> list = machineWeldsxService.getMachineWeldsxInfos(equipType);

        PageInfo page = new PageInfo(list,10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/8/25 16:20
     * @Description 松下设备信息新增
     * @Params
     */
    @RequestMapping(value = "sx/addSxProcessIssueInfos", method = RequestMethod.POST)
    public HttpResult addSxProcessIssueInfo(@RequestBody MachineWeldsxInfo machineWeldsxInfo){

        //设备序号 唯一标识判断
        Integer weldNoYesOrNo = machineWeldsxDao.judgeWeldNoYesOrNo(machineWeldsxInfo.getWeldNo());

        if(!ObjectUtils.isEmpty(weldNoYesOrNo)){

            return HttpResult.ok("设备序号已存在!");
        }

        //设备CID 唯一标识判断
        Integer weldCid = machineWeldsxDao.judgeWeldCidYesOrNo(machineWeldsxInfo.getWeldCid());

        if(!ObjectUtils.isEmpty(weldCid)){

            return HttpResult.ok("设备CID已存在!");
        }

        //松下设备新增
        machineWeldsxService.addMachineWeldsxInfo(machineWeldsxInfo);

        return HttpResult.ok("新增成功");
    }

    /**
     * @Date 2021/8/25 16:34
     * @Description 根据 id  查询 松下设备信息
     * @Params
     */
    @RequestMapping(value = "sx/getSxProcessIssueInfoById", method = RequestMethod.GET)
    public HttpResult getSxProcessIssueInfoById(Long id){

        //根据id 查询 松下设备信息
        MachineWeldsxInfo machineWeldsxInfo = machineWeldsxService.getMachineWeldsxInfoById(id);

        return HttpResult.ok(machineWeldsxInfo);
    }

    /**
     * @Date 2021/8/25 16:43
     * @Description 修改松下设备信息
     * @Params
     */
    @RequestMapping(value = "sx/updateSxProcessIssueInfo", method = RequestMethod.PUT)
    public HttpResult updateSxProcessIssueInfo(@RequestBody MachineWeldsxInfo machineWeldsxInfo){

        //修改松下设备信息
        machineWeldsxService.updateMachineWeldsxInfo(machineWeldsxInfo);

        return HttpResult.ok("修改成功");
    }

    /**
     * @Date 2021/8/25 16:57
     * @Description 删除松下设备信息
     * @Params
     */
    @RequestMapping(value = "sx/delSxProcessIssueInfo", method = RequestMethod.DELETE)
    public HttpResult delSxProcessIssueInfo(Long id){

        //删除松下设备信息
        machineWeldsxService.delMachineWeldsxInfo(id);

        return HttpResult.ok("删除成功");
    }

}
