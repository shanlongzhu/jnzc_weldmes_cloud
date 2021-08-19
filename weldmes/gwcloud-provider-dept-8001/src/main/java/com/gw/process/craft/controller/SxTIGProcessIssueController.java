package com.gw.process.craft.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.SxTIGProcessIssue;
import com.gw.process.craft.service.SxTIGProcessIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/19 11:00
 * @Description 松下TIG工艺控制器
 */
@RestController
public class SxTIGProcessIssueController {

    @Autowired
    SxTIGProcessIssueService sxTIGProcessIssueService;

    /**
     * @Date 2021/8/19 11:01
     * @Description 松下TIG工艺列表查询
     * @Params
     */
    @RequestMapping("sxTIG/getSxTIGProcessIssueInfos")
    public HttpResult getSxCO2ProcessIssueList(@RequestParam(value="pn",defaultValue = "1") Integer pn){

        PageHelper.startPage(pn,10);

        List<SxTIGProcessIssue> list = sxTIGProcessIssueService.getSxTIGProcessIssueInfos();

        PageInfo page=new PageInfo(list,5);

        return HttpResult.ok(page);
    }
}
