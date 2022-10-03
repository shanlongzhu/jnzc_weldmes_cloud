package com.gw.service.data.team.controller;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.service.config.DownExcel;
import com.gw.service.data.team.service.TeamService;
import com.gw.entities.WeldStatisticsDataTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "team")
public class TeamController {
    @Autowired
    private TeamService teamService;

    /**
     * @Date 2021/10/15 10:56
     * @Description 班组生产数据列表
     * @Params
     */
    @GetMapping
    public HttpResult getList(@RequestParam(value="pn",defaultValue = "1") Integer pn,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                              String time1,String time2,String deptId){

        //通过组织机构id 查询 该部门下所有的班组id
        List<Long> ids = teamService.getNextDeptIds(deptId);

        PageHelper.startPage(pn,size);

        //获取班组生产数据列表
        List<WeldStatisticsDataTeam> list = teamService.getList(time1,time2,ids);

        PageInfo<WeldStatisticsDataTeam> page=new PageInfo<>(list,10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/10/15 10:56
     * @Description 导出excel
     * @Params
     */
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response,String time1,String time2,String deptId){

        try {

            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());

            //设置Excel文件名
            String title = URLEncoder.encode("班组生产数据"+time, "UTF-8");

            //设置sheet表格名
            String sheetName = "班组生产数据";

            //通过组织机构id 查询 该部门下所有的班组id
            List<Long> ids = teamService.getNextDeptIds(deptId);

            //获取班组数据列表
            List<WeldStatisticsDataTeam> list = teamService.getList(time1,time2,ids);

            //导出为Excel
            DownExcel.download(response,WeldStatisticsDataTeam.class,list,sheetName,title);

            return HttpResult.ok("Excel导出成功");

        } catch (Exception e) {

            return HttpResult.error("Excel导出失败");
        }
    }
}
