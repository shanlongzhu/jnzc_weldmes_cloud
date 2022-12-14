package com.gw.service.config;

import com.gw.common.DateTimeUtils;
import com.gw.entities.ApiInfo;
import com.gw.entities.SysLog;
import com.gw.entities.UserLoginInfo;
import com.gw.service.sys.dao.SysLogDao;
import com.gw.service.sys.service.SysLogService;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Aspect
@Log4j
@Component
public class SysLogAspect {

    @Autowired
    SysLogService sysLogService;

    @Autowired
    SysLogDao sysLogDao;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * ip
     */
    private String ip;

    /**
     * 请求接口名
     */
    private String interfaceName;

    /**
     * 请求方式
     */
    private String methodWays;

    /**
     * 请求方法路径
     */
    private String requestMethodUrl;

    /**
     * 请求类方法参数
     */
    private String requestArgs;

    //声明一个切点
    @Pointcut("execution(public * com.gw.*.*.controller.*.*(..)) || execution(public * com.gw.*.controller.*.*(..))")
    private void aspect() {
    }

    @Before("aspect()")
    public void before(JoinPoint joinPoint) throws NoSuchMethodException {

        //开始访问的时间
        startTime = System.currentTimeMillis();

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //获取请求ip
        ip = request.getRemoteAddr();

        //获取请求方法名
        interfaceName = request.getRequestURI();

        //获取请求方式
        methodWays = request.getMethod();

        //获取请求方法地址
        requestMethodUrl = joinPoint.getSignature().toString();

        //获取请求参数
        requestArgs = Arrays.toString(joinPoint.getArgs());

    }

    @AfterReturning(pointcut = "aspect()")
    public void methodAfterReturning() {

        Long endTime = System.currentTimeMillis();

        //执行时长
        Double time = (endTime - startTime) / 1000.0;

        SysLog sysLog = new SysLog();

        //获取当前用户
        Subject currentUser = SecurityUtils.getSubject();

        UserLoginInfo subject = (UserLoginInfo) currentUser.getPrincipal();

        if (!ObjectUtils.isEmpty(subject)) {

            //用户名
            sysLog.setUserName(subject.getUserName());
        }

        //用户操作
        switch (methodWays) {
            case "GET":
                sysLog.setOperation("查询");
                break;
            case "POST":
                sysLog.setOperation("新增");
                break;
            case "PUT":
                sysLog.setOperation("修改");
                break;
            case "DELETE":
                sysLog.setOperation("删除");
                break;
        }
        //请求IP
        sysLog.setIp(ip);

        //请求类方法参数
        sysLog.setParams(requestArgs);

        //执行时长
        sysLog.setTime(time.toString());

        //请求方法路径
        sysLog.setMethod(requestMethodUrl);

        //入库时间
        String createTime = DateTimeUtils.getNowDateTime();

        sysLog.setCreateTime(createTime);


        //获取系统接口路径信息列表
        List<ApiInfo> apiInfos = sysLogDao.selectApiInfos();

        for (ApiInfo apiInfo : apiInfos) {

            //调用接口路径与接口信息列表进行匹配
            if (apiInfo.getApiName().equals(interfaceName)) {

                //菜单模块
                sysLog.setMenuModel(apiInfo.getMenuOperation());

                break;
            }
        }

        //日志入库
        sysLogService.addSysLogInfo(sysLog);

    }


}
