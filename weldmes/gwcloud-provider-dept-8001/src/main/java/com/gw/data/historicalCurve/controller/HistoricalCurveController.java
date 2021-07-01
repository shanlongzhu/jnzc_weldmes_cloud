package com.gw.data.historicalCurve.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.data.historicalCurve.service.HistoricalCurveService;
import com.gw.entities.RealtimeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "historicalCurve")
public class HistoricalCurveController {

    @Autowired
    private HistoricalCurveService historicalCurveService;

    //历史曲线展示
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn,RealtimeData realtimeData) {
        PageHelper.startPage(pn, 10);
        List<RealtimeData> list = historicalCurveService.getList(realtimeData);
        PageInfo page = new PageInfo(list, 5);
        return HttpResult.ok(page);
    }
}
