package com.gw.service.process.craft.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.SysDictionary;
import com.gw.entities.WpsLibrary;
import com.gw.service.process.craft.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "library")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    //列表展示
    @GetMapping
    public HttpResult getList(@RequestParam(value = "pn", defaultValue = "1") Integer pn, @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageHelper.startPage(pn, size);
        List<WpsLibrary> list = libraryService.getList();
        PageInfo<WpsLibrary> page = new PageInfo<>(list, 5);
        return HttpResult.ok(page);
    }

    //新增工艺库
    @PostMapping
    public HttpResult addLibrary(@RequestBody WpsLibrary wpsLibrary) {
        try {
            HttpResult result = new HttpResult();
            int i = libraryService.addLibrary(wpsLibrary);
            if (i > 0) {
                result.setMsg("新增成功！");
            } else {
                result.setMsg("新增失败！");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    //修改前先查询
    @GetMapping("{id}")
    public HttpResult getById(@PathVariable Long id) {
        List<WpsLibrary> list = libraryService.getById(id);
        return HttpResult.ok(list);

    }

    //修改工艺库
    @PutMapping
    public HttpResult updateLibrary(@RequestBody WpsLibrary wpsLibrary) {
        try {
            HttpResult result = new HttpResult();
            int i = libraryService.updateLibrary(wpsLibrary);
            if (i > 0) {
                result.setMsg("修改成功！");
            } else {
                result.setMsg("修改失败！");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    //删除工艺库
    @DeleteMapping
    public HttpResult deleteLibrary(Long id) {
        try {
            HttpResult result = new HttpResult();
            int i = libraryService.deleteLibrary(id);
            if (i > 0) {
                result.setMsg("删除成功！");
            } else {
                result.setMsg("删除失败！");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }

    }

    /**
     * @Date 2021/7/1 14:12
     * @Description 查询关联厂家的设备信息
     * @Params id 厂家字典表id
     */
    @RequestMapping("getMachineInfoByFirm")
    public HttpResult getMachineInfoByFirm(Long id) {

        List<SysDictionary> list = libraryService.getMachineInfoByFirmId(id);

        return HttpResult.ok(list);
    }

    /**
     * @Date 2021/7/15 15:56
     * @Description 查询关联区域的跨间信息
     * @Params id 区域字典表id
     */
    @RequestMapping("getBayInfoByArea")
    public HttpResult getBayInfoByArea(Long id) {

        List<SysDictionary> list = libraryService.getBayInfoByAreaById(id);

        return HttpResult.ok(list);
    }

}
