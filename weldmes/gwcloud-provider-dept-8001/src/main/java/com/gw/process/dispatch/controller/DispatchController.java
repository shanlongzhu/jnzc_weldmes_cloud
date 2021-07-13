package com.gw.process.dispatch.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.ConstantInfo;
import com.gw.common.HttpResult;
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

        if(ObjectUtils.isEmpty(userInfo.getRoles())){
            return HttpResult.ok("当前用户尚未分配角色,无法查询作业区信息");
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
    @RequestMapping(value = "task/list" ,method = RequestMethod.GET)
    public HttpResult queryTaskListController(@RequestParam(value="pn",defaultValue = "1") Integer pn,Integer grade,Integer taskStatus){

        PageHelper.startPage(pn,10);

        //获取任务列表
        List<TaskInfo> taskInfos=dispatchService.queryTaskList(grade,taskStatus);

        //将查询结果进行分页
        PageInfo<TaskInfo> page=new PageInfo(taskInfos,10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/6/23 8:42
     * @Description  获取分页任务列表
     * @Params pn 当前页码
     */
    @RequestMapping(value = "task/listT/{pn}")
    public HttpResult queryCurrentPageTaskListController(@PathVariable Integer pn){

        if(ObjectUtils.isEmpty(pn)){
            pn = 1;
        }
        PageHelper.startPage(pn,10);

        //获取任务列表
        List<TaskInfo> taskInfos=dispatchService.queryTaskList(1,1);

        //将查询结果进行分页
        PageInfo<TaskInfo> page=new PageInfo(taskInfos,10);

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
    @RequestMapping(value = "task/updateTaskInfo")
    public HttpResult updateTaskInfoController(@RequestBody TaskInfo taskInfo){

        dispatchService.updateTaskInfo(taskInfo);

        return HttpResult.ok("任务信息修改成功");

    }

    /**
     * @Date 2021/5/28 10:41
     * @Description 任务删除接口
     * @Params
     */
    @RequestMapping(value = "task/deleteTaskInfoInfo")
    public HttpResult deleteTaskInfoController(@RequestBody TaskInfo taskInfo){

        dispatchService.deleteTaskInfoById(taskInfo.getId());

        return HttpResult.ok("任务信息删除成功");

    }

    /**
     * @Date 2021/5/28 10:40
     * @Description 任务批量删除接口
     * @Params idList 任务主键列表
     */
    @RequestMapping(value = "task/deleteTaskInfoList")
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
    @RequestMapping(value = "task/finishTaskInfoList")
    public HttpResult finishTaskInfoListController(@RequestBody IdListVO idList){

        List<Long> ids = idList.getIdList();

        dispatchService.updateTaskInfoStatusByIdList(ids);

        return HttpResult.ok("任务状态信息修改批量完成成功");

    }

    /**
     * @Date 2021/6/1 9:27
     * @Description  工人刷卡领取任务后,需要记录工人领取时间及一些其他信息到 task_claim 表
     *               然后task_info表中 该 任务信息状态 变更为 进行中
     * @Params  taskClaim task_claim实体类
     */
    @RequestMapping(value = "task/getTaskInfo")
    public HttpResult updateTaskStatusAndInsertInfoController(@RequestBody TaskClaim taskClaim){

        HttpResult httpResult = dispatchService.updateTaskStatusAndInsertInfo(taskClaim);

        return httpResult;
    }

    /**
     * @Date 2021/5/28 10:44
     * @Description 任务状态变更接口 即
     * @Params id 任务主键   statusStr 任务当前状态
     */
    @RequestMapping(value = "task/taskStatusChange")
    public HttpResult taskStatusChangeController(@RequestBody TaskInfo taskInfo){

        dispatchService.taskStatusChange(taskInfo);

        return HttpResult.ok("任务状态变更成功");

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
    @RequestMapping(value = "task/importExcel",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public HttpResult importExcelController(@RequestParam("file") MultipartFile file){

        dispatchService.importExcel(file);

        return HttpResult.ok("Excel导入成功");
    }

    /**
     * @Date 2021/5/28 10:43
     * @Description 文件导出接口
     * @Params
     */
    @RequestMapping(value = "task/exportExcel")
    public HttpResult exportExcelController(HttpServletResponse response,Integer grade,Integer taskStatus){

        dispatchService.exportExcel(response,grade,taskStatus);

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
