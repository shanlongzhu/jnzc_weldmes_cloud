package com.gw.process.craft.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.SxTIGProcessIssue;
import com.gw.process.craft.service.SxTIGProcessIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/19 11:00
 * @Description 松下TIG工艺控制器
 */
@CrossOrigin
@RestController
public class SxTIGProcessIssueController {

    @Autowired
    SxTIGProcessIssueService sxTIGProcessIssueService;

    /**
     * @Date 2021/8/19 11:01
     * @Description 松下TIG工艺列表查询
     * @Params
     */
    @RequestMapping(value = "sxTIG/getSxTIGProcessIssueInfos",method = RequestMethod.GET)
    public HttpResult getSxTIGProcessIssueList(@RequestParam(value="pn",defaultValue = "1") Integer pn,Long wpsLibraryId,
                                               Integer size){

        PageHelper.startPage(pn,size);

        List<SxTIGProcessIssue> list = sxTIGProcessIssueService.getSxTIGProcessIssueInfos(wpsLibraryId);

        PageInfo page=new PageInfo(list,5);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/8/23 15:36
     * @Description 松下TIG工艺信息新增
     * @Params
     */
    @RequestMapping(value = "sxTIG/addSxTIGProcessIssueInfo",method = RequestMethod.POST)
    public HttpResult addSxTIGProcessIssueList(@RequestBody SxTIGProcessIssue sxTIGProcessIssue){

        sxTIGProcessIssueService.addSxTIGProcessIssueInfo(sxTIGProcessIssue);

        return HttpResult.ok("新增成功");
    }

    /**
     * @Date 2021/8/23 16:07
     * @Description 根据 id 查询 松下TIG工艺信息
     * @Params
     */
    @RequestMapping(value = "sxTIG/getSxTIGProcessIssueInfoById",method = RequestMethod.GET)
    public HttpResult getSxTIGProcessIssueInfoById(Long id){

        SxTIGProcessIssue sxTIGProcessIssue = sxTIGProcessIssueService.getSxTIGProcessIssueInfoById(id);

        return HttpResult.ok(sxTIGProcessIssue);
    }

    /**
     * @Date 2021/8/23 16:21
     * @Description 修改 松下TIG工艺信息
     * @Params
     */
    @RequestMapping(value = "sxTIG/updateSxTIGProcessIssueInfo",method = RequestMethod.PUT)
    public HttpResult updateSxTIGProcessIssueInfo(@RequestBody SxTIGProcessIssue sxTIGProcessIssue){

        sxTIGProcessIssueService.updateSxTIGProcessIssueInfo(sxTIGProcessIssue);

        return HttpResult.ok("修改成功");
    }

    /**
     * @Date 2021/8/23 16:51
     * @Description 删除 松下TIG工艺信息
     * @Params
     */
    @RequestMapping(value = "sxTIG/deleteSxTIGProcessIssueInfo",method = RequestMethod.DELETE)
    public HttpResult deleteSxTIGProcessIssueInfo(Long id){

        sxTIGProcessIssueService.deleteSxTIGProcessIssueInfo(id);

        return HttpResult.ok("删除成功");
    }

}
