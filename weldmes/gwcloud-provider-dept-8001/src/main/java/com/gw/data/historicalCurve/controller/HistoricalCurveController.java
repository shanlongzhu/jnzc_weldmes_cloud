package com.gw.data.historicalCurve.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.data.historicalCurve.service.HistoricalCurveService;
import com.gw.entities.RealtimeData;
import com.gw.entities.RtData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

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
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn, String startTime, String endTime,
                              Long taskId,Long welderId,Long weldMachineId) throws ParseException {
        PageHelper.startPage(pn, 10);
        List<RtData> list = historicalCurveService.getList(startTime,endTime,taskId,welderId,weldMachineId);
        PageInfo page = new PageInfo(list, 5);
        return HttpResult.ok(page);
    }
}
