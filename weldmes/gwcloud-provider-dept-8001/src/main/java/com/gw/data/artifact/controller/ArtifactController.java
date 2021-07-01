package com.gw.data.artifact.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.data.artifact.service.ArtifactService;
import com.gw.entities.RealtimeData;
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
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn, String time1, String time2, String taskNo) {
        PageHelper.startPage(pn, 10);
        List<RealtimeData> list = artifactService.getList(time1, time2, taskNo);
        PageInfo page = new PageInfo(list, 5);
        return HttpResult.ok(page);
    }

    //导出excel
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response, String time1, String time2, String taskNo) {
        HttpResult result = new HttpResult();
        List<RealtimeData> list = artifactService.getList(time1, time2, taskNo);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("工件生产数据");
        String[] titles = {"任务编号", "焊接时间", "工作时间", "焊接效率"};
        Row row = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            RealtimeData realtimeData = list.get(i);
            Cell getTaskNoCell = row.createCell(0);
            getTaskNoCell.setCellValue(realtimeData.getTaskInfo().getTaskNo());
            Cell TimeCell = row.createCell(1);
            TimeCell.setCellValue(new SimpleDateFormat("HH:mm:ss").format(realtimeData.getTime()));
            Cell Time2Cell = row.createCell(2);
            Time2Cell.setCellValue(new SimpleDateFormat("HH:mm:ss").format(realtimeData.getTime2()));
            Cell getUtilizationCell = row.createCell(3);
            getUtilizationCell.setCellValue(new DecimalFormat("#.00").format(realtimeData.getUtilization()));
        }
        try {
            String fileName = URLEncoder.encode("工件生产数据.xlsx", "UTF-8");
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
