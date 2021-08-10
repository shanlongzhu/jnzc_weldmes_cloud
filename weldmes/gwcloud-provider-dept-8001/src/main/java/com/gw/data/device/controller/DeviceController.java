package com.gw.data.device.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.data.device.service.DeviceService;
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
@RequestMapping(value = "device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    //设备生产数据列表展示
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn, String time1, String time2,String machineNo,Long deptId) {
        PageHelper.startPage(pn, 10);
        List<WeldStatisticsData> list = deviceService.getList(time1,time2,machineNo,deptId);
        PageInfo page = new PageInfo(list, 5);
        return HttpResult.ok(page);
    }

    //导出excel
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response,String time1, String time2,String machineNo,Long deptId) {
        HttpResult result = new HttpResult();
        List<WeldStatisticsData> list = deviceService.getList(time1,time2,machineNo,deptId);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("设备生产数据");
        String[] titles = {"设备编号", "班组","使用人员数", "焊接任务数", "工作时间", "焊接时间","正常时间","焊接效率", "超规范时间","规范符合率","焊材消耗","电能消耗"};
        Row row = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            WeldStatisticsData weldStatisticsData = list.get(i);
            Cell getMachineNoCell = row.createCell(0);
            getMachineNoCell.setCellValue(weldStatisticsData.getMachineWeldInfo().getMachineNo());
            Cell NameCell = row.createCell(1);
            NameCell.setCellValue(weldStatisticsData.getSysDept().getName());
            Cell countCountCell = row.createCell(2);
            countCountCell.setCellValue(weldStatisticsData.getCount());
            Cell cellCountCell = row.createCell(3);
            cellCountCell.setCellValue(weldStatisticsData.getCount2());
            Cell TimeCell = row.createCell(4);
            TimeCell.setCellValue(weldStatisticsData.getRealWeldTime());
            Cell normalTimeCell = row.createCell(5);
            normalTimeCell.setCellValue(weldStatisticsData.getNormalTime());
            Cell Time2Cell = row.createCell(6);
            Time2Cell.setCellValue(weldStatisticsData.getOnOffTime());
            if(weldStatisticsData.getWeldingEfficiency()==null){
                Cell getUtilizationCell = row.createCell(7);
                getUtilizationCell.setCellValue(" ");
            }else{
                Cell getUtilizationCell = row.createCell(7);
                getUtilizationCell.setCellValue(weldStatisticsData.getWeldingEfficiency());
            }
            Cell supergageTimeCell = row.createCell(8);
            supergageTimeCell.setCellValue(weldStatisticsData.getSupergageTime());
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
                String fileName = URLEncoder.encode("设备生产数据"+time+".xlsx", "UTF-8");
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
