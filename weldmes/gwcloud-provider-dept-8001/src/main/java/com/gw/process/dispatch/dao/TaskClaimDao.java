package com.gw.process.dispatch.dao;

import com.gw.entities.TaskClaim;
import com.gw.entities.TaskInfo;
import com.gw.entities.WeldClaimTaskInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/7/19 14:57
 * @Description 任务认领dao层
 */
@Mapper
public interface TaskClaimDao {

    /**
     * @Date 2021/7/19 14:59
     * @Description 根据焊机id查询任务绑定信息列表  倒序
     * @Params id 焊机id
     */
    public List<TaskClaim> selectTaskClaimInfoById(@Param("id")Long id);

    /**
     * @Date 2021/7/19 15:26
     * @Description 根据焊机任务信息中的id  查询具体的焊机任务绑定信息
     * @Params TaskClaimInfo  焊机任务信息
     */
    public WeldClaimTaskInfo selectWeldClaimTaskInfoByTaskClaim(@Param("taskClaim")TaskClaim taskClaim);

    /**
     * @Date 2021/7/19 16:14
     * @Description  根据焊工id查询任务工单信息列表
     * @Params welderId 焊工id
     */
    public List<TaskInfo> selectTaskInfoByWelderId(@Param("welderId")Long welderId);

    /**
     * @Date 2021/6/1 15:17
     * @Description taskClaimInfo入库操作
     * @Params taskClaimInfo 焊机任务绑定信息
     */
    public void insertTaskClaimInfo(@Param("taskClaimInfo") TaskClaim taskClaimInfo);

    /**
     * @Date 2021/7/23 19:34
     * @Description  根据任务状态查询任务信息
     * @Params deptId 焊工部门id
     */
    public List<TaskInfo> selectTaskInfoByStatus(@Param("deptId")Long deptId);

}
