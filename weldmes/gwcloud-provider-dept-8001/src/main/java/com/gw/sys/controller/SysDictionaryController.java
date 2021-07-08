package com.gw.sys.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.SysDictionary;
import com.gw.entities.TaskInfo;
import com.gw.sys.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  字典控制器
 * @Params
 */
@RestController
@CrossOrigin
public class SysDictionaryController {

    @Autowired
    SysDictionaryService sysDictionaryService;

    /**
     * @Date 2021/7/8 13:39
     * @Description  条件查询字典信息列表
     * @Params sysDictionary 字典信息
     */
    @RequestMapping("dictionary/getDictionaryInfos")
    public HttpResult getDictionaryInfos(@RequestParam(value="pn",defaultValue = "1") Integer pn,
                                         String type,String typeName,String value,String valueName){

        PageHelper.startPage(pn,10);

        List<SysDictionary> list = sysDictionaryService.getDictionaryInfos(type,typeName,value,valueName);

        //将查询结果进行分页
        PageInfo<TaskInfo> page=new PageInfo(list,10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/7/8 13:39
     * @Description  根据id查询字典信息
     * @Params id 字典id
     */
    @RequestMapping("dictionary/getDictionaryInfoById")
    public HttpResult getDictionaryInfoById(Long id){

        SysDictionary sysDictionary = sysDictionaryService.getDictionaryInfoById(id);

        return HttpResult.ok(sysDictionary);
    }

    /**
     * @Date 2021/7/8 13:39
     * @Description  修改字典信息
     * @Params sysDictionary 字典信息
     */
    @RequestMapping("dictionary/updateDictionaryInfo")
    public HttpResult updateDictionaryInfo(@RequestBody SysDictionary sysDictionary){

        sysDictionaryService.updateDictionaryInfo(sysDictionary);

        return HttpResult.ok("字典信息修改成功!");
    }

    /**
     * @Date 2021/7/8 13:39
     * @Description  根据id删除字典信息
     * @Params id 字典id
     */
    @RequestMapping("dictionary/delDictionaryInfo")
    public HttpResult delDictionaryInfoById(Long id){

        sysDictionaryService.delDictionaryInfoById(id);

        return HttpResult.ok("字典信息删除成功!");
    }

    /**
     * @Date 2021/7/8 13:39
     * @Description  新增字典信息
     * @Params sysDictionary 字典信息
     */
    @RequestMapping("dictionary/addDictionaryInfo")
    public HttpResult addDictionaryInfoById(@RequestBody SysDictionary sysDictionary){

        sysDictionaryService.addDictionaryInfo(sysDictionary);

        return HttpResult.ok("字典信息新增成功!");
    }




}
