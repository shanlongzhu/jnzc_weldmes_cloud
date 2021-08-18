package com.gw.equipment.welder.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.ConstantInfo;
import com.gw.common.ExcelUtils;
import com.gw.common.HttpResult;
import com.gw.entities.MachineWeldInfo;
import com.gw.equipment.welder.service.WelderService;
import com.gw.sys.dao.SysDictionaryDao;
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
                              Integer firm,Long isNetwork,String gatherNo,String ipPath,Integer model,Integer area,Integer bay){
        PageHelper.startPage(pn,10);
        List<MachineWeldInfo> list=welderService.getList(machineNo,type,grade,status,firm,isNetwork,gatherNo,ipPath,model,area,bay);
        PageInfo page=new PageInfo(list,5);
        return HttpResult.ok(page);
    }

    //列表展示
    @RequestMapping("welderInfosNoPage")
    public HttpResult getListNoPage(String machineNo,Integer type,Integer grade,Integer status,
                              Integer firm,Long isNetwork,String gatherNo,String ipPath,Integer model,Integer area,Integer bay){

        List<MachineWeldInfo> list=welderService.getList(machineNo,type,grade,status,firm,isNetwork,gatherNo,ipPath,model,area,bay);

        return HttpResult.ok(list);
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
                                  Integer firm,Long isNetwork,String gatherNo,String ipPath,Integer model,Integer area,Integer bay){
        HttpResult result=new HttpResult();
        List<MachineWeldInfo> list = welderService.getList(machineNo,type,grade,status,firm,isNetwork,gatherNo,ipPath,model,area,bay);
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

            if (ObjectUtils.isEmpty(machineWeldInfo.getMachineNo())){

                machineWeldInfo.setMachineNo("");
            }
            getMachineNoCell.setCellValue(machineWeldInfo.getMachineNo());

            Cell valueNameCell=row.createCell(1);

            if (ObjectUtils.isEmpty(machineWeldInfo.getSysDictionary().getValueName())){

                machineWeldInfo.getSysDictionary().setValueName("");
            }
            valueNameCell.setCellValue(machineWeldInfo.getSysDictionary().getValueName());

            Cell createTimeCell=row.createCell(2);

            if (ObjectUtils.isEmpty(machineWeldInfo.getCreateTime())){

                machineWeldInfo.setCreateTime("");
            }
            createTimeCell.setCellValue(machineWeldInfo.getCreateTime());

            Cell nameCell=row.createCell(3);

            if (ObjectUtils.isEmpty(machineWeldInfo.getSysDept().getName())){

                machineWeldInfo.getSysDept().setName("");
            }
            nameCell.setCellValue(machineWeldInfo.getSysDept().getName());

            Cell getValueNamesCell=row.createCell(4);

            if (ObjectUtils.isEmpty(machineWeldInfo.getSysDictionary().getValueNames())){

                machineWeldInfo.getSysDictionary().setValueNames("");
            }
            getValueNamesCell.setCellValue(machineWeldInfo.getSysDictionary().getValueNames());

            Cell getValueNamessCell=row.createCell(5);

            if (ObjectUtils.isEmpty(machineWeldInfo.getSysDictionary().getValueNamess())){

                machineWeldInfo.getSysDictionary().setValueNamess("");
            }
            getValueNamessCell.setCellValue(machineWeldInfo.getSysDictionary().getValueNamess());

            Cell getIsNetworkCell=row.createCell(6);
            getIsNetworkCell.setCellValue(machineWeldInfo.getIsNetwork()==0?"是":"否");

            Cell getGatherNoCell=row.createCell(7);

            if (ObjectUtils.isEmpty(machineWeldInfo.getMachineGatherInfo().getGatherNo())){

                machineWeldInfo.getMachineGatherInfo().setGatherNo("");
            }
            getGatherNoCell.setCellValue(machineWeldInfo.getMachineGatherInfo().getGatherNo());

            Cell positionCell=row.createCell(8);
            positionCell.setCellValue("");

            Cell ipPathCell=row.createCell(9);
            ipPathCell.setCellValue("");

            Cell getValueNamesssCell=row.createCell(10);

            if (ObjectUtils.isEmpty(machineWeldInfo.getSysDictionary().getValueNamesss())){

                machineWeldInfo.getSysDictionary().setValueNamesss("");
            }
            getValueNamesssCell.setCellValue(machineWeldInfo.getSysDictionary().getValueNamesss());

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

                String str = obs[0].toString();

                str = str.substring(0,str.indexOf("."));

                machineWeldInfo.setMachineNo(str);

                String type=(String) obs[1];

                Byte id = welderService.getTypeId(type,ConstantInfo.DICTIONARY_WELD_TYPE_FLAG);

                machineWeldInfo.setType(id);

                machineWeldInfo.setCreateTime((String) obs[2]);

                String deptName=(String) obs[3];

                Long deptId=welderService.getDeptId(deptName);

                machineWeldInfo.setDeptId(deptId);

                String status=(String) obs[4];

                Byte statusId= welderService.getStatusId(status);

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

                String machineNo = obs[7].toString();

                //将采集序号转成字符串数组
                String [] machineNos = machineNo.split(",");

                List<String> list = new ArrayList<>();

                for (int j = 0; j < machineNos.length ; j++) {

                    str = machineNos[j].substring(0,machineNos[j].indexOf("."));

                    String a = String.valueOf(welderService.getGid(str));

                    list.add(a);
                }

                StringBuffer sb = new StringBuffer();

                for(int k = 0; k < list.size(); k++){

                    sb. append(list.get(k)).append(",");
                }
               String s = sb.toString().substring(0,sb.length()-1);

                machineWeldInfo.setGId(s);

                String model=(String) obs[10];

                Byte modelId=welderService.getModelId(model);

                machineWeldInfo.setModel(modelId);

                String area = (String) obs[11];

                if(!ObjectUtils.isEmpty(area)){

                    Long areaId = welderService.getTypeId(area,ConstantInfo.AREA_FLAG).longValue();

                    if (!ObjectUtils.isEmpty(areaId)){

                        machineWeldInfo.setArea(areaId);
                    }

                }

                String bay = (String) obs[12];

                if (!ObjectUtils.isEmpty(bay)){

                    Long bayId = welderService.getTypeId(bay,ConstantInfo.BAY_FLAG).longValue();

                    if (!ObjectUtils.isEmpty(bayId)){

                        machineWeldInfo.setBay(bayId);
                    }
                }

                //把对象放到list
                machineWeldInfoArrayList.add(machineWeldInfo);
            }
            //保存
            welderService.importExcel(machineWeldInfoArrayList);
            return HttpResult.ok("导入成功！");
        }catch (Exception e){
            e.printStackTrace();
            return HttpResult.error("导入失败！");
        }
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

    /**
     * @Date 2021/7/29 13:20
     * @Description  根据部门id查询设备信息列表
     * @Params id 部门id
     */
    @RequestMapping(value = "getWeldInfosByDeptId")
    public HttpResult getWeldInfos(@RequestParam(value="pn",defaultValue = "1") Integer pn,Long id){

        PageHelper.startPage(pn,10);

        List<MachineWeldInfo> list = welderService.getWeldInfos(id);

        PageInfo page=new PageInfo(list,5);
        return HttpResult.ok(page);
    }

}
