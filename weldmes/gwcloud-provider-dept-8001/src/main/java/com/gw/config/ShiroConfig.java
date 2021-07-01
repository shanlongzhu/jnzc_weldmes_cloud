package com.gw.config;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhanghan
 * @Date 2021/6/3 18:09
 * @Description Shiro配置类
 */
@Configuration
public class ShiroConfig {

    @Autowired
    private UserRealm userRealm;


    /**
     * @Date 2021/6/4 9:06
     * @Description  SecurityManager 用于管理 Realm 、流程控制
     * @Params  Realm  资源
     */
    /*@Bean
    public DefaultWebSecurityManager userSecurityManager(Realm userRealm){

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        SessionManager sessionManager = new DefaultWebSessionManager();

        sessionManager.
        securityManager.setRealm(userRealm);

        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }*/

    /**
     * @Date 2021/6/4 9:10
     * @Description 请求过滤器
     * @Params securityManager
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager){

        ShiroFilterFactoryBean shiroFilterBean = new ShiroFilterFactoryBean();

        shiroFilterBean.setSecurityManager(securityManager);

        //待前端确定页面路径再配置
        /*// 身份认证失败，则跳转到登录页面的配置
        shiroFilterFactoryBean.setLoginUrl("/login");

        //权限认证失败，则跳转到指定页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/path/unauth");*/

        //配置路径过滤器  anon 无需登陆验证,直接访问; acthc 访问需要进行登录验证
        Map<String,String> filterMap = new HashMap<>();

        /*filterMap.put("/sysUser/login/**","anon");

        filterMap.put("/loginOut","anon");

        filterMap.put("/**","authc");*/

        shiroFilterBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterBean;
    }

    /**
     * @Date 2021/6/17 23:54
     * @Description  shiro安全管理器  shiro自定义session 配置
     * @Params
     */
    @Bean("securityManager")
    public SecurityManager securityManager(SessionDaoConfig sessionDaoConfig) {

        DefaultSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(userRealm);

        // 自定义session管理
        TokenWebSessionManager sessionManager = new TokenWebSessionManager();

        sessionManager.setSessionDAO(sessionDaoConfig);

        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }


    /**
     * @Date 2021/6/17 23:46
     * @Description 管理生命周期  如果不注入可能会无法启动  注意方法要是静态的
     * @Params
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {

        return new LifecycleBeanPostProcessor();
    }


    /**
     * @Date 2021/6/17 23:47
     * @Description  加入注解的使用，不加入注解不生效
     * @Params
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public static DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {

        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();

        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);

        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {

        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();

        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);

        return authorizationAttributeSourceAdvisor;
    }

}
