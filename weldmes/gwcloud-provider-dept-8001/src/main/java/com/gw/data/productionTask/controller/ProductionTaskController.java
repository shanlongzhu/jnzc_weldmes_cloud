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
import org.springframework.util.ObjectUtils;
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
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn,String time1, String time2,String welderNo,String welderName,String machineNo,String taskNo,Long deptId) {
        String name=productionTaskService.getName(deptId);
        PageHelper.startPage(pn, 10);
        List<WeldStatisticsData> list = productionTaskService.getList(time1, time2,welderNo,welderName,machineNo,taskNo,name);
        PageInfo page = new PageInfo(list, 5);
        return HttpResult.ok(page);
    }

    //导出excel
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response,String time1, String time2,String welderNo,String welderName,String machineNo,String taskNo,Long deptId) {
        HttpResult result = new HttpResult();
        String name=productionTaskService.getName(deptId);
        List<WeldStatisticsData> list = productionTaskService.getList(time1, time2,welderNo,welderName,machineNo,taskNo,name);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("生产任务详情");
        String[] titles = {"焊工编号", "焊工姓名", "焊机编号","任务编号","开始时间","结束时间","电流范围","平均电流","电压范围","平均电压"};
        Row row = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        for (int i = 0; i < list.size(); i++) {

            row = sheet.createRow(i + 1);
            WeldStatisticsData weldStatisticsData = list.get(i);

            Cell getFWelderNoCell = row.createCell(0);
            getFWelderNoCell.setCellValue(weldStatisticsData.getWelderInfo().getWelderNo());

            Cell getWelderNameCell = row.createCell(1);
            getWelderNameCell.setCellValue(weldStatisticsData.getWelderInfo().getWelderName());

            Cell getFWeldNoCell = row.createCell(2);
            getFWeldNoCell.setCellValue(weldStatisticsData.getMachineWeldInfo().getMachineNo());

            Cell getFJunctionNoCell = row.createCell(3);
            getFJunctionNoCell.setCellValue(weldStatisticsData.getTaskInfo().getTaskNo());

            Cell getRealityStartTimeCell = row.createCell(4);
            getRealityStartTimeCell.setCellValue(weldStatisticsData.getTaskInfo().getRealityStarttime());

            Cell getRealityEndTimeCell = row.createCell(5);
            getRealityEndTimeCell.setCellValue(weldStatisticsData.getTaskInfo().getRealityEndtime());

            Cell eleScopeCell = row.createCell(6);
            eleScopeCell.setCellValue(weldStatisticsData.getEleScope());

            Cell electricityCell = row.createCell(7);
            if(ObjectUtils.isEmpty(weldStatisticsData.getElectricity())){
                electricityCell.setCellValue("");
            }else{
                electricityCell.setCellValue(weldStatisticsData.getElectricity().doubleValue());
            }

            Cell volScopeCell = row.createCell(8);
            volScopeCell.setCellValue(weldStatisticsData.getVolScope());

            Cell voltageyCell = row.createCell(9);
            if(ObjectUtils.isEmpty(weldStatisticsData.getVoltage())){
                voltageyCell.setCellValue("");
            }else{
                voltageyCell.setCellValue(weldStatisticsData.getVoltage().doubleValue());
            }
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
