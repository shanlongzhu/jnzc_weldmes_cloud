package com.gw.process.craft.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.SxAT3ProcessIssue;
import com.gw.process.craft.service.SxAT3ProcessIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author zhanghan
 * @Date 2021/8/30 14:17
 * @Description 松下AT3工艺控制器
 */
@CrossOrigin
@RestController
public class SxAT3ProcessIssueController {

    @Autowired
    SxAT3ProcessIssueService sxAT3ProcessIssueService;

    /**
     * @Date 2021/8/30 14:18
     * @Description 松下AT3工艺列表查询
     * @Params
     */
    @RequestMapping(value = "sxAT3/getSxAT3ProcessIssueInfos", method = RequestMethod.GET)
    public HttpResult getSxAT3ProcessIssueInfos(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Long wpsLibraryId,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageHelper.startPage(pn, size);

        List<SxAT3ProcessIssue> list = sxAT3ProcessIssueService.getSxAT3ProcessIssueInfos(wpsLibraryId);

        PageInfo<?> page = new PageInfo<>(list, 10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/8/30 14:42
     * @Description 松下AT3工艺信息新增
     * @Params
     */
    @RequestMapping(value = "sxAT3/addSxAT3ProcessIssueInfos", method = RequestMethod.POST)
    public HttpResult getSxAT3ProcessIssueInfos(@RequestBody SxAT3ProcessIssue sxAT3ProcessIssue) {

        sxAT3ProcessIssueService.addSxAT3ProcessIssueInfo(sxAT3ProcessIssue);

        return HttpResult.ok("新增成功");
    }

    /**
     * @Date 2021/8/30 15:02
     * @Description 根据 id 查询 松下AT3工艺信息
     * @Params
     */
    @RequestMapping(value = "sxAT3/getSxAT3ProcessIssueInfoById", method = RequestMethod.GET)
    public HttpResult getSxAT3ProcessIssueInfoById(Long id) {

        SxAT3ProcessIssue sxAT3ProcessIssue = sxAT3ProcessIssueService.getSxAT3ProcessIssueInfoById(id);

        return HttpResult.ok(sxAT3ProcessIssue);
    }

    /**
     * @Date 2021/8/30 15:12
     * @Description 修改松下AT3工艺信息
     * @Params
     */
    @RequestMapping(value = "sxAT3/updateSxAT3ProcessIssueInfo", method = RequestMethod.PUT)
    public HttpResult updateSxAT3ProcessIssueInfo(@RequestBody SxAT3ProcessIssue sxAT3ProcessIssue) {

        sxAT3ProcessIssueService.updateSxAT3ProcessIssueInfo(sxAT3ProcessIssue);

        return HttpResult.ok("更新成功");
    }

    /**
     * @Date 2021/8/30 15:21
     * @Description 删除松下AT3工艺信息
     * @Params
     */
    @RequestMapping(value = "sxAT3/delSxAT3ProcessIssueInfo", method = RequestMethod.DELETE)
    public HttpResult delSxAT3ProcessIssueInfo(Long id) {

        sxAT3ProcessIssueService.deleteSxAT3ProcessIssueInfo(id);

        return HttpResult.ok("删除成功");
    }

    /**
     * @Date 2021/9/3 13:25
     * @Description 根据 工艺库id  查询  通道号
     * @Params
     */
    @RequestMapping(value = "sxAT3/getChannelNosById", method = RequestMethod.GET)
    public HttpResult getSxAT3ChannelNos(Long id) {

        List<String> list = sxAT3ProcessIssueService.getChannelNos(id);

        return HttpResult.ok(list);
    }
}
