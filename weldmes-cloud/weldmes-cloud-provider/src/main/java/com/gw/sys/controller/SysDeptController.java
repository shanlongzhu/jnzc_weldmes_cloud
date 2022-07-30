package com.gw.sys.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.SysDept;
import com.gw.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class SysDeptController {

    @Autowired
    SysDeptService sysDeptService;

    /**
     * @Date 2021/7/8 16:38
     * @Description 查询组织机构信息列表
     * @Params
     */
    @RequestMapping(value = "dept/getDeptInfos")
    public HttpResult getDictionaryInfos(@RequestParam(value="pn",defaultValue = "1") Integer pn,
                                         @RequestParam(value="id",defaultValue = "1")Long id){

        PageHelper.startPage(pn,10);

        List<SysDept> list = sysDeptService.getDeptInfos(id);

        //将查询结果进行分页
        PageInfo<SysDept> page=new PageInfo<>(list,10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/7/8 16:38
     * @Description 根据id查询组织机构信息
     * @Params id 组织机构id
     */
    @RequestMapping("dept/getDeptInfoById")
    public HttpResult getDeptInfoById(Long id){

        SysDept sysDept = sysDeptService.getDeptInfoById(id);

        return HttpResult.ok(sysDept);
    }

    /**
     * @Date 2021/7/8 16:38
     * @Description 修改组织机构信息
     * @Params sysDept 组织机构信息
     */
    @RequestMapping(value = "dept/updateDeptInfo",method = RequestMethod.PUT)
    public HttpResult updateDeptInfo(@RequestBody SysDept sysDept){

        sysDeptService.updateDeptInfo(sysDept);

        return HttpResult.ok("组织机构信息修改成功!");
    }

    /**
     * @Date 2021/7/8 16:38
     * @Description 根据id删除组织机构信息
     * @Params id 组织机构信息id
     */
    @RequestMapping(value = "dept/delDeptInfo",method = RequestMethod.DELETE)
    public HttpResult delDeptInfo(Long id){

        sysDeptService.delDeptInfoById(id);

        return HttpResult.ok("组织机构信息删除成功!");
    }

    /**
     * @Date 2021/7/8 16:38
     * @Description 新增组织机构信息
     * @Params sysDept 组织机构信息
     */
    @RequestMapping("dept/addDeptInfo")
    public HttpResult addDeptInfo(@RequestBody SysDept sysDept){

        sysDeptService.addDeptInfo(sysDept);

        return HttpResult.ok("组织机构信息新增成功!");
    }

    /**
     * @Date 2021/7/13 13:28
     * @Description  根据部门id以及部门名称筛选信息列表
     * @Params id 部门id   name  部门名称
     */
    @RequestMapping("dept/getDeptInfosByDeptIdAndName")
    public HttpResult getDeptInfosByDeptIdAndName(@RequestParam(value="pn",defaultValue = "1") Integer pn,
                                                  Long id,String name){

        PageHelper.startPage(pn,10);

        List<SysDept> list = sysDeptService.getDeptInfosByIdAndName(id,name);

        //将查询结果进行分页
        PageInfo<SysDept> page=new PageInfo<>(list,10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/8/27 11:09
     * @Description 获取到作业区层级信息
     * @Params
     */
    @RequestMapping(value = "dept/getDeptWorkInfos",method = RequestMethod.GET)
    public HttpResult getDeptWorkInfos(){

        List<SysDept> list = sysDeptService.getDeptWorkInfos();

        return HttpResult.ok(list);
    }

}
