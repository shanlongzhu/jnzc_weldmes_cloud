package com.gw.data.historicalCurve.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.data.historicalCurve.service.HistoricalCurveService;
import com.gw.entities.RtData;
import com.gw.entities.TableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "historicalCurve")
public class HistoricalCurveController {

    @Autowired
    private HistoricalCurveService historicalCurveService;

    /**
     * @Date 2021/7/14 8:57
     * @Description 焊机历史曲线
     * @Params startTime 开始时间  endTime 结束时间  taskId 任务id  welderId 焊工id  weldMachineId 焊机id
     */
    @PostMapping
    public HttpResult getList(@RequestBody TableInfo tableInfo) {

        Map<String, Object> map = historicalCurveService.getList(tableInfo);

        return HttpResult.ok(map);
    }

    /**
     * @Date 2021/7/14 8:57
     * @Description 焊机历史曲线信息列表
     * @Params startTime 开始时间  endTime 结束时间  taskId 任务id  welderId 焊工id  weldMachineId 焊机id
     */
    @RequestMapping(value = "getHistoryInfos", method = RequestMethod.POST)
    public HttpResult getHistoryCurveInfos(@RequestParam(value = "pn", defaultValue = "1") Integer pn,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size,
                                           @RequestBody TableInfo tableInfo) {

        PageHelper.startPage(pn, size);

        List<RtData> list = historicalCurveService.getHistoryCurveInfos(tableInfo);

        PageInfo page = new PageInfo(list, 10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/7/15 17:46
     * @Description 根据表名 查询 焊机历史曲线
     * @Params
     */
    @RequestMapping(value = "HistoricalCurveByTableName")
    public HttpResult getHistoricalCurveByTableName(@RequestBody TableInfo tableInfo) {

        Map<String, Object> map = historicalCurveService.getHistoryCurveInfoByTableName(tableInfo);

        return HttpResult.ok(map);
    }

}
