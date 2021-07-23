package com.gw.process.dispatch.service.impl;

import com.gw.common.DateTimeUtil;
import com.gw.entities.TaskClaim;
import com.gw.entities.TaskInfo;
import com.gw.entities.WeldClaimTaskInfo;
import com.gw.process.dispatch.dao.DispatchDao;
import com.gw.process.dispatch.dao.TaskClaimDao;
import com.gw.process.dispatch.service.TaskClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/7/19 14:54
 * @Description 任务认领业务实现层
 */
@Service
public class TaskClaimServiceImpl implements TaskClaimService {

    @Autowired
    TaskClaimDao taskClaimDao;

    @Autowired
    DispatchDao dispatchDao;

    /**
     * @Date 2021/7/19 14:50
     * @Description 根据焊机id查询最新一条焊机任务绑定信息
     * @Params id 焊机id
     */
    @Override
    public WeldClaimTaskInfo getWeldClaimTaskInfo(Long weldeId) {

        //根据焊机id查询焊机任务绑定信息列表
        List<TaskClaim> taskClaims =taskClaimDao.selectTaskClaimInfoById(weldeId);

        WeldClaimTaskInfo weldClaimTaskInfo = new WeldClaimTaskInfo();

        if(!ObjectUtils.isEmpty(taskClaims)){

            //根据焊机任务绑定信息查询具体绑定信息
            weldClaimTaskInfo = taskClaimDao.selectWeldClaimTaskInfoByTaskClaim(taskClaims.get(0));
        }

        return weldClaimTaskInfo;
    }

    /**
     * @Date 2021/7/19 16:14
     * @Description  根据焊工id查询任务工单信息列表
     * @Params welderId 焊工id
     */
    @Override
    public List<TaskInfo> getTaskInfoByWelderId(Long welderId) {

        List<TaskInfo> list = taskClaimDao.selectTaskInfoByWelderId(welderId);

        return list;
    }

    /**
     * @Date 2021/7/22 14:36
     * @Description 插入焊机任务绑定信息
     * @Params taskClaim 焊机任务绑定信息
     */
    @Override
    public void addTaskClaimInfo(TaskClaim taskClaim) {

        //获取当前时间
        String time = DateTimeUtil.getCurrentTime();

        taskClaim.setClaimTime(time);

        taskClaimDao.insertTaskClaimInfo(taskClaim);

        //判断任务表中该数据的实际开始时间是否为空
        TaskInfo taskInfo = dispatchDao.queryTaskInfoById(taskClaim.getTaskId());

        if(ObjectUtils.isEmpty(taskInfo.getRealityStarttime())){

            taskInfo.setRealityStarttime(taskClaim.getClaimTime());

            dispatchDao.updateTaskInfo(taskInfo);
        }
    }
}
