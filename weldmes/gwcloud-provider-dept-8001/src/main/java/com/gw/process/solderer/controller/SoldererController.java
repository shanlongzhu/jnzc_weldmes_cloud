package com.gw.process.solderer.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.ExcelUtils;
import com.gw.common.HttpResult;
import com.gw.config.DownExcel;
import com.gw.entities.TaskInfo;
import com.gw.entities.WelderInfo;
import com.gw.process.solderer.dao.SoldererDao;
import com.gw.process.solderer.service.SoldererService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @Autowired
    private SoldererDao soldererDao;

    /**
     * @Date 2021/10/12 15:22
     * @Description 焊工列表查询
     * @Params
     */
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                              String welderName,String welderNo,Integer rate,
                              Integer talent,Integer grade) {

        PageHelper.startPage(pn, size);

        List<WelderInfo> list = soldererService.getList(welderName,welderNo,rate,talent,grade);

        PageInfo page = new PageInfo(list, 10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/10/12 15:22
     * @Description 焊工列表查询 - 无分页
     * @Params
     */
    @GetMapping(value = "noPage")
    public HttpResult getListNoPage(String welderName,String welderNo,Integer rate,
                              Integer talent,Integer grade) {

        List<WelderInfo> list = soldererService.getList(welderName,welderNo,rate,talent,grade);

        return HttpResult.ok(list);
    }

    /**
     * @Date 2021/10/12 15:23
     * @Description 新增焊工信息
     * @Params
     */
    @PostMapping
    public HttpResult addSolderer(@RequestBody WelderInfo welderInfo) {

        Integer judge = soldererDao.judgeWelderNoYesOrNo(welderInfo.getWelderNo(),welderInfo.getId());

        if(!ObjectUtils.isEmpty(judge)){

            return HttpResult.ok("当前信息中焊工编号已存在,是否覆盖此条信息?");
        }

        soldererService.addSolderer(welderInfo);

        return HttpResult.ok("新增成功");
    }

    /**
     * @Date 2021/10/12 15:24
     * @Description 根据焊工id 查询焊工信息
     * @Params
     */
    @GetMapping("{id}")
    public HttpResult getById(@PathVariable Long id) {
        List<WelderInfo> list = soldererService.getById(id);
        return HttpResult.ok(list);
    }

    /**
     * @Date 2021/10/12 15:24
     * @Description 修改焊工信息
     * @Params
     */
    @PutMapping
    public HttpResult updateSolderer(@RequestBody WelderInfo welderInfo) {

        Integer judge = soldererDao.judgeWelderNoYesOrNo(welderInfo.getWelderNo(),welderInfo.getId());

        if(!ObjectUtils.isEmpty(judge)){

            return HttpResult.ok("当前信息中焊工编号不可重复,请更换焊工编号");
        }

        soldererService.updateSolderer(welderInfo);

        return HttpResult.ok("修改成功");

    }

    /**
     * @Date 2021/10/12 15:24
     * @Description 删除焊工信息
     * @Params
     */
    @DeleteMapping
    public HttpResult deleteSolderer(Long id) {

        soldererService.deleteSolderer(id);

        return HttpResult.ok("删除成功");
    }

    /**
     * @Date 2021/10/12 15:24
     * @Description Excel导出
     * @Params
     */
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response,String welderName,String welderNo,Integer rate,
                                  Integer talent,Integer grade) throws IllegalAccessException, IOException, InstantiationException {

        //设置Excel文件名
        String title = URLEncoder.encode("焊工管理数据", "UTF-8");

        //设置sheet表格名
        String sheetName = "焊工信息";

        //获取任务列表
        List<WelderInfo> list = soldererService.getList(welderName,welderNo,rate,talent,grade);

        //导出为Excel
        DownExcel.download(response,WelderInfo.class,list,sheetName,title);

        return HttpResult.ok("Excel导出成功");
    }

    /**
     * @Date 2021/10/12 15:25
     * @Description excel导入
     * @Params
     */
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

                WelderInfo welderInfo = new WelderInfo();

                welderInfo.setWelderName((String) obs[0]);

                welderInfo.setWelderNo((obs[1].toString()));

                welderInfo.setCellphone((String) obs[2]);

                String rank = (String) obs[3];

                Byte rankId=soldererService.getRankId(rank);

                welderInfo.setRank(rankId);

                String certification=(String) obs[4];

                Byte certificationId = soldererService.getCertificationId(certification);

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

    /**
     * @Date 2021/8/5 15:41
     * @Description 新增或更新焊工信息
     * @Params
     */
    @RequestMapping("judgeAfterAddWelderInfo")
    public HttpResult judgeWelderNo(@RequestBody WelderInfo welderInfo){

        soldererService.addSolderer(welderInfo);

        return HttpResult.ok("新增成功！");

    }
}
