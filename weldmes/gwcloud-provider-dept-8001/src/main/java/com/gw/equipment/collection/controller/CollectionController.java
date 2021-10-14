package com.gw.equipment.collection.controller;
import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.config.CollectionExcelListener;
import com.gw.config.DownExcel;
import com.gw.entities.MachineGatherInfo;
import com.gw.equipment.collection.dao.CollectionDao;
import com.gw.equipment.collection.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
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
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
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
     * @Description excel导出
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

    /**
     * @Date 2021/10/14 13:51
     * @Description excel导入
     * @Params
     */
    @PostMapping(value = "importExcel")
    public HttpResult importExcel(@RequestParam("file") MultipartFile file) {

        try{

            EasyExcel.read(file.getInputStream(), MachineGatherInfo.class, new CollectionExcelListener(collectionService)).sheet().doRead();

            return HttpResult.ok("Excel导入成功");

        }catch (Exception e) {

            return HttpResult.error("Excel导入失败");
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

