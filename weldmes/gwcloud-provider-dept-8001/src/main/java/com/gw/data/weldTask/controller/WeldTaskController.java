package com.gw.data.weldTask.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.data.weldTask.service.WeldTaskService;
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
@RequestMapping(value = "weldTask")
public class WeldTaskController {

    @Autowired
    private WeldTaskService weldTaskService;

    //焊机任务表数据列表展示
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Long areaId ,Long teamId,String time1, String time2,Long status) {
        PageHelper.startPage(pn, 10);
        List<RealtimeData> list = weldTaskService.getList(areaId,teamId,time1, time2,status);
        PageInfo page = new PageInfo(list, 5);
        return HttpResult.ok(page);
    }

    //导出excel
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response, Long areaId ,Long teamId,String time1, String time2,Long status) {
        HttpResult result = new HttpResult();
        List<RealtimeData> list = weldTaskService.getList(areaId,teamId,time1, time2,status);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("焊机任务表");
        String[] titles = {"所属班组", "设备编号", "日期", "任务号","状态"};
        Row row = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            RealtimeData realtimeData = list.get(i);
            Cell getNameCell = row.createCell(0);
            getNameCell.setCellValue(realtimeData.getSysDept().getName());
            Cell getFWeldNoCell = row.createCell(1);
            getFWeldNoCell.setCellValue(realtimeData.getFWeldNo());
            Cell getFWeldTimeCell = row.createCell(2);
            getFWeldTimeCell.setCellValue(realtimeData.getFWeldTime());
            Cell getTaskNoCell = row.createCell(3);
            getTaskNoCell.setCellValue(realtimeData.getTaskInfo().getTaskNo());
            Cell getValueNameCell = row.createCell(4);
            getValueNameCell.setCellValue(realtimeData.getSysDictionary().getValueName());
        }
        try {
            String fileName = URLEncoder.encode("焊机任务表.xlsx", "UTF-8");
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
