package com.gw.process.dispatch.controller;

import com.gw.common.HttpResult;
import com.gw.entities.TaskClaim;
import com.gw.entities.TaskInfo;
import com.gw.entities.WeldClaimTaskInfo;
import com.gw.process.dispatch.service.TaskClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @Author zhanghan
 * @Date 2021/7/19 14:49
 * @Description  任务认领控制器
 */
@CrossOrigin
@RestController
public class TaskClaimController {

    @Autowired
    TaskClaimService taskClaimService;
    /**
     * @Date 2021/7/19 14:50
     * @Description 通过焊机id查询焊机任务绑定信息
     * @Params weldeId 焊机id
     */
    @RequestMapping(value = "taskClaim/getWeldClaimTaskInfoById")
    public HttpResult getWeldClaimTaskInfoById(Long weldeId){

        WeldClaimTaskInfo weldClaimTaskInfo = taskClaimService.getWeldClaimTaskInfo(weldeId);

        return HttpResult.ok(weldClaimTaskInfo);
    }

    /**
     * @Date 2021/7/19 16:14
     * @Description  根据焊工id查询任务工单信息列表
     * @Params welderId 焊工id
     */
    @RequestMapping(value = "taskClaim/getTaskInfoByWelderId")
    public HttpResult getTaskInfoByWelderId(Long welderId){

        Set<TaskInfo> list = taskClaimService.getTaskInfoByWelderId(welderId);

        return HttpResult.ok(list);
    }

    /**
     * @Date 2021/7/22 14:32
     * @Description  插入焊机任务绑定信息
     * @Params
     */
    @RequestMapping(value = "taskClaim/addTaskClaimInfo")
    public HttpResult addTaskClaimInfo(@RequestBody TaskClaim taskClaim){

        taskClaimService.addTaskClaimInfo(taskClaim);

        return HttpResult.ok("焊机任务绑定信息添加成功!");
    }

}
