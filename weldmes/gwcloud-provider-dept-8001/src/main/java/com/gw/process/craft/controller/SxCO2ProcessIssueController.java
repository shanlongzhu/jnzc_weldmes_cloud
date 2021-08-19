package com.gw.process.craft.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.SxCO2ProcessIssue;
import com.gw.process.craft.service.SxCO2ProcessIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/18 10:09
 * @Description 松下CO2工艺控制器
 */
@RestController
public class SxCO2ProcessIssueController {

    @Autowired
    SxCO2ProcessIssueService sxCO2ProcessIssueService;

    /**
     * @Date 2021/8/18 10:10
     * @Description 松下CO2工艺列表查询
     * @Params
     */
    @RequestMapping("sxCO2/getSxCO2ProcessIssueInfos")
    public HttpResult getSxCO2ProcessIssueList(@RequestParam(value="pn",defaultValue = "1") Integer pn){

        PageHelper.startPage(pn,10);

        List<SxCO2ProcessIssue> list = sxCO2ProcessIssueService.getSxCO2ProcessIssueInfos();

        PageInfo page=new PageInfo(list,5);

        return HttpResult.ok(page);
    }

}
