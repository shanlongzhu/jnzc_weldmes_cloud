package com.gw.process.dispatch.service.impl;

import com.gw.common.DateTimeUtils;
import com.gw.common.DictionaryEnum;
import com.gw.entities.TaskClaim;
import com.gw.entities.TaskInfo;
import com.gw.entities.WeldClaimTaskInfo;
import com.gw.entities.WelderInfo;
import com.gw.process.dispatch.dao.DispatchDao;
import com.gw.process.dispatch.dao.TaskClaimDao;
import com.gw.process.dispatch.service.TaskClaimService;
import com.gw.process.solderer.dao.SoldererDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Autowired
    SoldererDao soldererDao;

    /**
     * @Date 2021/7/19 14:50
     * @Description 根据焊机id查询最新一条焊机任务绑定信息
     * @Params id 焊机id
     */
    @Override
    public WeldClaimTaskInfo getWeldClaimTaskInfo(Long weldeId, int weldType) {

        //根据焊机id查询焊机任务绑定信息列表
        List<TaskClaim> taskClaims = taskClaimDao.selectTaskClaimInfoById(weldeId, weldType);

        WeldClaimTaskInfo weldClaimTaskInfo = new WeldClaimTaskInfo();

        if (!ObjectUtils.isEmpty(taskClaims)) {

            //根据焊机任务绑定信息查询具体绑定信息
            weldClaimTaskInfo = taskClaimDao.selectWeldClaimTaskInfoByTaskClaim(taskClaims.get(0));
        }

        return weldClaimTaskInfo;
    }

    /**
     * @Date 2021/7/19 16:14
     * @Description 根据焊工id查询任务工单信息列表
     * @Params welderId 焊工id
     */
    @Override
    public Set<TaskInfo> getTaskInfoByWelderId(Long welderId) {

        List<TaskInfo> list = taskClaimDao.selectTaskInfoByWelderId(welderId);

        //根据焊工id查询焊工信息   获取焊工部门id
        List<WelderInfo> welders = soldererDao.getById(welderId);

        //未领取的任务信息
        List<TaskInfo> temp = taskClaimDao.selectTaskInfoByStatus(welders.get(0).getDeptId());

        list.addAll(temp);

        //去重
        Set<TaskInfo> set = new HashSet<>(list);

        return set;
    }

    /**
     * @Date 2021/7/22 14:36
     * @Description 插入焊机任务绑定信息
     * @Params taskClaim 焊机任务绑定信息
     */
    @Transactional
    @Override
    public void addTaskClaimInfo(TaskClaim taskClaim) {

        //获取当前时间
        String time = DateTimeUtils.getNowDateTime();

        taskClaim.setClaimTime(time);

        //领取表信息新增
        taskClaimDao.insertTaskClaimInfo(taskClaim);

        //判断任务表中该数据的实际开始时间是否为空
        TaskInfo taskInfo = dispatchDao.queryTaskInfoById(taskClaim.getTaskId());

        if (ObjectUtils.isEmpty(taskInfo.getRealityStarttime())) {

            taskInfo.setRealityStarttime(taskClaim.getClaimTime());
        }

        taskInfo.setStatus(DictionaryEnum.TASK_STATUS_WORKING.getId());

        taskInfo.setWelderId(taskClaim.getWelderId());

        dispatchDao.updateTaskInfo(taskInfo);
    }
}
