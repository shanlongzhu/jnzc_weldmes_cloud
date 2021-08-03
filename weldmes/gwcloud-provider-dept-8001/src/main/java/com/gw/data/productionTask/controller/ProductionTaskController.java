package com.gw.data.productionTask.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.data.productionTask.service.ProductionTaskService;
import com.gw.entities.RealtimeData;
import com.gw.entities.WeldStatisticsData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "ProductionTask")
public class ProductionTaskController {

    @Autowired
    private ProductionTaskService productionTaskService;

    //生产任务详情数据列表展示
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Long areaId , Long teamId, String welderNo,String junctionNo, String time1, String time2) {
        PageHelper.startPage(pn, 10);
        List<WeldStatisticsData> list = productionTaskService.getList(areaId,teamId,welderNo,junctionNo,time1, time2);
        PageInfo page = new PageInfo(list, 5);
        return HttpResult.ok(page);
    }

    //导出excel
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response, Long areaId , Long teamId, String welderNo,String junctionNo, String time1, String time2) {
        HttpResult result = new HttpResult();
        List<WeldStatisticsData> list = productionTaskService.getList(areaId,teamId,welderNo,junctionNo,time1, time2);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("生产任务详情");
        String[] titles = {"焊工班组", "焊工编号", "焊工姓名", "焊机编号","任务编号","开始时间","结束时间","使用通道","良好段","报警段","平均焊接电流","平均焊接电压","符合规范率(%)"};
        Row row = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            WeldStatisticsData weldStatisticsData = list.get(i);
            Cell getNameCell = row.createCell(0);
            getNameCell.setCellValue(weldStatisticsData.getSysDept().getName());
            Cell getFWelderNoCell = row.createCell(1);
            getFWelderNoCell.setCellValue(weldStatisticsData.getWelderNo());
            Cell getWelderNameCell = row.createCell(2);
            getWelderNameCell.setCellValue(weldStatisticsData.getWelderInfo().getWelderName());
            Cell getFWeldNoCell = row.createCell(3);
            getFWeldNoCell.setCellValue(weldStatisticsData.getMachineNo());
            Cell getFJunctionNoCell = row.createCell(4);
            getFJunctionNoCell.setCellValue(weldStatisticsData.getTaskNo());
            Cell getRealityStartTimeCell = row.createCell(5);
            getRealityStartTimeCell.setCellValue(weldStatisticsData.getTaskInfo().getRealityStarttime());
            Cell getRealityEndTimeCell = row.createCell(6);
            getRealityEndTimeCell.setCellValue(weldStatisticsData.getTaskInfo().getRealityEndtime());
            Cell getFChannelCell = row.createCell(7);
            getFChannelCell.setCellValue(" ");
            Cell getTimeCell = row.createCell(8);
            getTimeCell.setCellValue(new SimpleDateFormat("HH:mm:ss").format(weldStatisticsData.getTime()));
            Cell getTime2Cell = row.createCell(9);
            getTime2Cell.setCellValue(new SimpleDateFormat("HH:mm:ss").format(weldStatisticsData.getTime2()));
            Cell getFElectricityCell = row.createCell(10);
            getFElectricityCell.setCellValue(weldStatisticsData.getElectricity().toString());
            Cell getFVoltageCell = row.createCell(11);
            getFVoltageCell.setCellValue(weldStatisticsData.getVoltage().toString());
            Cell getUtilizationCell = row.createCell(12);
            getUtilizationCell.setCellValue(weldStatisticsData.getUtilization().toString());
        }
        try {
            String time=new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
            String fileName = URLEncoder.encode("生产任务详情"+time+".xlsx", "UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("content-disposition", "attachment;filename=" + fileName);
            response.setHeader("filename", fileName);
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
