package com.gw.data.device.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.config.DownExcel;
import com.gw.data.device.service.DeviceService;
import com.gw.entities.WeldStatisticsDataDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Date 2021/10/18 15:01
 * @Description 设备生产数据控制器
 * @Params
 */
@CrossOrigin
@RestController
@RequestMapping(value = "device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     * @Date 2021/10/18 15:01
     * @Description 获取设备生产数据列表
     * @Params
     */
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                              String time1, String time2,String machineNo,Long deptId) {

        String name=deviceService.getName(deptId);

        PageHelper.startPage(pn, size);

        List<WeldStatisticsDataDevice> list = deviceService.getList(time1,time2,machineNo,name);

        PageInfo page = new PageInfo(list, 5);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/10/18 15:04
     * @Description 导出excel
     * @Params
     */
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response,String time1, String time2,String machineNo,Long deptId) {

        try {

            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());

            //设置Excel文件名
            String title = URLEncoder.encode("设备生产数据" + time, "UTF-8");

            //设置sheet表格名
            String sheetName = "设备生产数据";

            //获取部门名称
            String name = deviceService.getName(deptId);

            //获取设备生产数据列表
            List<WeldStatisticsDataDevice> list = deviceService.getList(time1, time2, machineNo, name);

            //导出为Excel
            DownExcel.download(response, WeldStatisticsDataDevice.class, list, sheetName, title);

            return HttpResult.ok("Excel导出成功");

        } catch (Exception e) {

            return HttpResult.error("Excel导出失败");
        }
    }
}
