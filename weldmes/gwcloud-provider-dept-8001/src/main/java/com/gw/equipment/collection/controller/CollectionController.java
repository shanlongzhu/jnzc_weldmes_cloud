package com.gw.equipment.collection.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.ExcelUtils;
import com.gw.common.HttpResult;

import com.gw.entities.MachineGatherInfo;
import com.gw.equipment.collection.service.CollectionService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    //列表展示
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Integer grade, Integer gatherNo) {
        PageHelper.startPage(pn, 10);
        List<MachineGatherInfo> list = collectionService.getList(grade, gatherNo);
        PageInfo page = new PageInfo(list, 5);

        return HttpResult.ok(page);
    }

    //新增信息
    @PostMapping
    public HttpResult addCollection(@RequestBody MachineGatherInfo machineGatherInfo) {
        try {
            HttpResult result = new HttpResult();

            int i = collectionService.addCollection(machineGatherInfo);

            if (i > 0) {

                result.setMsg("新增成功！");
            } else {

                result.setMsg("新增失败！");
            }

            return result;

        } catch (Exception e) {

            return HttpResult.error();
        }
    }

    //删除信息
    @DeleteMapping
    public HttpResult deleteCollection(Long id) {
        HttpResult result = new HttpResult();
        int i = collectionService.deleteCollection(id);
        if (i > 0) {
            result.setMsg("删除成功！");
        } else {
            result.setMsg("删除失败！");
        }
        return result;

    }

    //修改前先查询
    @GetMapping("{id}")
    public HttpResult getById(@PathVariable Long id) {

        List<MachineGatherInfo> list = collectionService.getById(id);

        return HttpResult.ok(list);
    }

    //修改信息
    @PutMapping
    public HttpResult updateCollection(@RequestBody MachineGatherInfo machineGatherInfo){

        int i = collectionService.updateCollection(machineGatherInfo);

        if(i>0){

            return HttpResult.ok("修改成功！");

        }else {

            return HttpResult.error("修改失败！");

        }
    }


    //导出Excel
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response, Integer grade, Integer gatherNo) {
        HttpResult result = new HttpResult();
        List<MachineGatherInfo> list = collectionService.getList(grade, gatherNo);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("采集数据");
        String[] titles = {"采集模块编号", "所属项目", "采集模块状态", "采集模块通讯协议", "采集模块IP地址", "采集模块MAC地址", "采集模块出厂时间"};
        Row row = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            MachineGatherInfo machineGatherInfo = list.get(i);
            Cell getGatherNoCell = row.createCell(0);
            getGatherNoCell.setCellValue(machineGatherInfo.getGatherNo());
            Cell nameCell = row.createCell(1);
            nameCell.setCellValue(machineGatherInfo.getSysDept().getName());
            Cell valueNameCell = row.createCell(2);
            valueNameCell.setCellValue(machineGatherInfo.getSysDictionary().getValueName());
            Cell valuesNameCell = row.createCell(3);
            valuesNameCell.setCellValue(machineGatherInfo.getSysDictionary().getValueNames());
            Cell ipPathCell = row.createCell(4);
            ipPathCell.setCellValue(machineGatherInfo.getIpPath());
            Cell macPathCell = row.createCell(5);
            macPathCell.setCellValue(machineGatherInfo.getMacPath());
            Cell createTimeCell = row.createCell(6);
            createTimeCell.setCellValue(machineGatherInfo.getCreateTime());
        }
        try {
            String fileName = URLEncoder.encode("采集模块管理.xlsx", "UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("content-disposition", "attachment;filename=" + fileName);
            response.setHeader("filename", fileName);
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //导入
    @PostMapping(value = "importExcel", produces = "application/json;charset=UTF-8")
    public HttpResult importExcel(@RequestParam("file") MultipartFile file) {
        HttpResult result = new HttpResult();
        try {
            //workbook excel
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            //获取excel的第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            Row firstRow = sheet.getRow(firstRowNum);
            int lastCellNums = firstRow.getLastCellNum();
            List<MachineGatherInfo> machineGatherInfoList = new ArrayList<>();
            for (int i = 1; i <= lastRowNum; i++) {
                //获取第i行
                Row row = sheet.getRow(i);
                Object[] obs = new Object[lastCellNums];
                for (int j = 0; j < lastCellNums; j++) {
                    //获取第i行的 第j个单元格
                    Cell cell = row.getCell(j);
                    if (row.getCell(j) == null) {
                        continue;
                    }
                    //拿到单元格的 value值
                    Object value = ExcelUtils.getValue(cell);
                    obs[j] = value;
                }

                //把从excel中拿出来的数据封装到对象中
                MachineGatherInfo machineGatherInfo = new MachineGatherInfo();
                machineGatherInfo.setGatherNo((String) obs[0]);
                String name = (String) obs[1];
                Long id = collectionService.getDeptId(name);
                machineGatherInfo.setDeptId(id);
                String valueName = (String) obs[2];
                Integer status = collectionService.getStatus(valueName);
                machineGatherInfo.setStatus(status);
                String valueNames = (String) obs[3];
                Integer protocol = collectionService.getProtocol(valueNames);
                machineGatherInfo.setProtocol(protocol);
                machineGatherInfo.setIpPath((String) obs[4]);
                machineGatherInfo.setMacPath((String) obs[5]);
                machineGatherInfo.setCreateTime((String) obs[6]);
                //把对象放到list
                machineGatherInfoList.add(machineGatherInfo);
            }
            //保存
            collectionService.importExcel(machineGatherInfoList);
            result.setMsg("导入成功！");
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("导入失败！");
        }
        return result;
    }

    /**
     * @Date 2021/7/1 9:00
     * @Description  拉取采集序号
     * @Params
     */
    @RequestMapping("getGatherNos")
    public HttpResult getGatherNos(){

        List<MachineGatherInfo> list = collectionService.queryGatherNos();

        return HttpResult.ok(list);
    }
}
