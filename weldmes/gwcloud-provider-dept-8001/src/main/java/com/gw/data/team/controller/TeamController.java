package com.gw.data.team.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.data.team.service.TeamService;
import com.gw.entities.MachineGatherInfo;
import com.gw.entities.RealtimeData;
import com.gw.entities.WelderInfo;
import com.gw.entities.weldStatisticsData;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.DecimalFormat;
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
    public HttpResult getList(@RequestParam(value="pn",defaultValue = "1") Integer pn,String time1,String time2){
        PageHelper.startPage(pn,10);
        List<weldStatisticsData> list=teamService.getList(time1,time2);
        PageInfo page=new PageInfo(list,5);
        return HttpResult.ok(page);
    }

    //导出excel
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response,String time1,String time2){
        HttpResult result=new HttpResult();
        List<weldStatisticsData> list = teamService.getList(time1,time2);
        Workbook workbook=new XSSFWorkbook();
        Sheet sheet=workbook.createSheet("班组生产数据");
        String[] titles={"班组","设备总数","开机设备数","实焊设备数","未绑定设备数","设备利用率","焊接任务数","焊接时间","工作时间","焊接效率"};
        Row row=sheet.createRow(0);
        for (int i = 0; i <titles.length ; i++) {
            Cell cell=row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        for (int i = 0; i <list.size() ; i++) {

            row=sheet.createRow(i+1);

            weldStatisticsData weldStatistics= list.get(i);

            Cell getNameCell=row.createCell(0);
            getNameCell.setCellValue(weldStatistics.getSysDept().getName());

            Cell WelderCountCell=row.createCell(1);
            WelderCountCell.setCellValue(weldStatistics.getCount());

            Cell cellCount2Cell=row.createCell(2);
            cellCount2Cell.setCellValue(weldStatistics.getCount2());

            Cell count3Cell=row.createCell(3);
            count3Cell.setCellValue(weldStatistics.getCount3());

            Cell count4Cell=row.createCell(4);
            count4Cell.setCellValue(weldStatistics.getCount4());

            Cell getUtilizationCell=row.createCell(5);
            if(ObjectUtils.isEmpty(weldStatistics.getUtilization())){
                weldStatistics.setUtilization(0.00);
            }

            getUtilizationCell.setCellValue(weldStatistics.getUtilization());

            //设置单元格格式
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            getUtilizationCell.setCellStyle(cellStyle);

            Cell getCount5Cell=row.createCell(6);
            getCount5Cell.setCellValue(weldStatistics.getCount5());
            Cell getTimeCell=row.createCell(7);
            getTimeCell.setCellValue(new SimpleDateFormat("HH:mm:ss").format(weldStatistics.getTime()));
            Cell getTime2Cell=row.createCell(8);
            getTime2Cell.setCellValue(new SimpleDateFormat("HH:mm:ss").format(weldStatistics.getTime2()));
            Cell getUtilization2Cell=row.createCell(9);

            if(ObjectUtils.isEmpty(weldStatistics.getUtilization2())){
                weldStatistics.setUtilization2(0.00);
            }

            CellStyle cellStyle2 = workbook.createCellStyle();
            cellStyle2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            getUtilization2Cell.setCellStyle(cellStyle2);
            getUtilization2Cell.setCellValue(weldStatistics.getUtilization2());
        }
        try {
            String fileName= URLEncoder.encode("班组生产数据.xlsx","UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("content-disposition","attachment;filename="+fileName);
            response.setHeader("filename",fileName);
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
