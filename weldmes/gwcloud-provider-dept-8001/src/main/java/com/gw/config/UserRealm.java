package com.gw.config;

import com.gw.entities.UserLoginInfo;
import com.gw.entities.SysUser;
import com.gw.sys.service.UserRolesAndPerService;
import lombok.SneakyThrows;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/4 9:15
 * @Description 用户登录身份认证 以及 授权
 */
@Configuration
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserRolesAndPerService userRolesAndPerService;

    /**
     * @Date 2021/6/4 9:17
     * @Description 登陆后授权
     * @Params
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //获得当前用户
        UserLoginInfo currentUser = (UserLoginInfo)principalCollection.asList().get(0);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        //绑定角色
        authorizationInfo.setStringPermissions(new HashSet<>(currentUser.getRoles()));

        return authorizationInfo;
    }

    /**
     * @Date 2021/6/4 9:17
     * @Description 用户登录身份认证
     * @Params
     */
    @SneakyThrows
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        Subject currentUser = SecurityUtils.getSubject();

        UsernamePasswordToken userToken = (UsernamePasswordToken)token;

        Session session  = currentUser.getSession();

        String userName = (String)session.getAttribute("userName");

        //String passWord = (String)session.getAttribute("passWord");
        //获取前端传入的用户名
        //String userName = userToken.getUsername();

        //从数据库中获取用户
        SysUser sysUser = userRolesAndPerService.queryUserInfoByUserNameAndPwd(userName);

        //查询 用户数据 为空
        if(ObjectUtils.isEmpty(sysUser)){

            return null;
        }

        //当用户状态 为 0  0-禁用  1-正常
        if(sysUser.getStatus() == 0){

            throw new LockedAccountException();

        }

        //通过用户id获取  该用户的角色id
        List<Long> roleList = userRolesAndPerService.queryUserRoleIdList(sysUser.getId());

        //获取用户的角色名称
        List<String> roleNames = userRolesAndPerService.queryUserRoles(roleList);

        if(ObjectUtils.isEmpty(roleNames)){

            throw new RuntimeException("用户角色不存在,登陆失败");
        }

        UserLoginInfo userLoginInfo = new UserLoginInfo();

        //将查询出来的用户信息放入 用户登录信息类
        userLoginInfo.setId(sysUser.getId());

        userLoginInfo.setUserName(sysUser.getName());

        userLoginInfo.setPassWord(sysUser.getPassword());

        userLoginInfo.setStatus(sysUser.getStatus());

        userLoginInfo.setDeptId(sysUser.getDeptId());

        //将 用户角色 绑定到当前用户中
        userLoginInfo.setRoles(roleNames);

        //将 用户角色id 绑定到当前用户中
        userLoginInfo.setRoleIds(roleList);

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userLoginInfo,userLoginInfo.getPassWord(),getName());

        return simpleAuthenticationInfo;
    }
}
