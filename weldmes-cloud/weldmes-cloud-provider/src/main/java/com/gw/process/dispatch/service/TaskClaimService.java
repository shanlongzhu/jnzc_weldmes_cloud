package com.gw.process.dispatch.service;

import com.gw.entities.TaskClaim;
import com.gw.entities.TaskInfo;
import com.gw.entities.WeldClaimTaskInfo;

import java.util.List;
import java.util.Set;

public interface TaskClaimService {


    /**
     * @Date 2021/7/19 15:13
     * @Description 根据焊机id查询焊机任务绑定信息
     * @Params weldeId 焊机id
     */
    public WeldClaimTaskInfo getWeldClaimTaskInfo(Long weldeId,int weldType);

    /**
     * @Date 2021/7/19 16:14
     * @Description  根据焊工id查询任务工单信息列表
     * @Params welderId 焊工id
     */
    public Set<TaskInfo> getTaskInfoByWelderId(Long welderId);

    /**
     * @Date 2021/7/22 14:36
     * @Description 插入焊机任务绑定信息
     * @Params taskClaim 焊机任务绑定信息
     */
    public void addTaskClaimInfo(TaskClaim taskClaim);

}
