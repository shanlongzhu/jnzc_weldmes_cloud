package com.gw.service.equipment.welder.controller;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.EquipFeatureInfo;
import com.gw.entities.MachineWeldInfo;
import com.gw.service.config.DownExcel;
import com.gw.service.config.WelderExcelListener;
import com.gw.service.equipment.welder.service.WelderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "welder")
@Slf4j
public class WelderController {

    @Autowired
    private WelderService welderService;

    /**
     * @Date 2021/10/12 15:27
     * @Description 焊机列表查询
     * @Params
     */
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                              String machineNo, Integer type, Integer grade, Integer status,
                              Integer firm, Long isNetwork, String gatherNo, String ipPath, Integer model, Integer area, Integer bay) {

        PageHelper.startPage(pn, size);

        List<MachineWeldInfo> list = welderService.getList(machineNo, type, grade, status, firm, isNetwork, gatherNo, ipPath, model, area, bay);

        PageInfo<MachineWeldInfo> page = new PageInfo<>(list, 10);

        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/10/12 15:27
     * @Description 焊机是否绑定任务列表查询
     * @Params
     */
    @RequestMapping("welderInfosNoPage")
    public HttpResult getListNoPage(String machineNo, Integer type, Integer grade, Integer status,
                                    Integer firm, Long isNetwork, String gatherNo, String ipPath, Integer model, Integer area, Integer bay) {

        List<MachineWeldInfo> list = welderService.getStatusOfMachineWeldInfos(machineNo, type, grade, status, firm, isNetwork, gatherNo, ipPath, model, area, bay);

        return HttpResult.ok(list);
    }

    /**
     * @Date 2021/10/12 15:28
     * @Description 新增焊机信息
     * @Params
     */
    @PostMapping
    public HttpResult addWelder(@RequestBody MachineWeldInfo machineWeldInfo) {

        try {

            welderService.addWelder(machineWeldInfo);

            return HttpResult.ok("新增成功!");

        } catch (Exception e) {

            return HttpResult.error("新增失败!");
        }
    }

    /**
     * @Date 2021/10/12 15:28
     * @Description 根据焊机id 查询信息
     * @Params
     */
    @GetMapping("{id}")
    public HttpResult getById(@PathVariable Long id) {

        List<MachineWeldInfo> list = welderService.getById(id);

        return HttpResult.ok(list);

    }

    /**
     * @Date 2021/10/12 15:28
     * @Description 修改焊机信息
     * @Params
     */
    @PutMapping
    public HttpResult updateWelder(@RequestBody MachineWeldInfo machineWeldInfo) {

        try {

            welderService.updateWelder(machineWeldInfo);

            return HttpResult.ok("修改成功!");
        } catch (Exception e) {

            return HttpResult.error("修改失败!");
        }
    }

    /**
     * @Date 2021/10/12 15:28
     * @Description 删除焊机信息
     * @Params
     */
    @DeleteMapping
    public HttpResult deleteWelder(Long id) {

        try {

            welderService.deleteWelder(id);

            return HttpResult.ok("删除成功!");

        } catch (Exception e) {

            return HttpResult.error("删除失败!");
        }

    }

    /**
     * @Date 2021/10/12 15:29
     * @Description 导出Excel
     * @Params
     */
    @GetMapping(value = "excel")
    public HttpResult exportExcel(HttpServletResponse response, String machineNo, Integer type, Integer grade, Integer status,
                                  Integer firm, Long isNetwork, String gatherNo, String ipPath, Integer model, Integer area, Integer bay) {

        try {

            //设置Excel文件名
            String title = URLEncoder.encode("焊机设备管理", "UTF-8");

            //设置sheet表格名
            String sheetName = "焊机设备数据";

            //获取任务列表
            List<MachineWeldInfo> list = welderService.getList(machineNo, type, grade, status, firm, isNetwork, gatherNo, ipPath, model, area, bay);

            //导出为Excel
            DownExcel.download(response, MachineWeldInfo.class, list, sheetName, title);

            return HttpResult.ok("Excel导出成功");

        } catch (Exception e) {

            return HttpResult.error("Excel导出失败");
        }

    }

    /**
     * @Date 2021/10/12 15:29
     * @Description 导入Excel
     * @Params
     */
    @PostMapping(value = "importExcel")
    public HttpResult importExcel(@RequestParam("file") MultipartFile file) throws IOException {

        EasyExcel.read(file.getInputStream(), MachineWeldInfo.class, new WelderExcelListener(welderService)).sheet().doRead();

        return HttpResult.ok("Excel导入成功");

    }

    /**
     * @Date 2021/7/13 18:01
     * @Description 获取历史曲线中焊机id以及设备编号
     * @Params
     */
    @RequestMapping(value = "historyWelderInfos")
    public HttpResult historyWelderInfos() {

        List<MachineWeldInfo> list = welderService.getIdAndMachineNoOfWelderInfos();

        return HttpResult.ok(list);
    }

    /**
     * @Date 2021/7/29 13:20
     * @Description 根据部门id查询设备信息列表
     * @Params id 部门id
     */
    @RequestMapping(value = "getWeldInfosByDeptId")
    public HttpResult getWeldInfos(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Long id) {

        PageHelper.startPage(pn, 10);

        List<MachineWeldInfo> list = welderService.getWeldInfos(id);

        PageInfo<MachineWeldInfo> page = new PageInfo<>(list, 5);
        return HttpResult.ok(page);
    }

    /**
     * @Date 2021/10/19 14:15
     * @Description 根据焊机标识、焊机id 获取焊机特征信息
     * @Params
     */
    @RequestMapping(value = "getEquipFeatureInfo")
    public HttpResult getEquipFeatureInfo(Integer macFlag, Long id) {

        EquipFeatureInfo equipFeatureInfo = welderService.getEquipFeatureInfo(macFlag, id);

        return HttpResult.ok(equipFeatureInfo);
    }

    @RequestMapping(value = "machineUpgrade")
    public HttpResult machineUpgrade(@RequestParam("file") MultipartFile file) {

        HttpResult result = new HttpResult();
        String property = System.getProperty("user.dir");
        File absolutePath = new File(property);
        File savetFile = new File(absolutePath.getParent() + "/html/" + file.getOriginalFilename());
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(savetFile);
        } catch (Exception e) {
            log.error("异常：", e);
        }

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            log.error("异常：", e);
            result.setMsg("false");
            return result;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("异常：", e);
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error("异常：", e);
                }
            }
        }
        result.setMsg("true");
        return result;
    }

}
