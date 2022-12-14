package com.gw.service.process.dispatch.service;

import com.gw.entities.GatherAndFirmInfo;
import com.gw.entities.IdListVO;
import com.gw.entities.TaskInfo;

import java.util.List;
import java.util.Map;

public interface DispatchService {

    /**
     * @Date 2021/5/27 11:25
     * @Description 获取任务列表
     * @Params grade班组id      taskStatus任务状态id
     */
    public List<TaskInfo> queryTaskList(List<Integer> grade, Integer taskStatus);

    /**
     * @Date 2021/5/27 11:25
     * @Description 通过用户名和密码获取作业区列表, 另外完成对任务状态、作业区所属班组的获取
     * @Params userName 用户名 , passWord 密码
     */
    public Map<String, Object> queryConditionsList(String userName, String passWord);

    /**
     * @Date 2021/5/28 11:16
     * @Description 获取任务等级信息 供前端新增任务时，根据任务等级列表选择不同的任务等级
     * @Params
     */
    public Map<String, Object> queryTaskRateList();

    /**
     * @Date 2021/5/28 11:24
     * @Description 新增任务信息
     * @Params taskInfo 任务信息实体类
     */
    public void addTaskInfo(TaskInfo taskInfo);

    /**
     * @Date 2021/5/28 11:44
     * @Description 根据任务表主键查询任务信息
     * @Params id 任务表主键
     */
    public TaskInfo queryTaskInfoById(Long id);

    /**
     * @Date 2021/5/28 11:24
     * @Description 修改任务信息
     * @Params id 任务信息表 task_info主键
     */
    public void updateTaskInfo(TaskInfo taskInfo);

    /**
     * @Date 2021/5/28 12:51
     * @Description 根据任务主键 删除任务信息
     * @Params id 任务信息表 task_info主键
     */
    public void deleteTaskInfoById(Long id);

    /**
     * @Date 2021/5/28 12:59
     * @Description 批量删除任务信息
     * @Params idList  任务主键列表
     */
    public void deleteTaskInfoByIdList(IdListVO idList);

    /**
     * @Date 2021/6/1 17:47
     * @Description 获取所有任务状态为 进行中 的任务编号
     * @Params
     */
    public List<TaskInfo> queryAllTaskNosOfStatusWorking();

    /**
     * @Date 2021/5/28 13:21
     * @Description 批量修改任务状态, 将状态修改为已完成 即 批量完成
     * @Params idList  任务主键列表
     */
    public void updateTaskInfoStatusByIdList(List<Long> idList);

    /**
     * @Date 2021/5/28 16:30
     * @Description 任务状态变更
     * @Params taskInfo 任务信息
     */
    public List<GatherAndFirmInfo> taskStatusChange(TaskInfo taskInfo);

    /**
     * @Date 2021/5/31 9:41
     * @Description 导入Excel
     * @Params
     */
    public TaskInfo importExcel(TaskInfo data);

    /**
     * @Date 2021/6/1 9:52
     * @Description 评论信息插入
     * @Params id 任务Id   comments 评论信息
     */
    public void insertComments(Long id, String comments, int start);


    /**
     * @Date 2021/6/29 9:45
     * @Description 用户为管理员，查询所有的区以及区下班组
     * @Params username  用户名   password 密码
     */
    public Map<String, Object> getWorkSpaceAndGradeInfo(String username, String password);


    /**
     * @Date 2021/7/13 17:33
     * @Description 获取历史曲线中任务id, 编号列表
     * @Params
     */
    public List<TaskInfo> getIdAndTaskNoOfTaskInfos(List<Integer> taskStatus);

    /**
     * @Date 2021/8/11 15:50
     * @Description 查询班组id列表
     * @Params
     */
    public List<Integer> getGradeIds(Integer gradeId);

    /**
     * @Date 2021/10/12 10:33
     * @Description 批量插入数据
     * @Params
     */
    public void addTaskInfos(List<TaskInfo> list);
}
