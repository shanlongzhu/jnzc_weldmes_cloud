package com.gw.process.solderer.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.ExcelUtils;
import com.gw.common.HttpResult;
import com.gw.entities.TaskInfo;
import com.gw.entities.WelderInfo;
import com.gw.process.solderer.service.SoldererService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Date 2021/7/1 10:16
 * @Description 焊工管理
 * @Params
 */
@CrossOrigin
@RestController
@RequestMapping(value = "solderer")
public class SoldererController {

    @Autowired
    private SoldererService soldererService;

    //列表展示
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn,String welderName,String welderNo,Integer rate,
                              Integer talent,Integer grade) {

        PageHelper.startPage(pn, 10);
        List<WelderInfo> list = soldererService.getList(welderName,welderNo,rate,talent,grade);
        PageInfo page = new PageInfo(list, 5);
        return HttpResult.ok(page);
    }

    //无分页列表展示
    @GetMapping(value = "noPage")
    public HttpResult getListNoPage(String welderName,String welderNo,Integer rate,
                              Integer talent,Integer grade) {
        List<WelderInfo> list = soldererService.getList(welderName,welderNo,rate,talent,grade);
        return HttpResult.ok(list);
    }

    //新增焊工信息
    @PostMapping
    public HttpResult addSolderer(@RequestBody WelderInfo welderInfo) {
        try {
            HttpResult result = new HttpResult();
            int i = soldererService.addSolderer(welderInfo);
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

    //修改前先查询
    @GetMapping("{id}")
    public HttpResult getById(@PathVariable Long id) {
        List<WelderInfo> list = soldererService.getById(id);
        return HttpResult.ok(list);
    }

    //修改焊工信息
    @PutMapping
    public HttpResult updateSolderer(@RequestBody WelderInfo welderInfo) {
        try {
            HttpResult result = new HttpResult();
            int i = soldererService.updateSolderer(welderInfo);
            if (i > 0) {
                result.setMsg("修改成功！");
            } else {
                result.setMsg("修改失败！");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    //删除焊工信息
    @DeleteMapping
    public HttpResult deleteSolderer(Long id) {
        try {
            HttpResult result = new HttpResult();
            int i = soldererService.deleteSolderer(id);
            if (i > 0) {
                result.setMsg("删除成功！");
            } else {
                result.setMsg("删除失败！");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    //导出Excel
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response,String welderName,String welderNo,Integer rate,
                                  Integer talent,Integer grade){
        HttpResult result=new HttpResult();
        List<WelderInfo> list = soldererService.getList(welderName,welderNo,rate,talent,grade);
        Workbook workbook=new XSSFWorkbook();
        Sheet sheet=workbook.createSheet("焊工管理数据");
        String[] titles={"姓名","编号","手机","级别","资质","部门","备注"};
        Row row=sheet.createRow(0);
        for (int i = 0; i <titles.length ; i++) {
            Cell cell=row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        for (int i = 0; i <list.size() ; i++) {
            row=sheet.createRow(i+1);
            WelderInfo welderInfo=list.get(i);
            Cell getWelderNameCell=row.createCell(0);
            getWelderNameCell.setCellValue(welderInfo.getWelderName());
            Cell WelderNoCell=row.createCell(1);
            WelderNoCell.setCellValue(welderInfo.getWelderNo());
            Cell cellphoneCell=row.createCell(2);
            cellphoneCell.setCellValue(welderInfo.getCellphone());
            Cell rankCell=row.createCell(3);
            rankCell.setCellValue(welderInfo.getSysDictionary().getValueName());
            Cell certificationCell=row.createCell(4);
            certificationCell.setCellValue(welderInfo.getSysDictionary().getValueNames());
            Cell getDeptNameCell=row.createCell(5);
            getDeptNameCell.setCellValue(welderInfo.getSysDept().getName());
            Cell getRemarksCell=row.createCell(6);
            getRemarksCell.setCellValue(welderInfo.getRemarks());
        }
        try {
            String fileName= URLEncoder.encode("焊工管理.xlsx","UTF-8");
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
            Workbook workbook=new XSSFWorkbook(file.getInputStream());
            Sheet sheet=workbook.getSheetAt(0);
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum=sheet.getLastRowNum();
            Row firstRow = sheet.getRow(firstRowNum);
            int lastCellNums = firstRow.getLastCellNum();
            List<WelderInfo> welderInfoArrayList=new ArrayList<>();
            for (int i = 1; i <=lastRowNum ; i++) {
                Row row=sheet.getRow(i);
                Object[] obs=new Object[lastCellNums];
                for (int j = 0; j <lastCellNums ; j++) {
                    Cell cell=row.getCell(j);
                    if(row.getCell(j)==null){
                        continue;
                    }
                    Object value= ExcelUtils.getValue(cell);
                    obs[j]=value;

                }
                WelderInfo welderInfo=new WelderInfo();
                welderInfo.setWelderName((String) obs[0]);
                welderInfo.setWelderNo((String) obs[1]);
                welderInfo.setCellphone((String) obs[2]);
                String rank=(String) obs[3];
                Byte rankId=soldererService.getRankId(rank);
                welderInfo.setRank(rankId);
                String certification=(String) obs[4];
                Byte certificationId=soldererService.getCertificationId(certification);
                welderInfo.setCertification(certificationId);
                String deptName=(String) obs[5];
                Long deptId=soldererService.getDeptId(deptName);
                welderInfo.setDeptId(deptId);
                welderInfo.setRemarks((String) obs[6]);
                welderInfoArrayList.add(welderInfo);
            }
            soldererService.importExcel(welderInfoArrayList);
            result.setMsg("导入成功！");
        }catch (Exception e){
            e.printStackTrace();
            result.setMsg("导入失败！");
        }
        return  result;
    }

    /**
     * @Date 2021/7/13 18:01
     * @Description 获取历史曲线中焊工id、姓名以及编号
     * @Params
     */
    @RequestMapping(value = "historySoldererInfos")
    public HttpResult historySoldererInfos(){

        Set<WelderInfo> list = soldererService.getHistoryWelderInfos();

        return HttpResult.ok(list);
    }
}
