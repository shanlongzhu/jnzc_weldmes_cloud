package com.gw.process.dispatch.service.impl;

import com.gw.common.DateTimeUtil;
import com.gw.common.DictionaryEnum;
import com.gw.common.ExcelUtils;
import com.gw.common.HttpResult;
import com.gw.entities.*;
import com.gw.equipment.welder.dao.WelderDao;
import com.gw.process.dispatch.dao.DispatchDao;
import com.gw.process.dispatch.service.DispatchService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.sql.Timestamp;
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
public class DispatchServiceImpl implements DispatchService{

    @Autowired
    DispatchDao dispatchDao;

    @Autowired
    WelderDao welderDao;


    /**
     * @Date 2021/5/27 11:30
     * @Description 获取任务列表
     * @Params
     */
    @Override
    public List<TaskInfo> queryTaskList(Integer grade,Integer taskStatus) {

        //无条件筛选，直接获取任务列表
        List<TaskInfo> list = dispatchDao.queryTaskList(grade,taskStatus);

        return list;
    }

    /**
     * @Date 2021/5/27 11:31
     * @Description 获取 用户作业区列表 以及 作业区下对应的所属班组
     * @Params userName 用户名 , passWord 密码
     */
    @Override
    public Map<String,Object> queryConditionsList(String userName, String passWord) {

        //通过用户名和密码获取该用户的部门Id
        Long deptId = dispatchDao.queryUserDeptId(userName,passWord);

        Map map = new HashMap<String,Object>();

        if(ObjectUtils.isEmpty(deptId)){

            map.put("msg","通过用户名和密码获取该用户的部门Id为空");

            return map;
        }

        //通过部门Id查询一级列表
        SysDept group = dispatchDao.queryDeptNameListById(deptId);

        //通过部门Id查询二级列表
        List<SysDept> deptList = dispatchDao.queryGradeList(deptId);

        for (SysDept sysDept : deptList) {

            //查询三级列表
            List<SysDept> workAreaList = dispatchDao.queryGradeList(sysDept.getId());

            if(!ObjectUtils.isEmpty(workAreaList)){

                for (SysDept workArea : workAreaList) {

                    //查询四级列表
                    List<SysDept> gradeList = dispatchDao.queryGradeList(sysDept.getId());

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

        map.put("workArea",list);   //所属作业区以及班组信息

        map.put("taskStatus",taskStatusList); //任务状态信息

        return map;
    }

    /**
     * @Date 2021/5/28 11:25
     * @Description  获取任务等级列表
     * @Params
     */
    @Override
    public Map<String, Object> queryTaskRateList() {

        String taskRate = DictionaryEnum.TASK_RATE_V1.getType();

        //对任务等级的查询,供前端新增任务时，根据任务等级列表选择不同的任务等级
        List<SysDictionary> taskRateList = dispatchDao.queryDictionInfos(taskRate);

        Map map = new HashMap<String,Object>();

        map.put("taskRateList",taskRateList); //任务等级

        return map;
    }

    /**
     * @Date 2021/5/28 11:26
     * @Description 新增任务信息
     * @Params
     */
    @Override
    public void addTaskInfo(TaskInfo taskInfo) {

       /* //根据等级获取等级id
        byte grade = dispatchDao.queryTaskGradeIdByValueName(taskInfo.getGradeIdToStr());
        //根据班组获取班组id
        Long deptId = dispatchDao.queryDeptIdByWorkArea(taskInfo.getDeptName());

        taskInfo.setGrade(grade);
        taskInfo.setDeptId(deptId);*/

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

        /*//通过任务等级获取主键
        byte gradeId = dispatchDao.queryTaskGradeIdByValueName(taskInfo.getGradeIdToStr());

        taskInfo.setGrade(gradeId);

        //通过所属班组获取主键
        Long deptId = dispatchDao.queryDeptIdByWorkArea(taskInfo.getDeptName());

        taskInfo.setDeptId(deptId);*/

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
     * @Description  获取所有任务状态为 进行中 的任务编号
     * @Params
     */
    @Override
    public List<TaskInfo> queryAllTaskNosOfStatusWorking() {

        //获取到所有的任务状态为 进行中 的任务编号
        List<TaskInfo> taskInfos= dispatchDao.queryAllTaskNosOfStatusWorking();

        return taskInfos;
    }

    /**
     * @Date 2021/5/28 13:24
     * @Description 批量修改任务信息状态,将状态信息修改为字典表中任务状态为已完成的主键 即 批量完成
     * @Params idList 任务主键列表
     */
    @Override
    public void updateTaskInfoStatusByIdList(List<Long> idList) {

        //获取到任务完成的主键
        int id = DictionaryEnum.TASK_STATUS_FINISH.getId();

        //获取当前时间
        String time = DateTimeUtil.getCurrentTime();

        dispatchDao.updateTaskInfoStatusByIdList(idList,id,time);
    }

    /**
     * @Date 2021/5/28 16:32
     * @Description 根据任务id以及任务当前状态 修改该条任务状态 未领取-->进行中-->已完成
     * @Params  taskInfo 任务信息实体类  只需传入 任务id 任务当前状态
     */
    @Override
    public void taskStatusChange(TaskInfo taskInfo) {

        //任务状态 更改为 已完成
        if(taskInfo.getStatusStr().equals(DictionaryEnum.TASK_STATUS_WORKING.getValueName())){

            //获取到 已完成 的任务主键
            int status = DictionaryEnum.TASK_STATUS_FINISH.getId();

            //获取当前时间
            String time = DateTimeUtil.getCurrentTime();

            taskInfo.setStatus(status);

            taskInfo.setRealityEndtime(time);

            //入库操作  修改任务状态
            dispatchDao.updateTaskInfo(taskInfo);


        }

        /*//任务状态 更改为 进行中
        if(taskInfo.getStatusStr().equals(DictionaryEnum.TASK_STATUS_NO_GET.getValueName())){

            //获取到 进行中 的任务主键
            int status = DictionaryEnum.TASK_STATUS_WORKING.getId();

            //更新任务信息表中状态 将 未领取 修改为 进行中
            taskInfo.setStatus(status);

            //入库操作  修改任务状态
            dispatchDao.updateTaskInfo(taskInfo);

        }*/
    }

    /**
     * @Date 2021/5/28 17:39
     * @Description 数据导入excel
     * @Params
     */
    @Override
    public void exportExcel(HttpServletResponse response,Integer grade,Integer taskStatus) {

        //获取任务列表
        List<TaskInfo> list = dispatchDao.queryTaskList(grade,taskStatus);

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("派工任务数据");

        String[] titles = {"任务编号","任务等级","所属班组","计划开始时间","计划结束时间","实际开始时间","实际结束时间","任务评价","评价等级"};

        Row row = sheet.createRow(0);

        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        for (int i = 0; i < list.size() ; i++) {

            row = sheet.createRow(i+1);

            TaskInfo taskInfo = list.get(i);

            //将实体类中的信息遍历导入Excel
            forEachTaskInfoToCell(row,taskInfo);

        }

        try {
            String fileName = URLEncoder.encode("派工任务数据.xlsx","UTF-8");

            response.setContentType("application/octet-stream");

            response.setHeader("content-disposition","attachment;filename="+fileName);

            response.setHeader("filename",fileName);

            workbook.write(response.getOutputStream());
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    /**
     * @Date 2021/5/31 9:42
     * @Description 将Excel中的数据导入
     * @Params
     */
    @Override
    public void importExcel(MultipartFile uploadFile) {

        //导入数据库
        importExcelMethod(uploadFile);

    }

    /**
     * @Date 2021/6/1 9:54
     * @Description  根据任务id  插入评论信息
     * @Params id 任务主键   comments 任务评价
     */
    @Override
    public void insertComments(Long id, String comments,int start) {

        dispatchDao.addCommentInfo(id,comments,start);
    }

    /**
     * @Date 2021/6/29 10:17
     * @Description  用户为管理员时  获取作业区以及列表
     * @Params
     */
    @Override
    public Map<String, Object> getWorkSpaceAndGradeInfo(String username,String password) {

        //通过用户名和密码拿到部门id
        Long deptId = dispatchDao.queryUserDeptId(username,password);

        //通过部门id获取到作业区的id
        List<SysDept> workArea = dispatchDao.queryGradeList(deptId);

        for (SysDept workSpace : workArea) {

            List<SysDept> gradeList = dispatchDao.queryGradeList(workSpace.getId());

            workSpace.setList(gradeList);
        }

        //获取到 任务状态 字符串
        String taskStatus = DictionaryEnum.TASK_STATUS_FINISH.getType();

        //对 任务状态 的所有情况进行查询,供前端任务条件查询时，可根据任务状态对任务进行筛选
        List<SysDictionary> taskStatusList = dispatchDao.queryDictionInfos(taskStatus);

        Map map = new HashMap<String,Object>();

        map.put("workArea",workArea);  //所属作业区以及班组信息

        map.put("taskStatus",taskStatusList); //任务状态信息

        return map;
    }

    /**
     * @Date 2021/6/1 16:10
     * @Description 工人刷卡领取任务后, 需要记录工人领取时间及一些其他信息到 task_claim 表
     *              然后task_info表中 该 任务信息状态 变更为 进行中
     * @Params
     */
    @Override
    public HttpResult updateTaskStatusAndInsertInfo(TaskClaim taskClaimInfo) {

        //焊工刷卡领取任务后  首先根据 任务id 判断 该焊工可否领取此任务

        //通过任务id 获取该条任务信息
        TaskInfo taskInfo = dispatchDao.queryTaskInfoById(taskClaimInfo.getTaskId());

        //通过任务信息的 焊工id 和 组织id 判断焊工是否可以领取该任务
        if (!ObjectUtils.isEmpty(taskInfo)){

            //当该条任务信息只存在 部门id 时
            if(ObjectUtils.isEmpty(taskInfo.getWelderId()) && !ObjectUtils.isEmpty(taskInfo.getDeptId())){

                //通过 taskClaimInfo中的组织Id 获取到该组织下所有的焊工Id
                List<Long> welderIds = welderDao.getDeptIdByWelderId(taskInfo.getDeptId());

                if(!ObjectUtils.isEmpty(welderIds)){

                    for (Long welderId:welderIds) {
                        if(welderId == taskClaimInfo.getWelderId()){

                            updateTaskInfoAndAddTaskClaimInfo(taskInfo,taskClaimInfo);

                            return HttpResult.ok("领取成功！");
                        }

                        return HttpResult.ok("领取失败,当前焊工无法领取此任务！");
                    }
                }

            }

            //当该条任务信息存在 焊工id 时
            if(!ObjectUtils.isEmpty(taskInfo.getWelderId())){

                //判断焊工Id是否一致
                if(taskClaimInfo.getWelderId() == taskInfo.getWelderId()){

                    updateTaskInfoAndAddTaskClaimInfo(taskInfo,taskClaimInfo);

                    return HttpResult.ok("领取成功！");

                }

                return HttpResult.ok("领取失败,当前焊工无法领取此任务！");
            }

            //当任务信息中无 焊工id 和 组织id 时
            if(ObjectUtils.isEmpty(taskInfo.getWelderId()) && ObjectUtils.isEmpty(taskInfo.getDeptId())){

                updateTaskInfoAndAddTaskClaimInfo(taskInfo,taskClaimInfo);

                return HttpResult.ok("领取成功！");
            }

        }

        return HttpResult.ok("领取失败,暂无该任务信息！");
    }

    public void forEachTaskInfoToCell(Row row,TaskInfo taskInfo){

        //将 任务编号 字段值插入单元格
        Cell getTaskNoCell = row.createCell(0);
        getTaskNoCell.setCellValue(taskInfo.getTaskNo());

        //将 任务等级 字段值插入单元格
        Cell getTaskGradeCell = row.createCell(1);
        getTaskGradeCell.setCellValue(taskInfo.getGradeIdToStr());

        //将 所属班组 字段值插入单元格
        Cell getTaskGradeNameCell = row.createCell(2);
        getTaskGradeNameCell.setCellValue(taskInfo.getDeptName());


        //将 计划开始时间 字段值插入单元格
        Cell getPlanStartTimeCell = row.createCell(3);

        getPlanStartTimeCell.setCellValue(taskInfo.getPlanStarttime());

        //将 计划结束时间 字段值插入单元格
        Cell getPlanEndTimeCell = row.createCell(4);

        getPlanEndTimeCell.setCellValue(taskInfo.getPlanEndtime());

        //将 实际开始时间 字段值插入单元格
        Cell getRealityStartTimeCell = row.createCell(5);
        getRealityStartTimeCell.setCellValue(taskInfo.getRealityStarttime());

        //将 实际结束时间 字段值插入单元格
        Cell getRealityEndTimeCell = row.createCell(6);
        getRealityEndTimeCell.setCellValue(taskInfo.getRealityEndtime());

        //将 任务评价 字段值插入单元格
        Cell evaluateContent = row.createCell(7);
        evaluateContent.setCellValue(taskInfo.getEvaluateContent());

        //将 评价等级 字段值插入单元格
        Cell evaluateStars = row.createCell(8);
        evaluateStars.setCellValue(taskInfo.getEvaluateStarsIdToStr());
    }

    public void updateTaskInfoAndAddTaskClaimInfo(TaskInfo taskInfo,TaskClaim taskClaimInfo){

        //进行 taskClaim表 入库操作
        dispatchDao.addTaskClaimInfo(taskClaimInfo);

        taskInfo.setWelderId(taskInfo.getWelderId());

        taskInfo.setDeptId(taskInfo.getDeptId());

        taskInfo.setStatusStr(DictionaryEnum.TASK_STATUS_NO_GET.getValueName());

        //修改任务状态
        dispatchDao.updateTaskInfo(taskInfo);

    }

    public void importExcelMethod(MultipartFile uploadFile){
        try {
            //workbook excel
            Workbook workbook = new XSSFWorkbook(uploadFile.getInputStream());

            //获取excel的第一个sheet
            Sheet sheet = workbook.getSheetAt(0);

            //获取最后一行的下标
            int lastRowNum = sheet.getLastRowNum();

            List<TaskInfo> taskInfos = new ArrayList<>();

            for (int i = 1; i <= lastRowNum ; i++) {

                //获取第i行
                Row row = sheet.getRow(i);

                //获取本行的最后一个单元格的下标
                short lastCellNum = row.getLastCellNum();

                Object[] obs = new Object[lastCellNum];

                //Excel中一条完整的 任务信息 存储到 obs 里
                for (int j = 0; j <lastCellNum ; j++) {

                    //获取第i行的 第j个 单元格
                    Cell cell = row.getCell(j);

                    System.out.println("cell: "+cell);

                    //拿到单元格的 value值
                    Object value = ExcelUtils.getValue(cell);

                    obs[j] = value;

                }

                //把从excel中拿出来的数据封装到 对象中
                TaskInfo taskInfo = new TaskInfo();

                if(obs[0] instanceof Double){

                    int index = obs[0].toString().indexOf(".");

                    obs[0] = obs[0].toString().substring(0,index);
                }

                //任务编号
                taskInfo.setTaskNo((obs[0].toString()));

                String valueName = (String) obs[1];

                //通过任务等级获取 该任务等级的主键Id
                byte taskGradeId = dispatchDao.queryTaskGradeIdByValueName(valueName);

                //任务等级
                taskInfo.setGrade(taskGradeId);

                String deptName = (String) obs[2];

                //根据班组名 得到对应班组Id
                Long deptId = dispatchDao.queryDeptIdByWorkArea(deptName);

                //所属班组
                taskInfo.setDeptId(deptId);

                String planStartTime = (String) obs[3];

                Timestamp planStartTimeFt = Timestamp.valueOf(planStartTime);

                //计划开始时间
                taskInfo.setPlanStarttime(planStartTime);

                String planEndTime = (String) obs[4];

                Timestamp planEndTimeFt = Timestamp.valueOf(planEndTime);

                //计划结束时间
                taskInfo.setPlanEndtime(planEndTime);

                String realityStartTime = (String) obs[5];

                Timestamp realityStartTimeFt = Timestamp.valueOf(realityStartTime);

                //实际开始时间
                taskInfo.setRealityStarttime(realityStartTime);

                String realityEndTime = (String) obs[6];

                //实际结束时间
                Timestamp realityEndTimeFt = Timestamp.valueOf(realityEndTime);

                taskInfo.setRealityEndtime(realityEndTime);

                String evaluateContent = (String) obs[7];

                //任务评价
                taskInfo.setEvaluateContent(evaluateContent);

                //评价星级
                String evaluateStars = (String) obs[8];

                //根据评价星级 获取到星级Id
                byte evaluateStarsId = dispatchDao.queryTaskGradeIdByValueName(evaluateStars);

                taskInfo.setEvaluateStars(evaluateStarsId);

                //把对象放到list
                taskInfos.add(taskInfo);
            }

            //导入数据库
            dispatchDao.addTaskInfoList(taskInfos);

            HttpResult.ok("导入成功！");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
