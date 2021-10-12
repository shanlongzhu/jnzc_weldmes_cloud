package com.gw.process.dispatch.controller;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.ConstantInfo;
import com.gw.common.HttpResult;
import com.gw.config.DownExcel;
import com.gw.config.TaskExcelListener;
import com.gw.entities.*;
import com.gw.process.dispatch.dao.DispatchDao;
import com.gw.process.dispatch.service.DispatchService;

import org.apache.shiro.SecurityUtils;

import org.apache.shiro.subject.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhanghan
 * @Date 2021/5/27 10:44
 * @Description 派工任务管理控制器
 */
@CrossOrigin
@RestController
public class DispatchController {

    @Autowired
    DispatchService dispatchService;

    @Autowired
    DispatchDao dispatchDao;

    /**
     * @Date 2021/5/27 11:14
     * @Description 通过用户名和密码获取该用户的部门Id,再通过部门Id获取所属作业区列表
     *              另外完成对作业区下所属班组的查询和任务状态的查询
     * @Params
     */
    @RequestMapping(value = "task/conditions")
    public HttpResult queryConditionsListController(){

        Subject currentUser = SecurityUtils.getSubject();

        UserLoginInfo userInfo = (UserLoginInfo)currentUser.getPrincipal();

        if(ObjectUtils.isEmpty(userInfo)){
            return HttpResult.ok();
        }

        if(ObjectUtils.isEmpty(userInfo.getRoles())){
            return HttpResult.ok();
        }

        if(userInfo.getRoles().get(0).equals(ConstantInfo.ADMIN_FLAG)){

            Map<String,Object> map= dispatchService.getWorkSpaceAndGradeInfo(userInfo.getUserName(),userInfo.getPassWord());
            return HttpResult.ok(map);
        }

        Map<String,Object> map= dispatchService.queryConditionsList(userInfo.getUserName(),userInfo.getPassWord());

        return HttpResult.ok(map);
    }

    /**
     * @Date 2021/5/27 11:08
     * @Description 获取任务列表
     * @Params
     */
    @RequestMapping(value = "task/list")
    public HttpResult queryTaskListController(@RequestParam(value="pn",defaultValue = "1") Integer pn,
                                              @RequestParam(value="size",defaultValue = "10") Integer size,
                                              Integer grade,Integer taskStatus){

        List<Integer> gradIds = new ArrayList<>();

        if(!ObjectUtils.isEmpty(grade)){

            //获取班组id列表
            gradIds = dispatchService.getGradeIds(grade);

        }

        PageHelper.startPage(pn,size);

        //获取任务列表
        List<TaskInfo> taskInfos = dispatchService.queryTaskList(gradIds,taskStatus);

        //将查询结果进行分页
        PageInfo<TaskInfo> page = new PageInfo(taskInfos,10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/5/28 10:52
     * @Description 获取任务等级接口
     * @Params
     */
    @RequestMapping(value = "task/getTaskRateList")
    public HttpResult addTaskInfoController(){

        //获取任务等级
        Map<String,Object> map=dispatchService.queryTaskRateList();

        return HttpResult.ok(map);
    }


    /**
     * @Date 2021/5/28 10:40
     * @Description 任务新增接口
     * @Params taskInfo任务信息
     */
    @RequestMapping(value = "task/addTaskInfo")
    public HttpResult addTaskInfoController(@RequestBody TaskInfo taskInfo){


        dispatchService.addTaskInfo(taskInfo);

        return HttpResult.ok("任务信息新增成功");
    }

    /**
     * @Date 2021/5/28 11:48
     * @Description 根据任务id 查询任务信息
     * @Params  id 任务id
     */
    @RequestMapping(value = "task/queryTaskInfoById")
    public HttpResult queryTaskInfoByIdController(Long id){

        TaskInfo taskInfo=dispatchService.queryTaskInfoById(id);

        if(ObjectUtils.isEmpty(taskInfo)){
            return HttpResult.ok("未查询出任务信息！");
        }

        return HttpResult.ok(taskInfo);
    }

    /**
     * @Date 2021/5/28 10:41
     * @Description 任务修改接口
     * @Params taskInfo任务信息
     */
    @RequestMapping(value = "task/updateTaskInfo",method = RequestMethod.PUT)
    public HttpResult updateTaskInfoController(@RequestBody TaskInfo taskInfo){

        dispatchService.updateTaskInfo(taskInfo);

        return HttpResult.ok("任务信息修改成功");

    }

    /**
     * @Date 2021/5/28 10:41
     * @Description 任务删除接口
     * @Params
     */
    @RequestMapping(value = "task/deleteTaskInfoInfo",method = RequestMethod.DELETE)
    public HttpResult deleteTaskInfoController(@RequestBody TaskInfo taskInfo){

        dispatchService.deleteTaskInfoById(taskInfo.getId());

        return HttpResult.ok("任务信息删除成功");

    }

    /**
     * @Date 2021/5/28 10:40
     * @Description 任务批量删除接口
     * @Params idList 任务主键列表
     */
    @RequestMapping(value = "task/deleteTaskInfoList",method = RequestMethod.DELETE)
    public HttpResult deleteTaskInfoListController(@RequestBody IdListVO idList){

        dispatchService.deleteTaskInfoByIdList(idList);

        return HttpResult.ok("任务信息批量删除成功");
    }

    /**
     * @Date 2021/6/1 17:42
     * @Description 获取所有任务状态为 进行中 的任务编号
     * @Params
     */
    @RequestMapping(value = "task/queryAllTaskNos")
    public HttpResult queryAllTaskNosOfTaskStatusWorkingController(){

        List<TaskInfo> taskInfos = dispatchService.queryAllTaskNosOfStatusWorking();

        return HttpResult.ok(taskInfos);

    }

    /**
     * @Date 2021/5/28 10:44
     * @Description 批量完成接口
     * @Params idList 主键列表
     */
    @RequestMapping(value = "task/finishTaskInfoList",method = RequestMethod.PUT)
    public HttpResult finishTaskInfoListController(@RequestBody IdListVO idList){

        List<Long> ids = idList.getIdList();

        dispatchService.updateTaskInfoStatusByIdList(ids);

        return HttpResult.ok("任务状态信息修改批量完成成功");

    }

    /**
     * @Date 2021/5/28 10:44
     * @Description 任务状态变更接口 即
     * @Params id 任务主键   statusStr 任务当前状态
     */
    @RequestMapping(value = "task/taskStatusChange")
    public HttpResult taskStatusChangeController(@RequestBody TaskInfo taskInfo){

        List<GatherAndFirmInfo> list = dispatchService.taskStatusChange(taskInfo);

        return HttpResult.ok(list);

    }

    /**
     * @Date 2021/5/28 10:45
     * @Description 评价接口
     * @Params id 任务Id   comments 评论信息
     */
    @RequestMapping(value = "task/addComment")
    public HttpResult addCommentController(@RequestBody CommentInfo commentInfo){

        dispatchService.insertComments(commentInfo.getId(),commentInfo.getComments(),commentInfo.getStart());

        return HttpResult.ok("评论添加成功");
    }

    /**
     * @Date 2021/5/28 10:42
     * @Description 文件导入接口
     * @Params
     */
    @RequestMapping(value = "task/importExcel",method = RequestMethod.POST)
    public HttpResult importExcelController(@RequestParam MultipartFile file) throws IOException {

        EasyExcel.read(file.getInputStream(), TaskInfo.class, new TaskExcelListener(dispatchService)).sheet().doRead();

        return HttpResult.ok("Excel导入成功");
    }

    /**
     * @Date 2021/5/28 10:43
     * @Description 文件导出接口
     * @Params
     */
    @RequestMapping(value = "task/exportExcel")
    public HttpResult exportExcelController(HttpServletResponse response,Integer grade,Integer taskStatus) throws IllegalAccessException, IOException, InstantiationException {

        //设置Excel文件名
        String title = URLEncoder.encode("派工任务数据", "UTF-8");

        //设置sheet表格名
        String sheetName = "派工任务";

        //获取任务列表
        List<TaskInfo> list = dispatchDao.queryTaskList(grade,taskStatus);

        //导出为Excel
        DownExcel.download(response,TaskInfo.class,list,sheetName,title);

        return HttpResult.ok("Excel导出成功");
    }

    /**
     * @Date 2021/6/30 11:08
     * @Description 通过 type列表 获取字典表信息
     * @Params types 类型列表
     */
    @RequestMapping(value = "sysDictionary/getInfoByType")
    public HttpResult getDictionaryInfos(@RequestBody DictionaryTypeInfo types){

        Map<String,Object> map = new HashMap<>();

        for (String type : types.getTypes()) {

            List<SysDictionary> list = dispatchDao.queryDictionaryInfos(type);

            map.put(list.get(0).getType(),list);
        }

        return HttpResult.ok(map);
    }

    /**
     * @Date 2021/7/13 17:33
     * @Description  获取历史曲线中任务id,编号列表
     * @Params
     */
    @RequestMapping(value = "task/historyTaskInfos")
    public HttpResult historyTaskInfos(){

        List<TaskInfo> list = dispatchService.getIdAndTaskNoOfTaskInfos();
        return HttpResult.ok(list);
    }
}
