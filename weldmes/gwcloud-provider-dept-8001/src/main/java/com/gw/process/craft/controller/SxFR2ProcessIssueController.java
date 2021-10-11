package com.gw.process.craft.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.SxFR2ProcessIssue;
import com.gw.process.craft.service.SxFR2ProcessIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/31 11:17
 * @Description 松下FR2工艺控制器
 */
@CrossOrigin
@RestController
public class SxFR2ProcessIssueController {

    @Autowired
    SxFR2ProcessIssueService sxFR2ProcessIssueService;

    /**
     * @Date 2021/8/31 11:17
     * @Description 松下FR2工艺信息列表查询
     * @Params
     */
    @RequestMapping(value = "sx/getSxFR2ProcessIssueInfos",method = RequestMethod.GET)
    public HttpResult getSxFR2ProcessIssueInfos(@RequestParam(value="pn",defaultValue = "1") Integer pn,Long wpsLibraryId,
                                                @RequestParam(value="size",defaultValue = "10") Integer size){

        PageHelper.startPage(pn,size);

        List<SxFR2ProcessIssue> list = sxFR2ProcessIssueService.getSxFR2ProcessIssueInfos(wpsLibraryId);

        PageInfo page=new PageInfo(list,10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/8/31 14:37
     * @Description 根据id查询松下FR2工艺信息
     * @Params
     */
    @RequestMapping(value = "sx/getSxFR2ProcessIssueInfoById",method = RequestMethod.GET)
    public HttpResult getSxFR2ProcessIssueInfoById(Long id){

        SxFR2ProcessIssue sxFR2ProcessIssue = sxFR2ProcessIssueService.getSxFR2ProcessIssueInfoById(id);

        return HttpResult.ok(sxFR2ProcessIssue);
    }

    /**
     * @Date 2021/8/31 14:42
     * @Description 新增松下FR2工艺信息
     * @Params
     */
    @RequestMapping(value = "sx/addSxFR2ProcessIssueInfo",method = RequestMethod.POST)
    public HttpResult addSxFR2ProcessIssueInfo(@RequestBody SxFR2ProcessIssue sxFR2ProcessIssue){

        sxFR2ProcessIssueService.addSxFR2ProcessIssueInfo(sxFR2ProcessIssue);

        return HttpResult.ok("新增成功");
    }

    /**
     * @Date 2021/8/31 14:44
     * @Description 修改松下FR2工艺信息
     * @Params
     */
    @RequestMapping(value = "sx/alterSxFR2ProcessIssueInfo",method = RequestMethod.PUT)
    public HttpResult alterSxFR2ProcessIssueInfo(@RequestBody SxFR2ProcessIssue sxFR2ProcessIssue){

        sxFR2ProcessIssueService.alterSxFR2ProcessIssueInfo(sxFR2ProcessIssue);

        return HttpResult.ok("修改成功");
    }

    /**
     * @Date 2021/8/31 15:06
     * @Description 删除松下FR2工艺信息
     * @Params
     */
    @RequestMapping(value = "sx/delSxFR2ProcessIssueInfo",method = RequestMethod.DELETE)
    public HttpResult delSxFR2ProcessIssueInfo(Long id){

        sxFR2ProcessIssueService.delSxFR2ProcessIssueInfo(id);

        return HttpResult.ok("删除成功");
    }

    /**
     * @Date 2021/9/3 13:25
     * @Description 根据 工艺库id  查询  通道号
     * @Params
     */
    @RequestMapping(value = "sxFR2/getChannelNosById",method = RequestMethod.GET)
    public HttpResult getSxFR2ChannelNos(Long id){

        List<String> list = sxFR2ProcessIssueService.getChannelNos(id);

        return HttpResult.ok(list);
    }
}
