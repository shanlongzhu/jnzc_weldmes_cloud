package com.gw.equipment.collection.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.ExcelUtils;
import com.gw.common.HttpResult;

import com.gw.config.DownExcel;
import com.gw.entities.MachineGatherInfo;
import com.gw.entities.MachineWeldInfo;
import com.gw.equipment.collection.dao.CollectionDao;
import com.gw.equipment.collection.service.CollectionService;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2021/10/14 9:26
 * @Description 采集模块控制器
 * @Params
 */
@CrossOrigin
@RestController
@RequestMapping(value = "collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private CollectionDao collectionDao;

    /**
     * @Date 2021/10/14 9:27
     * @Description 采集信息列表查询
     * @Params
     */
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn,@RequestParam(value = "size", defaultValue = "10") Integer size,
                              Integer grade, Integer gatherNo) {
        PageHelper.startPage(pn, size);
        List<MachineGatherInfo> list = collectionService.getList(grade, gatherNo);
        PageInfo page = new PageInfo(list, 10);

        return HttpResult.ok(page);
    }


    /**
     * @Date 2021/10/14 9:27
     * @Description 新增采集信息
     * @Params
     */
    @PostMapping
    public HttpResult addCollection(@RequestBody MachineGatherInfo machineGatherInfo) {

        //判断新增信息的采集编号是否存在
        Integer judge = collectionDao.judgeGatherNoYesOrNo(machineGatherInfo.getGatherNo(),machineGatherInfo.getId());

        if(!ObjectUtils.isEmpty(judge)){

            return HttpResult.ok("当前信息中采集编号已存在,是否覆盖此条信息?");
        }

        collectionService.addCollection(machineGatherInfo);

        return HttpResult.ok("新增成功");
    }

    /**
     * @Date 2021/10/14 9:27
     * @Description 删除采集信息
     * @Params
     */
    @DeleteMapping
    public HttpResult deleteCollection(Long id) {

        collectionService.deleteCollection(id);

        return HttpResult.ok("删除成功");

    }

    /**
     * @Date 2021/10/14 9:28
     * @Description 根据id查询采集信息
     * @Params
     */
    @GetMapping("{id}")
    public HttpResult getById(@PathVariable Long id) {

        List<MachineGatherInfo> list = collectionService.getById(id);

        return HttpResult.ok(list);
    }

    /**
     * @Date 2021/10/14 9:28
     * @Description 修改采集信息
     * @Params
     */
    @PutMapping
    public HttpResult updateCollection(@RequestBody MachineGatherInfo machineGatherInfo){

        //判断新增信息的采集编号是否存在
        Integer judge = collectionDao.judgeGatherNoYesOrNo(machineGatherInfo.getGatherNo(),machineGatherInfo.getId());

        if(!ObjectUtils.isEmpty(judge)){

            return HttpResult.ok("当前信息中采集编号不可重复,请更换采集编号");
        }

        collectionService.updateCollection(machineGatherInfo);

        return HttpResult.ok("修改成功");
    }


    /**
     * @Date 2021/10/14 9:28
     * @Description 导出excel
     * @Params
     */
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response, Integer grade, Integer gatherNo) {

        try {

            //设置Excel文件名
            String title = URLEncoder.encode("采集模块管理", "UTF-8");

            //设置sheet表格名
            String sheetName = "采集数据";

            //获取任务列表
            List<MachineGatherInfo> list = collectionService.getList(grade, gatherNo);

            //导出为Excel
            DownExcel.download(response,MachineGatherInfo.class,list,sheetName,title);

            return HttpResult.ok("Excel导出成功");

        } catch (Exception e) {

            return HttpResult.error("Excel导出失败");
        }
    }

    //导入
    @PostMapping(value = "importExcel", produces = "application/json;charset=UTF-8")
    public HttpResult importExcel(@RequestParam("file") MultipartFile file) {

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

                String str = obs[0].toString();

                if(str.indexOf(".") > 0){

                    str = str.substring(0,str.indexOf("."));

                    machineGatherInfo.setGatherNo(str);
                }

                machineGatherInfo.setGatherNo(str);

                String name = (String) obs[1];

                Long id = collectionService.getDeptId(name);

                machineGatherInfo.setDeptId(id);
                String valueName = (String) obs[2];

                Integer status = collectionService.getStatus(valueName);
                machineGatherInfo.setStatus(status);

                /*String valueNames = (String) obs[3];
                Integer protocol = collectionService.getProtocol(valueNames);*/

                //machineGatherInfo.setProtocol(protocol);
                machineGatherInfo.setIpPath((String) obs[4]);

                machineGatherInfo.setMacPath((String) obs[5]);

                if(!ObjectUtils.isEmpty(obs[6])){

                    machineGatherInfo.setCreateTime(obs[6].toString());
                }

                //把对象放到list
                machineGatherInfoList.add(machineGatherInfo);

            }
            //保存
            collectionService.importExcel(machineGatherInfoList);

            return HttpResult.ok("导入成功！");

        } catch (Exception e) {

            e.printStackTrace();

            return HttpResult.error("导入失败！");
        }
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

    /**
     * @Date 2021/8/5 15:41
     * @Description 直接新增信息
     * @Params
     */
    @RequestMapping("judgeAfterAddGatherInfo")
    public HttpResult judgeGatherNo(@RequestBody MachineGatherInfo machineGatherInfo){

        collectionService.addCollection(machineGatherInfo);

        return HttpResult.ok("新增成功！");

    }
}

