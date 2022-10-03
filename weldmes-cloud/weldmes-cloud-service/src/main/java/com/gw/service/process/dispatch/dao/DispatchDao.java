package com.gw.service.process.dispatch.dao;

import com.gw.entities.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DispatchDao {

    /**
     * @Date 2021/5/27 13:24
     * @Description 通过用户名和密码获取该用户的部门Id
     * @Params userName 用户名 , passWord 密码
     */
    public Long queryUserDeptId(@Param("userName")String userName,@Param("passWord")String passWord);

    /**
     * @Date 2021/5/27 11:25
     * @Description 获取任务列表
     * @Params
     */
    public List<TaskInfo> queryTaskList(@Param("grade")Integer grade,@Param("taskStatus")Integer taskStatus);

    /**
     * @Date 2021/5/27 11:25
     * @Description 通过所属作业区查询出部门Id
     * @Params workArea 所属工作区
     */
    public Long queryDeptIdByWorkArea(@Param("workArea")String workArea);

    /**
     * @Date 2021/5/27 11:25
     * @Description 通过 部门Id 获取任务列表
     * @Params deptIdList 部门Id列表
     */
    public List<TaskInfo> queryTaskListByDeptIdList(@Param("deptId")Long deptId);

    /**
     * @Date 2021/5/27 11:25
     * @Description 通过 父级Id 获取任务列表
     * @Params deptId 父级Id
     */
    public List<TaskInfo> queryTaskListByDeptId(@Param("deptId")Long deptId);

    /**
     * @Date 2021/5/27 11:25
     * @Description 通过部门Id 获取作业区
     * @Params deptId 部门Id
     */
    public SysDept queryDeptNameListById(@Param("deptId")Long deptId);

    /**
     * @Date 2021/5/27 14:45
     * @Description 通过 父级Id 获取到作业区下所有的班组
     * @Params deptId 父级Id
     */
    public List<SysDept> queryGradeList(@Param("deptId")Long deptId);

    /**
     * @Date 2021/5/27 16:18
     * @Description 通过 类型 获取 字典表信息
     * @Params type 类型
     */
    public List<SysDictionary> queryDictionInfos(@Param("type")String type);

    /**
     * @Date 2021/5/28 11:28
     * @Description 新增任务信息
     * @Params
     */
    public void addTaskInfo(@Param("taskInfo")TaskInfo taskInfo);

    /**
     * @Date 2021/5/28 11:52
     * @Description 根据任务Id 查询任务信息
     * @Params id 任务信息主键
     */
    public TaskInfo queryTaskInfoById(@Param("id")Long id);

    /**
     * @Date 2021/5/28 12:41
     * @Description 修改任务信息
     * @Params  taskInfo 任务信息实体
     */
    public void updateTaskInfo(@Param("taskInfo")TaskInfo taskInfo);

    /**
     * @Date 2021/5/28 12:54
     * @Description 根据任务信息主键删除任务信息
     * @Params id 任务表主键
     */
    public void deleteTaskInfoById(@Param("id")Long id);

    /**
     * @Date 2021/5/28 13:03
     * @Description 批量删除任务信息
     * @Params idList 任务主键列表
     */
    public void deleteTaskInfoByIdList(@Param("idList")List<Long> idList);

    /**
     * @Date 2021/5/28 13:29
     * @Description 批量修改任务状态 批量完成
     * @Params idList 任务id列表
     */
    public void updateTaskInfoStatusByIdList(@Param("idList")List<Long> idList,@Param("status")int status,@Param("time")String time);

    /**
     * @Date 2021/5/31 11:02
     * @Description 通过 任务等级的描述 查询 任务等级Id
     * @Params taskGradeValueName 任务等级的描述
     */
    public Long queryTaskGradeIdByValueName(String taskGradeValueName);

    /**
     * @Date 2021/5/31 18:43
     * @Description 批量导入任务信息
     * @Params taskInfos 任务信息列表
     */
    public void addTaskInfoList(@Param("taskInfos")List<TaskInfo> taskInfos);

    /**
     * @Date 2021/6/1 9:55
     * @Description 根据 任务id 插入 评论信息
     * @Params id 任务主键   comments 评论信息
     */
    public void addCommentInfo(@Param("id")Long id,@Param("comments")String comments,@Param("start")int start);

    /**
     * @Date 2021/6/1 15:17
     * @Description taskClaimInfo入库操作
     * @Params
     */
    public void addTaskClaimInfo(@Param("taskClaimInfo") TaskClaim taskClaimInfo);

    /**
     * @Date 2021/6/1 17:50
     * @Description 获取所有任务状态为 进行中 的任务编号
     * @Params
     */
    public List<TaskInfo> queryAllTaskNosOfStatusWorking();

    /**
     * @Date 2021/6/30 11:19
     * @Description  通过 类型 获取 字典表信息
     * @Params  type 类型列表
     */
    public List<SysDictionary> queryDictionaryInfos(@Param("type")String type);

    /**
     * @Date 2021/7/13 17:33
     * @Description  获取历史曲线中任务id,编号列表
     * @Params
     */
    public List<TaskInfo> selectIdAndTaskNoOfTaskInfos(List<Integer> taskStatus);
}
