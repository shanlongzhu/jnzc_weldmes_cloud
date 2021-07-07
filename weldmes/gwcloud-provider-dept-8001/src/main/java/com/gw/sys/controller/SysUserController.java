package com.gw.sys.controller;

import com.gw.common.*;
import com.gw.entities.SysDeptInfo;
import com.gw.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  用户控制器
 * @Params
 */
@RestController
@CrossOrigin
public class SysUserController {

    @Autowired
    SysUserService sysUserService;


    /**
     * @Date 2021/7/7 9:43
     * @Description 获取 当前用户 的班组树状图信息
     * @Params
     */
    @RequestMapping(value = "user/gradeInfos")
    public HttpResult getCurrentGradeInfos() {

        SysDeptInfo sysDeptInfo = sysUserService.getGradeInfo();

        return HttpResult.ok(sysDeptInfo);

    }


}
