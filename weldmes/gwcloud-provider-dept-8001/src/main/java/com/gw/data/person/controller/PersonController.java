package com.gw.data.person.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.data.person.service.PersonService;
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
@RequestMapping(value = "person")
public class PersonController {

    @Autowired
    private PersonService personService;

    //人员生产数据列表展示
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn, String time1, String time2) {
        PageHelper.startPage(pn, 10);
        List<RealtimeData> list = personService.getList(time1,time2);
        PageInfo page = new PageInfo(list, 5);
        return HttpResult.ok(page);
    }

    //导出excel
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response, String time1, String time2) {
        HttpResult result = new HttpResult();
        List<RealtimeData> list = personService.getList(time1,time2);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("人员生产数据");
        String[] titles = {"焊工姓名", "焊工编号", "焊接任务数", "焊接时间", "工作时间", "焊接效率"};
        Row row = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            RealtimeData realtimeData = list.get(i);
            Cell getWelderNameCell = row.createCell(0);
            getWelderNameCell.setCellValue(realtimeData.getWelderInfo().getWelderName());
            Cell getWelderNoCell = row.createCell(1);
            getWelderNoCell.setCellValue(realtimeData.getWelderInfo().getWelderNo());
            Cell cellCountCell = row.createCell(2);
            cellCountCell.setCellValue(realtimeData.getCount());
            Cell TimeCell = row.createCell(3);
            TimeCell.setCellValue(new SimpleDateFormat("HH:mm:ss").format(realtimeData.getTime()));
            Cell Time2Cell = row.createCell(4);
            Time2Cell.setCellValue(new SimpleDateFormat("HH:mm:ss").format(realtimeData.getTime2()));
            Cell getUtilizationCell = row.createCell(5);
            getUtilizationCell.setCellValue(new DecimalFormat("#.00").format(realtimeData.getUtilization()));
        }
        try {
            String fileName = URLEncoder.encode("人员生产数据.xlsx", "UTF-8");
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
