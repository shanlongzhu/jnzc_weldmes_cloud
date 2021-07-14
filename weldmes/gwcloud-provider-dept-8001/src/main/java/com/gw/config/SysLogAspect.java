package com.gw.config;

import com.gw.common.DateTimeUtil;
import com.gw.entities.SysLog;
import com.gw.sys.service.SysLogService;
import lombok.extern.log4j.Log4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


/**
 * @Author zhanghan
 * @Date 2021/7/9 10:06
 * @Description  日志切面类
 */
@Aspect
@Log4j
@Component
public class SysLogAspect {

    @Autowired
    SysLogService sysLogService;

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
    @Pointcut("execution(public * com.gw.process.dispatch.controller.*.*(..))")
    private void aspect(){}

    @Before("aspect()")
    public void before(JoinPoint joinPoint) throws NoSuchMethodException{

        //开始访问的时间
        startTime = System.currentTimeMillis();

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //获取请求ip
        ip = request.getRemoteAddr();

        //获取请求地址
        interfaceName = request.getRequestURI();

        //获取请求方式
        methodWays = request.getMethod();

        //获取请求方法名
        requestMethodUrl = joinPoint.getSignature().toString();

        //获取请求参数
        requestArgs = Arrays.toString(joinPoint.getArgs());

    }

    @AfterReturning(pointcut = "aspect()")
    public void methodAfterReturning(){

        Long endTime = System.currentTimeMillis();

        //执行时长
        Double time = (endTime-startTime)/1000.0;

        log.info("==============================请求内容开始=====================================");
        log.info("请求IP:" + ip);
        log.info("请求接口名:" + interfaceName);
        log.info("请求方式:" + methodWays);
        log.info("请求方法路径:" + requestMethodUrl);
        log.info("请求类方法参数:" + requestArgs);
        log.info("==============================请求内容结束=====================================");
        log.info("执行时长:" + (endTime-startTime)/1000.0+"s");

        SysLog sysLog = new SysLog();

        //用户名

        //用户操作

        //请求IP
        sysLog.setIp(ip);

        //请求类方法参数
        sysLog.setParams(requestArgs);

        //执行时长
        sysLog.setTime(time.toString());

        //请求方法路径
        sysLog.setMethod(requestMethodUrl);

        //入库时间
        String createTime = DateTimeUtil.getCurrentTime();

        sysLog.setCreateTime(createTime);

        //日志入库
        sysLogService.addSysLogInfo(sysLog);

    }


}
