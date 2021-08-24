package com.gw.process.craft.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.SxCO2ProcessIssue;
import com.gw.process.craft.service.SxCO2ProcessIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/18 10:09
 * @Description 松下CO2工艺控制器
 */
@CrossOrigin
@RestController
public class SxCO2ProcessIssueController {

    @Autowired
    SxCO2ProcessIssueService sxCO2ProcessIssueService;

    /**
     * @Date 2021/8/18 10:10
     * @Description 松下CO2工艺列表查询
     * @Params
     */
    @RequestMapping(value = "sxCO2/getSxCO2ProcessIssueInfos", method = RequestMethod.GET)
    public HttpResult getSxCO2ProcessIssueList(@RequestParam(value="pn",defaultValue = "1") Integer pn){

        PageHelper.startPage(pn,10);

        List<SxCO2ProcessIssue> list = sxCO2ProcessIssueService.getSxCO2ProcessIssueInfos();

        PageInfo page=new PageInfo(list,5);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/8/19 16:31
     * @Description 添加松下CO2工艺信息
     * @Params
     */
    @RequestMapping(value = "sxCO2/addSxCO2ProcessIssueInfo",method = RequestMethod.POST)
    public HttpResult addSxCO2ProcessIssueInfo(@RequestBody SxCO2ProcessIssue sxCO2ProcessIssue){

        sxCO2ProcessIssueService.addSxCO2ProcessIssueInfo(sxCO2ProcessIssue);

        return HttpResult.ok("添加成功");
    }

    /**
     * @Date 2021/8/19 15:40
     * @Description 根据id 查询 松下CO2工艺信息
     * @Params
     */
    @RequestMapping(value = "sxCO2/getSxCO2ProcessIssueInfoById",method = RequestMethod.GET)
    public HttpResult getSxCO2ProcessIssueInfoById(Long id){

        SxCO2ProcessIssue sxCO2ProcessIssue = sxCO2ProcessIssueService.getSxCO2ProcessIssueInfoById(id);

        return HttpResult.ok(sxCO2ProcessIssue);
    }

    /**
     * @Date 2021/8/19 15:46
     * @Description 根据id 修改 松下CO2工艺信息
     * @Params
     */
    @RequestMapping(value = "sxCO2/updateSxCO2ProcessIssueInfo",method = RequestMethod.PUT)
    public HttpResult updateSxCO2ProcessIssueInfo(@RequestBody SxCO2ProcessIssue sxCO2ProcessIssue){

        sxCO2ProcessIssueService.updateSxCO2ProcessIssueInfo(sxCO2ProcessIssue);

        return HttpResult.ok("修改成功");
    }

    /**
     * @Date 2021/8/19 16:22
     * @Description 根据id删除 松下CO2工艺信息
     * @Params
     */
    @RequestMapping(value = "sxCO2/deleteSxCO2ProcessIssueInfoById",method = RequestMethod.DELETE)
    public HttpResult deleteSxCO2ProcessIssueInfoById(Long id){

        sxCO2ProcessIssueService.deleteSxCO2ProcessIssueInfoById(id);

        return HttpResult.ok("删除成功");
    }


}
