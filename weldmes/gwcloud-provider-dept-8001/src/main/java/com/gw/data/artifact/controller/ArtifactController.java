package com.gw.data.artifact.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.data.artifact.service.ArtifactService;
import com.gw.entities.RealtimeData;
import com.gw.entities.WeldStatisticsData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "artifact")
public class ArtifactController {

    @Autowired
    private ArtifactService artifactService;


    //工件生产数据列表展示
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn, String time1, String time2) {
        PageHelper.startPage(pn, 10);
        List<WeldStatisticsData> list = artifactService.getList(time1, time2);
        PageInfo page = new PageInfo(list, 5);
        return HttpResult.ok(page);
    }

    //导出excel
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response, String time1, String time2) {
        HttpResult result = new HttpResult();
        List<WeldStatisticsData> list = artifactService.getList(time1, time2);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("工件生产数据");
        String[] titles = {"任务编号", "参与人员数","使用设备数","工作时间", "焊接时间", "正常时间","焊接效率","超规范时间","规范符合率","焊材消耗","电能消耗"};
        Row row = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            WeldStatisticsData weldStatisticsData = list.get(i);
            Cell getTaskNoCell = row.createCell(0);
            getTaskNoCell.setCellValue(weldStatisticsData.getTaskInfo().getTaskNo());
            Cell countCell = row.createCell(1);
            countCell.setCellValue(weldStatisticsData.getCount());
            Cell count2Cell = row.createCell(2);
            count2Cell.setCellValue(weldStatisticsData.getCount2());
            Cell onOffTimeCell = row.createCell(3);
            onOffTimeCell.setCellValue(weldStatisticsData.getOnOffTime());
            Cell realWeldTimeCell = row.createCell(4);
            realWeldTimeCell.setCellValue(weldStatisticsData.getRealWeldTime());
            Cell normalTimeCell = row.createCell(5);
            normalTimeCell.setCellValue(weldStatisticsData.getNormalTime());
            if(weldStatisticsData.getWeldingEfficiency()==null){
                Cell weldingEfficiencyCell = row.createCell(6);
                weldingEfficiencyCell.setCellValue(" ");
            }else{
                Cell weldingEfficiencyCell = row.createCell(6);
                weldingEfficiencyCell.setCellValue(weldStatisticsData.getWeldingEfficiency());
            }
            Cell supergageTimeCell = row.createCell(7);
            supergageTimeCell.setCellValue(weldStatisticsData.getSupergageTime());
            if(weldStatisticsData.getStandardPercentage()==null){
                Cell standardPercentageCell = row.createCell(8);
                standardPercentageCell.setCellValue(" ");
            }else {
                Cell standardPercentageCell = row.createCell(8);
                standardPercentageCell.setCellValue(weldStatisticsData.getStandardPercentage());
            }
            if(weldStatisticsData.getStandardPercentage()==null){
                Cell standardPercentageCell = row.createCell(9);
                standardPercentageCell.setCellValue(" ");
            }else {
                Cell standardPercentageCell = row.createCell(9);
                standardPercentageCell.setCellValue(weldStatisticsData.getStandardPercentage());
            }
            if(weldStatisticsData.getMaterialsConsumption()==null){
                Cell materialsConsumptionCell = row.createCell(10);
                materialsConsumptionCell.setCellValue(" ");
            }else {
                Cell materialsConsumptionCell = row.createCell(10);
                materialsConsumptionCell.setCellValue(weldStatisticsData.getMaterialsConsumption());
            }
            if(weldStatisticsData.getPowerConsumption()==null){
                Cell powerConsumptioCell = row.createCell(11);
                powerConsumptioCell.setCellValue(" ");
            }else {
                Cell powerConsumptioCell = row.createCell(11);
                powerConsumptioCell.setCellValue(weldStatisticsData.getPowerConsumption());
            }
        }
        try {
            String time=new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
            String fileName = URLEncoder.encode("工件生产数据"+time+".xlsx", "UTF-8");
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
