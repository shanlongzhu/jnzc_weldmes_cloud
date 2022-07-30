package com.gw.data.productionTask.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.config.DownExcel;
import com.gw.data.productionTask.service.ProductionTaskService;
import com.gw.data.team.service.TeamService;
import com.gw.entities.UserLoginInfo;
import com.gw.entities.WeldStatisticsDataProductionTask;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "ProductionTask")
public class ProductionTaskController {

    @Autowired
    private ProductionTaskService productionTaskService;

    @Autowired
    TeamService teamService;

    /**
     * @Date 2021/10/18 17:13
     * @Description 获取生产任务详情数据列表
     * @Params
     */
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                              String time1, String time2, String welderNo, String welderName,
                              String machineNo, String taskNo, Long deptId) {

        //判断用户部门id是否传入
        if (ObjectUtils.isEmpty(deptId)) {

            //获取到当前用户
            Subject currentUser = SecurityUtils.getSubject();

            UserLoginInfo subject = (UserLoginInfo) currentUser.getPrincipal();

            deptId = subject.getDeptId();

        }

        //通过组织机构id 查询 该部门下所有的班组id
        List<Long> ids = teamService.getNextDeptIds(deptId.toString());

        PageHelper.startPage(pn, size);

        List<WeldStatisticsDataProductionTask> list = productionTaskService.getList(time1, time2, welderNo, welderName, machineNo, taskNo, ids);

        PageInfo<WeldStatisticsDataProductionTask> page = new PageInfo<>(list, 5);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/10/18 17:13
     * @Description 导出excel
     * @Params
     */
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response, String time1, String time2, String welderNo, String welderName, String machineNo, String taskNo, Long deptId) {

        try {

            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());

            //设置Excel文件名
            String title = URLEncoder.encode("生产任务详情" + time, "UTF-8");

            //设置sheet表格名
            String sheetName = "生产任务详情";

            //通过组织机构id 查询 该部门下所有的班组id
            List<Long> ids = teamService.getNextDeptIds(deptId.toString());

            //获取人员生产数据信息列表
            List<WeldStatisticsDataProductionTask> list = productionTaskService.getList(time1, time2, welderNo, welderName,
                    machineNo, taskNo, ids);

            //导出为Excel
            DownExcel.download(response, WeldStatisticsDataProductionTask.class, list, sheetName, title);

            return HttpResult.ok("Excel导出成功");

        } catch (Exception e) {

            return HttpResult.error("Excel导出失败");
        }
    }
}
