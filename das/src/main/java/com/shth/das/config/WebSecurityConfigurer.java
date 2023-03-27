package com.shth.das.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    /**
     * 上下文路径
     */
    private final String contextPath;

    public WebSecurityConfigurer(AdminServerProperties adminServerProperties) {
        this.contextPath = adminServerProperties.getContextPath();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(contextPath + "/");

        http.headers().frameOptions().disable()
                .and().authorizeRequests()
                .antMatchers(contextPath + "/assets/**"
                        , contextPath + "/login"
                        , contextPath + "/actuator/**"
                        , contextPath + "/instances/**"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage(contextPath + "/login")
                .successHandler(successHandler).and()
                .logout().logoutUrl(contextPath + "/logout")
                .and()
                .httpBasic().and()
                .csrf()
                .disable();
    }
}
