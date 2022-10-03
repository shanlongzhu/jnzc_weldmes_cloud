package com.gw.service.data.weldTask.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.service.data.weldTask.service.WeldTaskService;
import com.gw.entities.WeldStatisticsDataWeldStatics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "weldTask")
public class WeldTaskController {

    @Autowired
    private WeldTaskService weldTaskService;

    /**
     * @Date 2021/10/19 14:00
     * @Description 获取焊机任务信息列表
     * @Params
     */
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                              Long areaId ,Long teamId,String time1, String time2,Long status) {

        PageHelper.startPage(pn, size);

        List<WeldStatisticsDataWeldStatics> list = weldTaskService.getList(areaId,teamId,time1, time2,status);

        PageInfo<WeldStatisticsDataWeldStatics> page = new PageInfo<>(list, 10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/10/19 14:00
     * @Description 导出excel
     * @Params
     */
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response, Long areaId ,Long teamId,String time1, String time2,Long status) {

        List<WeldStatisticsDataWeldStatics> list = weldTaskService.getList(areaId,teamId,time1, time2,status);

        //Sheet sheet = workbook.createSheet("焊机任务表");
        //String[] titles = {"所属班组", "设备编号", "日期", "任务号","状态"};

        return HttpResult.ok("导出成功");
    }
}
