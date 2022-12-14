package com.gw.service.sys.controller;

import com.gw.common.HttpResult;
import com.gw.entities.Token;
import com.gw.entities.UserLoginInfo;
import com.gw.service.sys.service.SysMenuService;
import com.gw.service.sys.service.SysUserService;
import com.gw.service.sys.service.UserRolesAndPerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
public class LoginController {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    UserRolesAndPerService userRolesAndPerService;

    @Autowired
    SysMenuService sysMenuService;

    //登录身份认证
    @RequestMapping("/sysUser/login")
    public HttpResult login(@RequestParam String username, @RequestParam String password) throws Exception {

        //获取到当前用户
        Subject currentUser = SecurityUtils.getSubject();
        //永不过期,在登陆最开始加上
        SecurityUtils.getSubject().getSession().setTimeout(-1000L);
        UserLoginInfo subject = (UserLoginInfo) currentUser.getPrincipal();
        String newPwdMD5 = DigestUtils.md5Hex(password);
        //  针对 同一浏览器 不同窗口 中 同一用户重复登录
        if (currentUser.isAuthenticated() &&
                subject.getUserName().equals(username) &&
                subject.getPassWord().equals(newPwdMD5)) {
            throw new RuntimeException("当前用户已登录,请更换用户重新登录");
        }

        //当前用户 未登录 时 进行登录认证
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken userToken = new UsernamePasswordToken(username, newPwdMD5);
            currentUser.getSession().setAttribute("userName", username);
            currentUser.getSession().setAttribute("passWord", password);
            try {
                //登录认证 --> 由此跳转入UserRealm中进行认证
                currentUser.login(userToken);
                //登录成功 获取 当前用户 的sessionId
                Serializable sessionId = currentUser.getSession().getId();
                //将 当前用户 的sessionId 作为 token 返回给前端  存于浏览器请求头中的 Authorization 中
                Token token = new Token(sessionId);
                return HttpResult.ok(token);
            } catch (UnknownAccountException uka) {
                log.info("登陆失败: 此用户 >>> " + userToken.getPrincipal() + " 不存在");
                return HttpResult.error("登陆失败,用户不存在");
            } catch (IncorrectCredentialsException ice) {
                log.info("登陆失败: 此用户 >>> " + userToken.getPrincipal() + " 密码不正确");
                return HttpResult.error("登陆失败,密码不正确");
            } catch (LockedAccountException lae) {
                log.info("登陆失败: 此用户 >>> " + userToken.getPrincipal() + " 已被禁用");
                return HttpResult.error("登陆失败,账号已被禁用");
            } catch (AuthenticationException ate) {
                log.info("此用户 >>> " + userToken.getPrincipal() + " 登陆失败");
                return HttpResult.error("用户登陆失败");
            }
        }
        return HttpResult.ok(" 用户已登录");

    }

    /**
     * @Date 2021/6/16 13:27
     * @Description 获取用户角色信息
     * @Params
     */
    @RequestMapping(value = "vue/getInfo")
    public HttpResult getInfo() {

        //获取到当前用户
        Subject currentUser = SecurityUtils.getSubject();
        UserLoginInfo userLoginInfo = (UserLoginInfo) currentUser.getPrincipal();

        if (ObjectUtils.isEmpty(userLoginInfo)) {
            return HttpResult.ok("当前用户信息为空!");
        }

        Map<String, Object> menus = sysMenuService.getCurrentUserMenuAndButtonInfos();


        Map<String, Object> map = new HashMap<>();

        map.put("id", userLoginInfo.getId());
        map.put("deptId", userLoginInfo.getDeptId());
        map.put("userName", userLoginInfo.getUserName());
        map.put("roles", userLoginInfo.getRoles());
        map.put("menus", menus);

        return HttpResult.ok(map);
    }

    /**
     * @Date 2021/6/4 15:56
     * @Description 用户登出
     * @Params
     */
    @RequestMapping(value = "sysUser/loginOut")
    public HttpResult loginOut() {

        //获取到当前用户
        Subject currentUser = SecurityUtils.getSubject();

        currentUser.logout();

        return HttpResult.ok("用户退出系统");
    }
}

