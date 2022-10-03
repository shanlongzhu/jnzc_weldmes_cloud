package com.gw.service.data.artifact.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.service.config.DownExcel;
import com.gw.service.data.artifact.service.ArtifactService;
import com.gw.service.data.team.service.TeamService;
import com.gw.entities.UserLoginInfo;
import com.gw.entities.WeldStatisticsDataArtifact;
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
@RequestMapping(value = "artifact")
public class ArtifactController {

    @Autowired
    private ArtifactService artifactService;

    @Autowired
    TeamService teamService;

    /**
     * @Date 2021/10/18 13:55
     * @Description 工件生产数据列表展示
     * @Params
     */
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                              String time1, String time2, String taskNo, Long deptId) {

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

        List<WeldStatisticsDataArtifact> list = artifactService.getList(time1, time2, taskNo, ids);

        PageInfo<WeldStatisticsDataArtifact> page = new PageInfo<>(list, 10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/10/18 13:54
     * @Description 导出excel
     * @Params
     */
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response, String time1, String time2, String taskNo, Long deptId) {

        try {

            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());

            //设置Excel文件名
            String title = URLEncoder.encode("工件生产数据" + time, "UTF-8");

            //设置sheet表格名
            String sheetName = "工件生产数据";

            //通过组织机构id 查询 该部门下所有的班组id
            List<Long> ids = teamService.getNextDeptIds(deptId.toString());

            //获取工件报表数据
            List<WeldStatisticsDataArtifact> list = artifactService.getList(time1, time2, taskNo, ids);

            //导出为Excel
            DownExcel.download(response, WeldStatisticsDataArtifact.class, list, sheetName, title);

            return HttpResult.ok("Excel导出成功");

        } catch (Exception e) {

            return HttpResult.error("Excel导出失败");
        }
    }

}
