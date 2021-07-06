package com.gw.sys.controller;

import com.gw.common.HttpResult;

import com.gw.entities.Token;
import com.gw.entities.UserLoginInfo;
import com.gw.sys.service.SysMenuService;
import com.gw.sys.service.SysUserService;
import com.gw.sys.service.UserRolesAndPerService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  登陆控制器
 * @Params
 */
@RestController
@CrossOrigin
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    SysUserService sysUserService;

    @Autowired
    UserRolesAndPerService userRolesAndPerService;

    @Autowired
    SysMenuService sysMenuService;

    //登录身份认证
    @RequestMapping("/sysUser/login")
    public HttpResult login(@RequestParam String username, @RequestParam String password) {

        //获取到当前用户
        Subject currentUser = SecurityUtils.getSubject();

        UserLoginInfo subject = (UserLoginInfo)currentUser.getPrincipal();

        //  针对 同一浏览器 不同窗口 中 同一用户重复登录
        if(currentUser.isAuthenticated() &&
                subject.getUserName().equals(username) &&
                subject.getPassWord().equals(password)){

            return HttpResult.ok("当前用户已登录,请更换用户重新登录");
        }

        //当前用户 未登录 时 进行登录认证
        if(!currentUser.isAuthenticated()){

            UsernamePasswordToken userToken = new UsernamePasswordToken(username,password);

            try{

                //登录认证 --> 由此跳转入UserRealm中进行认证
                currentUser.login(userToken);

                //登录成功 获取 当前用户 的sessionId
                Serializable sessionId = currentUser.getSession().getId();

                //将 当前用户 的sessionId 作为 token 返回给前端  存于浏览器请求头中的 Authorization 中
                Token token = new Token(sessionId);

                return HttpResult.ok(token);

            }catch(UnknownAccountException uka){

                logger.info("登陆失败: 此用户 >>> "+userToken.getPrincipal()+" 不存在");

                return HttpResult.ok("登陆失败,用户不存在");

            }catch(IncorrectCredentialsException ice){

                logger.info("登陆失败: 此用户 >>> "+userToken.getPrincipal()+" 密码不正确");

                return HttpResult.ok("登陆失败,密码不正确");

            }catch(LockedAccountException lae){

                logger.info("登陆失败: 此用户 >>> "+userToken.getPrincipal()+" 已被锁定");

                return HttpResult.ok("登陆失败,用户已被锁定");

            }catch(AuthenticationException ate){

                logger.info("此用户 >>> "+userToken.getPrincipal()+" 登陆失败");

                return HttpResult.ok("用户登陆失败");
            }
        }

        return HttpResult.ok(" 用户已登录");

    }

    /**
     * @Date 2021/6/16 13:27
     * @Description  获取用户角色信息
     * @Params
     */
    @RequestMapping(value = "vue/getInfo")
    public HttpResult getInfo(){

        //获取到当前用户
        Subject currentUser = SecurityUtils.getSubject();
        UserLoginInfo userLoginInfo = (UserLoginInfo)currentUser.getPrincipal();

        if(ObjectUtils.isEmpty(userLoginInfo)){
            return HttpResult.ok("当前用户信息为空!");
        }

        Map<String,Object> menus = sysMenuService.getCurrentUserMenuAndButtonInfos();


        Map<String,Object> map = new HashMap<>();

        map.put("id",userLoginInfo.getId());
        map.put("userName",userLoginInfo.getUserName());
        map.put("roles",userLoginInfo.getRoles());
        map.put("menus",menus);

        return HttpResult.ok(map);
    }

    /**
     * @Date 2021/6/4 15:56
     * @Description 用户登出
     * @Params
     */
    @RequestMapping(value = "sysUser/loginOut")
    public HttpResult loginOut(){

        //获取到当前用户
        Subject currentUser = SecurityUtils.getSubject();

        currentUser.logout();

        return HttpResult.ok("用户退出系统");
    }
}

