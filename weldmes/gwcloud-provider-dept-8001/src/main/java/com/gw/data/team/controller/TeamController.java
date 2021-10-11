package com.gw.data.team.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.data.team.service.TeamService;
import com.gw.entities.UserLoginInfo;
import com.gw.entities.WeldStatisticsData;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "team")
public class TeamController {
    @Autowired
    private TeamService teamService;

    //班组生产数据列表展示
    @GetMapping
    public HttpResult getList(@RequestParam(value="pn",defaultValue = "1") Integer pn,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                              String time1,String time2,String deptId){

        PageHelper.startPage(pn,size);

        List<WeldStatisticsData> list=teamService.getList(time1,time2,deptId);

        PageInfo page=new PageInfo(list,10);

        return HttpResult.ok(page);
    }

    //导出excel
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response,String time1,String time2,String deptId) throws ParseException {

        List<WeldStatisticsData> list = teamService.getList(time1,time2,deptId);

        Workbook workbook=new XSSFWorkbook();

        Sheet sheet=workbook.createSheet("班组生产数据");
        String[] titles={"班组","设备总数","开机设备数","实焊设备数","未绑定设备数","设备利用率","焊接任务数","焊接时间","工作时间","正常焊接时间",
                "焊接效率","超规范时间","规范符合率","焊材消耗","电能消耗"};

        Row row=sheet.createRow(0);

        //标题行内容信息填入
        for (int i = 0; i <titles.length ; i++) {
            Cell cell=row.createCell(i);
            cell.setCellValue(titles[i]);
        }

        //数据填入
        for (int i = 0; i <list.size() ; i++) {

            row = sheet.createRow(i+1);

            WeldStatisticsData weldStatistics = list.get(i);

            //班组
            Cell getNameCell = row.createCell(0);

            if(ObjectUtils.isEmpty(weldStatistics.getDeptName())){

                getNameCell.setCellValue("");
            }else{
                getNameCell.setCellValue(weldStatistics.getDeptName());
            }

            //设备总数
            Cell WelderCountCell=row.createCell(1);

            if(ObjectUtils.isEmpty(weldStatistics.getAllCount())){

                WelderCountCell.setCellValue("");
            }else{
                WelderCountCell.setCellValue(weldStatistics.getAllCount());
            }

            //开机设备数
            Cell cellCount2Cell=row.createCell(2);

            if(ObjectUtils.isEmpty(weldStatistics.getOnOffCount())){

                cellCount2Cell.setCellValue("");
            }else {
                cellCount2Cell.setCellValue(weldStatistics.getOnOffCount());
            }

            //实焊设备数
            Cell count3Cell=row.createCell(3);

            if(ObjectUtils.isEmpty(weldStatistics.getRealWeldOnline())){

                count3Cell.setCellValue("");
            }else {
                count3Cell.setCellValue(weldStatistics.getRealWeldOnline());
            }

            //未绑定设备数
            Cell count4Cell=row.createCell(4);

            if(ObjectUtils.isEmpty(weldStatistics.getNoTaskCount())){

                count4Cell.setCellValue("");
            }else {
                count4Cell.setCellValue(weldStatistics.getNoTaskCount());
            }

            //设备利用率
            Cell getUtilizationCell=row.createCell(5);

            if(ObjectUtils.isEmpty(weldStatistics.getEquipUtilization())){
                weldStatistics.setEquipUtilization(0.00);
            }

            getUtilizationCell.setCellValue(weldStatistics.getEquipUtilization());

            //设置单元格格式
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            getUtilizationCell.setCellStyle(cellStyle);

            //焊接任务数
            Cell getCount5Cell=row.createCell(6);

            if(ObjectUtils.isEmpty(weldStatistics.getTaskCount())){

                getCount5Cell.setCellValue("");
            }

            getCount5Cell.setCellValue(weldStatistics.getTaskCount());

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            //焊接时间
            Cell getTimeCell=row.createCell(7);

            if(ObjectUtils.isEmpty(weldStatistics.getRealWeldTime())){

                getTimeCell.setCellValue("");
            }else{
                getTimeCell.setCellValue(weldStatistics.getRealWeldTime());
            }

            //工作时间
            Cell getTime2Cell=row.createCell(8);

            if(ObjectUtils.isEmpty(weldStatistics.getOnOffTime())){

                getTime2Cell.setCellValue("");
            }else{
                getTime2Cell.setCellValue(weldStatistics.getOnOffTime());
            }

            //正常焊接时间
            Cell normalTimeCell = row.createCell(9);

            if(ObjectUtils.isEmpty(weldStatistics.getSupergageTime())){

                normalTimeCell.setCellValue("");
            }else{
                normalTimeCell.setCellValue(weldStatistics.getNormalTime());
            }

            //焊接效率
            Cell weldingEfficiencyCell = row.createCell(10);

            if(ObjectUtils.isEmpty(weldStatistics.getWeldingEfficiency())){

                weldingEfficiencyCell.setCellValue("");
            }else{
                weldingEfficiencyCell.setCellValue(weldStatistics.getWeldingEfficiency());
            }

            //超规范时间
            Cell supergageTimeCell = row.createCell(11);

            if(ObjectUtils.isEmpty(weldStatistics.getSupergageTime())){

                supergageTimeCell.setCellValue("");
            }else{
                supergageTimeCell.setCellValue(weldStatistics.getSupergageTime());
            }

            //规范符合率
            Cell standardPercentageCell = row.createCell(12);

            if(ObjectUtils.isEmpty(weldStatistics.getStandardPercentage())){

                standardPercentageCell.setCellValue("");
            }else{
                standardPercentageCell.setCellValue(weldStatistics.getStandardPercentage());
            }

            //焊材消耗
            Cell materialsConsumptionCell = row.createCell(13);

            if(ObjectUtils.isEmpty(weldStatistics.getMaterialsConsumption())){

                materialsConsumptionCell.setCellValue("");
            }else{
                materialsConsumptionCell.setCellValue(weldStatistics.getMaterialsConsumption());
            }

            //电能消耗
            Cell powerConsumptionCell = row.createCell(14);

            if(ObjectUtils.isEmpty(weldStatistics.getPowerConsumption())){

                powerConsumptionCell.setCellValue("");
            }else{
                powerConsumptionCell.setCellValue(weldStatistics.getPowerConsumption());
            }

        }

        try { String time=new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());

            String fileName= URLEncoder.encode("班组生产数据"+time+".xlsx","UTF-8");

            response.setContentType("application/octet-stream");

            response.setHeader("content-disposition","attachment;filename="+fileName);

            response.setHeader("filename",fileName);

            workbook.write(response.getOutputStream());

        } catch (Exception e) {

            e.printStackTrace();
            return HttpResult.error("导出失败");
        }

        return HttpResult.ok("导出成功");
    }
}
