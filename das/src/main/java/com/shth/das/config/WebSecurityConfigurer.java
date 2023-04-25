package com.shth.das.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
public class WebSecurityConfigurer {

    @Autowired
    private AdminServerProperties adminServerProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String contextPath = adminServerProperties.getContextPath();
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(contextPath + "/");

        http.headers().frameOptions().disable()
                .and().authorizeRequests()
                .antMatchers(contextPath + "/assets/**"
                        , contextPath + "/druid/**"
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
                .csrf();
        return http.build();
    }

}
