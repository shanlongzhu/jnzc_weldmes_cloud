package com.gw.data.historicalCurve.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.data.historicalCurve.service.HistoricalCurveService;
import com.gw.entities.MachineGatherInfo;
import com.gw.entities.MachineWeldsxInfo;
import com.gw.entities.RtData;
import com.gw.entities.TableInfo;
import com.gw.equipment.collection.service.CollectionService;
import com.gw.process.craft.service.MachineWeldsxService;
import com.gw.processdb.DbHistoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "historicalCurve")
public class HistoricalCurveController {

    @Autowired
    private HistoricalCurveService historicalCurveService;
    @Autowired
    CollectionService collectionService;
    @Autowired
    MachineWeldsxService machineWeldsxService;

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

    /**
     * 查询ProcessDB历史数据
     *
     * @param tableInfo 入参
     * @return HttpResult
     */
    @RequestMapping(value = "getProcessDbHistoryData")
    public HttpResult getProcessDbHistoryData(@RequestBody TableInfo tableInfo) {
        final HashMap<String, Object> map = new HashMap<>();
        if (null != tableInfo) {
            final Integer weldType = tableInfo.getWeldType();
            if (weldType != null) {
                final String startTime = tableInfo.getStartTime();
                final String endTime = tableInfo.getEndTime();
                final Long weldMachineId = tableInfo.getWeldMachineId();
                //0:OTC设备
                if (weldType == 0) {
                    //根据焊机id查询采集编号
                    final List<MachineGatherInfo> gatherNoList = collectionService.getGatherNoByWeldId(weldMachineId);
                    if (null != gatherNoList && gatherNoList.size() > 0) {
                        //如果查询到多个采集编号，则只取第一个
                        final String gatherNo = gatherNoList.get(0).getGatherNo();
                        final Map<String, Object> otcMap = new DbHistoryQuery().selectOtcHistoryFromDb(gatherNo, startTime, endTime);
                        map.put("otcHistoryList", otcMap.get("otcHistoryList"));
                        map.put("otcDataNum", otcMap.get("otcDataNum"));
                    }
                }
                //1:松下设备
                else if (weldType == 1) {
                    final MachineWeldsxInfo weldsxInfo = machineWeldsxService.getSxWeldCidById(weldMachineId);
                    if (null != weldsxInfo) {
                        final String weldCid = weldsxInfo.getWeldCid();
                        final Map<String, Object> sxMap = new DbHistoryQuery().selectSxHistoryFromDb(weldCid, startTime, endTime);
                        map.put("sxHistoryList", sxMap.get("sxHistoryList"));
                        map.put("sxDataNum", sxMap.get("sxDataNum"));
                    }
                }
            }
        }
        return HttpResult.ok(map);
    }
}
