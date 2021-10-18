package com.gw.data.artifact.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.config.DownExcel;
import com.gw.data.artifact.service.ArtifactService;
import com.gw.entities.WeldStatisticsDataArtifact;
import org.springframework.beans.factory.annotation.Autowired;
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


    /**
     * @Date 2021/10/18 13:55
     * @Description 工件生产数据列表展示
     * @Params
     */
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                              String time1, String time2,String taskNo,Long deptId) {

        String name=artifactService.getName(deptId);

        PageHelper.startPage(pn, size);

        List<WeldStatisticsDataArtifact> list = artifactService.getList(time1, time2,taskNo,name);

        PageInfo page = new PageInfo(list, 10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/10/18 13:54
     * @Description 导出excel
     * @Params
     */
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response, String time1, String time2,String taskNo,Long deptId) {

        try {

            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());

            //设置Excel文件名
            String title = URLEncoder.encode("工件生产数据"+time, "UTF-8");

            //设置sheet表格名
            String sheetName = "工件生产数据";

            //获取部门名称
            String name = artifactService.getName(deptId);

            //获取工件报表数据
            List<WeldStatisticsDataArtifact> list = artifactService.getList(time1, time2,taskNo,name);

            //导出为Excel
            DownExcel.download(response,WeldStatisticsDataArtifact.class,list,sheetName,title);

            return HttpResult.ok("Excel导出成功");

        } catch (Exception e) {

            return HttpResult.error("Excel导出失败");
        }
    }

}
