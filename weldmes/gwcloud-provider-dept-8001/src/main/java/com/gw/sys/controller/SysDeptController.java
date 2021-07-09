package com.gw.sys.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.DeptTreeInfo;
import com.gw.entities.SysDept;
import com.gw.entities.TaskInfo;
import com.gw.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  组织机构管理控制器
 * @Params
 */
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
    @RequestMapping("dept/getDeptInfos")
    public HttpResult getDictionaryInfos(@RequestParam(value="pn",defaultValue = "1") Integer pn){

        PageHelper.startPage(pn,10);

        List<SysDept> list = sysDeptService.getDeptInfos();

        //将查询结果进行分页
        PageInfo<TaskInfo> page=new PageInfo(list,10);

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
    @RequestMapping("dept/updateDeptInfo")
    public HttpResult updateDeptInfo(@RequestBody SysDept sysDept){

        sysDeptService.updateDeptInfo(sysDept);

        return HttpResult.ok("组织机构信息修改成功!");
    }

    /**
     * @Date 2021/7/8 16:38
     * @Description 根据id删除组织机构信息
     * @Params id 组织机构信息id
     */
    @RequestMapping("dept/delDeptInfo")
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
     * @Date 2021/7/8 16:38
     * @Description 树状图-查询组织机构信息
     * @Params id 组织机构id
     */
    @RequestMapping("dept/getTreeDeptInfo")
    public HttpResult getTreeDeptInfo(Long id){

        List<DeptTreeInfo> list = sysDeptService.getTreeDeptInfos(id);

        return HttpResult.ok(list);
    }


}
