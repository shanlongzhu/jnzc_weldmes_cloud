package com.gw.process.craft.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gw.common.HttpResult;
import com.gw.entities.WpsLibrary;
import com.gw.entities.WpsNorm;
import com.gw.process.craft.service.CraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "craft")
public class CraftController {

    @Autowired
    private CraftService craftService;

    //根据工艺库id列表展示
    @GetMapping
    public HttpResult getList(@RequestParam(value="pn",defaultValue = "1") Integer pn,Long id){
        PageHelper.startPage(pn,10);
        List<WpsNorm> list=craftService.getList(id);
        PageInfo page=new PageInfo(list,5);
        return HttpResult.ok(page);
    }

    //新增工艺
    @PostMapping
    public HttpResult addCraft(@RequestBody WpsNorm wpsNorm){
        try {
            HttpResult result=new HttpResult();
            int i=craftService.addCraft(wpsNorm);
            if(i>0){
                result.setMsg("新增成功！");
            }else {
                result.setMsg("新增失败！");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }
    //修改工艺前先查询
    @GetMapping("{id}")
    public List<WpsNorm> getById(@PathVariable Long id){
        List<WpsNorm> list=craftService.getById(id);
        return list;
    }

    //修改工艺信息
    @PutMapping
    public HttpResult updateLibrary(@RequestBody WpsNorm wpsNorm){
        try {
            HttpResult result=new HttpResult();
            int i=craftService.updateCraft(wpsNorm);
            if(i>0){
                result.setMsg("修改成功！");
            }else {
                result.setMsg("修改失败！");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }
    }

    //删除工艺信息
    @DeleteMapping
    public HttpResult deleteCraft(Long id){
        try {
            HttpResult result=new HttpResult();
            int i=craftService.deleteCraft(id);
            if(i>0){
                result.setMsg("删除成功！");
            }else {
                result.setMsg("删除失败！");
            }
            return result;
        } catch (Exception e) {
            return HttpResult.error();
        }

    }
}
