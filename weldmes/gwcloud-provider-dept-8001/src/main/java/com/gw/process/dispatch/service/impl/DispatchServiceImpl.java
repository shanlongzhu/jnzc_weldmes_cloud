package com.gw.process.dispatch.service.impl;

import com.gw.common.DateTimeUtils;
import com.gw.common.DictionaryEnum;
import com.gw.entities.*;
import com.gw.equipment.welder.dao.WelderDao;
import com.gw.process.dispatch.dao.DispatchDao;
import com.gw.process.dispatch.dao.TaskClaimDao;
import com.gw.process.dispatch.service.DispatchService;
import com.gw.process.solderer.dao.SoldererDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhanghan
 * @Date 2021/5/27 11:30
 * @Description 派工任务管理业务实现层
 */

@Service
public class DispatchServiceImpl implements DispatchService {

    @Autowired
    DispatchDao dispatchDao;

    @Autowired
    WelderDao welderDao;

    @Autowired
    TaskClaimDao taskClaimDao;

    @Autowired
    SoldererDao soldererDao;


    /**
     * @Date 2021/5/27 11:30
     * @Description 获取任务列表
     * @Params
     */
    @Override
    public List<TaskInfo> queryTaskList(List<Integer> grades, Integer taskStatus) {

        List<TaskInfo> taskInfos = new ArrayList<>();

        if (ObjectUtils.isEmpty(grades)) {

            Integer grade = null;
            //无条件筛选，直接获取任务列表
            List<TaskInfo> list = dispatchDao.queryTaskList(grade, taskStatus);

            return list;
        }

        for (Integer grade : grades) {

            //无条件筛选，直接获取任务列表
            List<TaskInfo> list = dispatchDao.queryTaskList(grade, taskStatus);

            taskInfos.addAll(list);

        }

        return taskInfos;
    }

    /**
     * @Date 2021/5/27 11:31
     * @Description 获取 用户作业区列表 以及 作业区下对应的所属班组
     * @Params userName 用户名 , passWord 密码
     */
    @Override
    public Map<String, Object> queryConditionsList(String userName, String passWord) {

        //通过用户名和密码获取该用户的部门Id
        Long deptId = dispatchDao.queryUserDeptId(userName, passWord);

        Map map = new HashMap<String, Object>();

        if (ObjectUtils.isEmpty(deptId)) {

            map.put("msg", "通过用户名和密码获取该用户的部门Id为空");

            return map;
        }

        //通过部门Id查询一级列表
        SysDept group = dispatchDao.queryDeptNameListById(deptId);

        //通过部门Id查询二级列表
        List<SysDept> deptList = dispatchDao.queryGradeList(deptId);

        for (SysDept sysDept : deptList) {

            //查询三级列表
            List<SysDept> workAreaList = dispatchDao.queryGradeList(sysDept.getId());

            if (!ObjectUtils.isEmpty(workAreaList)) {

                for (SysDept workArea : workAreaList) {

                    //查询四级列表
                    List<SysDept> gradeList = dispatchDao.queryGradeList(workArea.getId());

                    workArea.setList(gradeList);

                }
            }
            sysDept.setList(workAreaList);
        }
        group.setList(deptList);

        List<SysDept> list = new ArrayList<>();

        list.add(group);
        //获取到 任务状态 字符串
        String taskStatus = DictionaryEnum.TASK_STATUS_FINISH.getType();

        //对 任务状态 的所有情况进行查询,供前端任务条件查询时，可根据任务状态对任务进行筛选
        List<SysDictionary> taskStatusList = dispatchDao.queryDictionInfos(taskStatus);

        map.put("workArea", list);   //所属作业区以及班组信息

        map.put("taskStatus", taskStatusList); //任务状态信息

        return map;
    }

    /**
     * @Date 2021/5/28 11:25
     * @Description 获取任务等级列表
     * @Params
     */
    @Override
    public Map<String, Object> queryTaskRateList() {

        String taskRate = DictionaryEnum.TASK_RATE_V1.getType();

        //对任务等级的查询,供前端新增任务时，根据任务等级列表选择不同的任务等级
        List<SysDictionary> taskRateList = dispatchDao.queryDictionInfos(taskRate);

        Map map = new HashMap<String, Object>();

        map.put("taskRateList", taskRateList); //任务等级

        return map;
    }

    /**
     * @Date 2021/5/28 11:26
     * @Description 新增任务信息
     * @Params
     */
    @Override
    public void addTaskInfo(TaskInfo taskInfo) {

        //获取当前系统时间
        String time = DateTimeUtils.getNowDateTime();

        taskInfo.setCreateTime(time);

        if (ObjectUtils.isEmpty(taskInfo.getPlanStarttime())) {

            taskInfo.setPlanStarttime(null);
        }

        if (ObjectUtils.isEmpty(taskInfo.getPlanEndtime())) {

            taskInfo.setPlanEndtime(null);
        }
        dispatchDao.addTaskInfo(taskInfo);

    }

    /**
     * @Date 2021/5/28 11:43
     * @Description 根据任务表主键Id 查询任务信息
     * @Params id 任务表主键
     */
    @Override
    public TaskInfo queryTaskInfoById(Long id) {

        TaskInfo taskInfo = dispatchDao.queryTaskInfoById(id);

        return taskInfo;
    }

    /**
     * @Date 2021/5/28 11:43
     * @Description 修改任务信息
     * @Params taskInfo 任务信息
     */
    @Override
    public void updateTaskInfo(TaskInfo taskInfo) {

        dispatchDao.updateTaskInfo(taskInfo);

    }

    /**
     * @Date 2021/5/28 12:53
     * @Description 根据任务主键 删除任务信息
     * @Params id 任务信息主键
     */
    @Override
    public void deleteTaskInfoById(Long id) {

        dispatchDao.deleteTaskInfoById(id);

    }

    /**
     * @Date 2021/5/28 13:01
     * @Description 根据任务主键列表 批量删除任务信息
     * @Params
     */
    @Override
    public void deleteTaskInfoByIdList(IdListVO idList) {

        List<Long> ids = idList.getIdList();

        dispatchDao.deleteTaskInfoByIdList(ids);

    }

    /**
     * @Date 2021/6/2 9:56
     * @Description 获取所有任务状态为 进行中 的任务编号
     * @Params
     */
    @Override
    public List<TaskInfo> queryAllTaskNosOfStatusWorking() {

        //获取到所有的任务状态为 进行中 的任务编号
        List<TaskInfo> taskInfos = dispatchDao.queryAllTaskNosOfStatusWorking();

        return taskInfos;
    }

    /**
     * @Date 2021/5/28 13:24
     * @Description 批量修改任务信息状态, 将状态信息修改为字典表中任务状态为已完成的主键 即 批量完成
     * @Params idList 任务主键列表
     */
    @Override
    public void updateTaskInfoStatusByIdList(List<Long> idList) {

        //获取到任务完成的主键
        int id = DictionaryEnum.TASK_STATUS_FINISH.getId();

        //获取当前时间
        String time = DateTimeUtils.getNowDateTime();

        dispatchDao.updateTaskInfoStatusByIdList(idList, id, time);
    }

    /**
     * @Date 2021/5/28 16:32
     * @Description 根据任务id以及任务当前状态 修改该条任务状态 未领取-->进行中-->已完成
     * @Params taskInfo 任务信息实体类  只需传入 任务id 任务当前状态
     */
    @Override
    public List<GatherAndFirmInfo> taskStatusChange(TaskInfo taskInfo) {

        //获取到 已完成 的任务主键
        int status = DictionaryEnum.TASK_STATUS_FINISH.getId();

        //获取当前时间
        String time = DateTimeUtils.getNowDateTime();

        taskInfo.setStatus(status);

        taskInfo.setRealityEndtime(time);

        //入库操作  修改任务状态
        dispatchDao.updateTaskInfo(taskInfo);

        //根据任务id拿到焊机id列表
        List<GatherAndFirmInfo> firmAndGatherNoInfos = taskClaimDao.selectFirmAndGatherNoInfosByTaskId(taskInfo.getId());

        return firmAndGatherNoInfos;
    }

    /**
     * @Date 2021/5/31 9:42
     * @Description 将Excel中的数据导入
     * @Params
     */
    @Override
    public TaskInfo importExcel(TaskInfo data) {

        //任务等级
        if (!ObjectUtils.isEmpty(data.getGradeIdToStr())) {

            Long gradeId = dispatchDao.queryTaskGradeIdByValueName(data.getGradeIdToStr());

            if (!ObjectUtils.isEmpty(gradeId)) {

                data.setGrade(gradeId);
            }
        }

        //所属班组
        if (!ObjectUtils.isEmpty(data.getDeptName())) {

            Long deptId = dispatchDao.queryDeptIdByWorkArea(data.getDeptName());

            if (!ObjectUtils.isEmpty(deptId)) {

                data.setDeptId(deptId);
            }
        }

        //焊工id
        if (!ObjectUtils.isEmpty(data.getWelderName())) {

            Long welderId = soldererDao.selectWelderInfoByWelderNo(data.getWelderName());

            if (!ObjectUtils.isEmpty(welderId)) {

                data.setWelderId(welderId);
            }
        }

        //评价等级
        if (!ObjectUtils.isEmpty(data.getEvaluateStarsIdToStr())) {

            Long evaluateStarsId = dispatchDao.queryTaskGradeIdByValueName(data.getEvaluateStarsIdToStr());

            if (!ObjectUtils.isEmpty(evaluateStarsId)) {

                data.setWelderId(evaluateStarsId);
            }
        }

        return data;

    }

    /**
     * @Date 2021/6/1 9:54
     * @Description 根据任务id  插入评论信息
     * @Params id 任务主键   comments 任务评价
     */
    @Override
    public void insertComments(Long id, String comments, int start) {

        dispatchDao.addCommentInfo(id, comments, start);
    }

    /**
     * @Date 2021/6/29 10:17
     * @Description 用户为管理员时  获取作业区以及列表
     * @Params
     */
    @Override
    public Map<String, Object> getWorkSpaceAndGradeInfo(String username, String password) {

        //通过用户名和密码拿到部门id
        Long deptId = dispatchDao.queryUserDeptId(username, password);

        //通过部门Id查询一级列表
        SysDept group = dispatchDao.queryDeptNameListById(deptId);

        //通过部门Id查询二级列表
        List<SysDept> deptList = dispatchDao.queryGradeList(deptId);

        for (SysDept sysDept : deptList) {

            //查询三级列表
            List<SysDept> workAreaList = dispatchDao.queryGradeList(sysDept.getId());

            if (!ObjectUtils.isEmpty(workAreaList)) {

                for (SysDept workArea : workAreaList) {

                    //查询四级列表
                    List<SysDept> gradeList = dispatchDao.queryGradeList(workArea.getId());

                    workArea.setList(gradeList);

                }
            }
            sysDept.setList(workAreaList);
        }
        group.setList(deptList);

        List<SysDept> workArea = new ArrayList<>();

        workArea.add(group);

        //获取到 任务状态 字符串
        String taskStatus = DictionaryEnum.TASK_STATUS_FINISH.getType();

        //对 任务状态 的所有情况进行查询,供前端任务条件查询时，可根据任务状态对任务进行筛选
        List<SysDictionary> taskStatusList = dispatchDao.queryDictionInfos(taskStatus);

        Map map = new HashMap<String, Object>();

        map.put("workArea", workArea);  //所属作业区以及班组信息

        map.put("taskStatus", taskStatusList); //任务状态信息

        return map;
    }

    /**
     * @Date 2021/7/13 17:33
     * @Description 获取历史曲线中任务id, 编号列表
     * @Params
     */
    @Override
    public List<TaskInfo> getIdAndTaskNoOfTaskInfos(List<Integer> taskStatus) {

        return dispatchDao.selectIdAndTaskNoOfTaskInfos(taskStatus);
    }

    /**
     * @Date 2021/8/11 15:50
     * @Description 查询班组id列表
     * @Params
     */
    @Override
    public List<Integer> getGradeIds(Integer gradeId) {

        List<SysDept> list = dispatchDao.queryGradeList(gradeId.longValue());

        List<Integer> ids = new ArrayList<>();

        if (list.size() == 0) {

            ids.add(gradeId);

            return ids;
        }

        do {
            List<SysDept> nextSysDeptInfos = new ArrayList<>();

            for (SysDept sysDeptInfo : list) {

                List<SysDept> sysDeptList = dispatchDao.queryGradeList(sysDeptInfo.getId());

                nextSysDeptInfos.addAll(sysDeptList);

            }

            if (ObjectUtils.isEmpty(nextSysDeptInfos)) {

                ids = getSysDeptIds(list);

                return ids;
            }

            list.clear();

            list = nextSysDeptInfos;

        } while (!ObjectUtils.isEmpty(list));

        return ids;
    }

    /**
     * @Date 2021/10/11 18:37
     * @Description 批量插入数据
     * @Params
     */
    @Override
    public void addTaskInfos(List<TaskInfo> list) {

        dispatchDao.addTaskInfoList(list);

    }

    /**
     * @Date 2021/8/11 16:18
     * @Description 获取班组id列表
     * @Params
     */
    public List<Integer> getSysDeptIds(List<SysDept> list) {

        List<Integer> gradIds = new ArrayList<>();

        for (SysDept sysDept : list) {

            gradIds.add(sysDept.getId().intValue());

        }

        return gradIds;
    }
}
