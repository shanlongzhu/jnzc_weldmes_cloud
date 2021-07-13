package com.gw.equipment.welder.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.ExcelUtils;
import com.gw.common.HttpResult;
import com.gw.entities.MachineGatherInfo;
import com.gw.entities.MachineWeldInfo;
import com.gw.entities.TaskInfo;
import com.gw.equipment.welder.service.WelderService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Date 2021/6/30 16:50
 * @Description 生产设备控制器
 * @Params
 */
@CrossOrigin
@RestController
@RequestMapping(value = "welder")
public class WelderController {

    @Autowired
    private WelderService welderService;

    //列表展示
    @GetMapping
    public HttpResult getList(@RequestParam(value="pn",defaultValue = "1") Integer pn,
                              String machineNo,Integer type,Integer grade,Integer status,
                              Integer firm,Long isNetwork,String gatherNo,String ipPath,Integer model){
        PageHelper.startPage(pn,10);
        List<MachineWeldInfo> list=welderService.getList(machineNo,type,grade,status,firm,isNetwork,gatherNo,ipPath,model);
        PageInfo page=new PageInfo(list,5);
        return HttpResult.ok(page);
    }

    //新增焊机信息
    @PostMapping
    public HttpResult addWelder(@RequestBody MachineWeldInfo machineWeldInfo){
        try {
            HttpResult result=new HttpResult();
            int i=welderService.addWelder(machineWeldInfo);
            if(i>0){
                result.setMsg("新增成功！");
            }else {
                result.setMsg("新增失败！");
            }
            return result;
        } catch (Exception e) {
           return HttpResult.error();
        }
    }

    //修改前先查询
    @GetMapping("{id}")
    public HttpResult getById(@PathVariable Long id){
        List<MachineWeldInfo> list=welderService.getById(id);
        return HttpResult.ok(list);

    }

    //修改信息
    @PutMapping
    public HttpResult updateWelder(@RequestBody MachineWeldInfo machineWeldInfo){
        try {
            HttpResult result=new HttpResult();
            int i=welderService.updateWelder(machineWeldInfo);
            if(i>0){
                result.setMsg("修改成功！");
            }else {
                result.setMsg("修改失败！");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    //删除信息
    @DeleteMapping
    public HttpResult deleteWelder(Long id){
        try {
            HttpResult result=new HttpResult();
            int i=welderService.deleteWelder(id);
            if(i>0){
                result.setMsg("删除成功！");
            }else {
                result.setMsg("删除失败！");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }

    }

    //导出Excel
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response,String machineNo,Integer type,Integer grade,Integer status,
                                  Integer firm,Long isNetwork,String gatherNo,String ipPath,Integer model){
        HttpResult result=new HttpResult();
        List<MachineWeldInfo> list = welderService.getList(machineNo,type,grade,status,firm,isNetwork,gatherNo,ipPath,model);
        Workbook workbook=new XSSFWorkbook();
        Sheet sheet=workbook.createSheet("焊机设备数据");
        String[] titles={"固定资产编号","设备类型","入厂时间","所属项目","状态","厂家","是否在网","采集序号","位置","ip地址","设备型号"};
        Row row=sheet.createRow(0);
        for (int i = 0; i <titles.length ; i++) {
            Cell cell=row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        for (int i = 0; i <list.size() ; i++) {
            row=sheet.createRow(i+1);
            MachineWeldInfo machineWeldInfo=list.get(i);
            Cell getMachineNoCell=row.createCell(0);
            getMachineNoCell.setCellValue(machineWeldInfo.getMachineNo());
            Cell valueNameCell=row.createCell(1);
            valueNameCell.setCellValue(machineWeldInfo.getSysDictionary().getValueName());
            Cell createTimeCell=row.createCell(2);
            createTimeCell.setCellValue(machineWeldInfo.getCreateTime());
            Cell nameCell=row.createCell(3);
            nameCell.setCellValue(machineWeldInfo.getSysDept().getName());
            Cell getValueNamesCell=row.createCell(4);
            getValueNamesCell.setCellValue(machineWeldInfo.getSysDictionary().getValueNames());
            Cell getValueNamessCell=row.createCell(5);
            getValueNamessCell.setCellValue(machineWeldInfo.getSysDictionary().getValueNamess());
            Cell getIsNetworkCell=row.createCell(6);
            getIsNetworkCell.setCellValue(machineWeldInfo.getIsNetwork()==0?"是":"否");
            Cell getGatherNoCell=row.createCell(7);
            getGatherNoCell.setCellValue(machineWeldInfo.getMachineGatherInfo().getGatherNo());
            Cell positionCell=row.createCell(8);
            positionCell.setCellValue("");
            Cell ipPathCell=row.createCell(9);
            ipPathCell.setCellValue("");
            Cell getValueNamesssCell=row.createCell(10);
            getValueNamesssCell.setCellValue(machineWeldInfo.getSysDictionary().getValueNamessss());

        }
        try {
            String fileName= URLEncoder.encode("焊机设备管理.xlsx","UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("content-disposition","attachment;filename="+fileName);
            response.setHeader("filename",fileName);
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //导入
    @PostMapping(value = "importExcel",produces = "application/json;charset=UTF-8")
    public HttpResult importExcel(@RequestParam("file")MultipartFile file){
        HttpResult result=new HttpResult();
        try {
            //workbook excel
            Workbook workbook=new XSSFWorkbook(file.getInputStream());
            //获取excel的第一个sheet
            Sheet sheet=workbook.getSheetAt(0);
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum=sheet.getLastRowNum();
            Row firstRow = sheet.getRow(firstRowNum);
            int lastCellNums = firstRow.getLastCellNum();
            List<MachineWeldInfo> machineWeldInfoArrayList=new ArrayList<>();
            for (int i = 1; i <=lastRowNum ; i++) {
                //获取第i行
                Row row=sheet.getRow(i);

                Object[] obs=new Object[lastCellNums];
                for (int j = 0; j <lastCellNums ; j++) {
                    //获取第i行的 第j个 单元格
                    Cell cell=row.getCell(j);
                    if(row.getCell(j)==null){
                        continue;
                    }
                    //拿到单元格的 value值
                    Object value= ExcelUtils.getValue(cell);
                    obs[j]=value;

                }
                //把从excel中拿出来的数据封装到 对象中
                MachineWeldInfo machineWeldInfo=new MachineWeldInfo();
                machineWeldInfo.setMachineNo((String) obs[0]);
                String type=(String) obs[1];
                Byte id=welderService.getTypeId(type);
                machineWeldInfo.setType(id);
                machineWeldInfo.setCreateTime((String) obs[2]);
                String deptName=(String) obs[3];
                Long deptId=welderService.getDeptId(deptName);
                machineWeldInfo.setDeptId(deptId);
                String status=(String) obs[4];
                Byte statusId=welderService.getStatusId(status);
                machineWeldInfo.setStatus(statusId);
                String firm=(String) obs[5];
                Byte firmId=welderService.getFirmId(firm);
                machineWeldInfo.setFirm(firmId);
                String isNetWork=(String) obs[6];
                if(isNetWork.equals("是")){
                    machineWeldInfo.setIsNetwork(0);
                }else {
                    machineWeldInfo.setIsNetwork(1);
                }
                String machineNo=(String) obs[7];
                Long gId=welderService.getGid(machineNo);
                machineWeldInfo.setGId(gId);
                String model=(String) obs[10];
                Byte modelId=welderService.getModelId(model);
                machineWeldInfo.setModel(modelId);
                //把对象放到list
                machineWeldInfoArrayList.add(machineWeldInfo);
            }
            //保存
            welderService.importExcel(machineWeldInfoArrayList);
            result.setMsg("导入成功！");
        }catch (Exception e){
            e.printStackTrace();
            result.setMsg("导入失败！");
        }
        return  result;
    }

    /**
     * @Date 2021/7/13 18:01
     * @Description 获取历史曲线中焊机id以及设备编号
     * @Params
     */
    @RequestMapping(value = "historyWelderInfos")
    public HttpResult historyWelderInfos(){

        List<MachineWeldInfo> list = welderService.getIdAndMachineNoOfWelderInfos();

        return HttpResult.ok(list);
    }

}
