package com.zbw.blog.configuration;

import com.zbw.blog.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 登录成功处理器
     */
    private LoginSuccessHandler loginSuccessHandler;
    /**
     * 登录失败处理器
     */
    private LoginFailureHandler loginFailureHandler;

    /**
     * 匿名用户访问无权限资源
     */
    private AnonymousAuthenticationEntryPoint anonymousAuthenticationEntryPoint;

    private DefaultAuthenticationProvider authenticationProvider;

    private DefaultUserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 设置默认的加密方式（强hash方式加密）
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http相关的配置，包括登入登出、异常处理、会话管理等
        http.cors().and().csrf().disable();
        http.authenticationProvider(authenticationProvider)
                // 未登录请求资源
                .exceptionHandling()
                .authenticationEntryPoint(anonymousAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .anyRequest()  //所有请求拦截
//                .mvcMatchers("/admin/**","/user/**")
                .authenticated();
        http
                .formLogin()
                //发送Ajax请求的路径
                .loginProcessingUrl("/login")
                //验证失败处理
                .failureHandler(loginFailureHandler)
                //验证用户名之后逻辑
                .successHandler(loginSuccessHandler)
                .permitAll();

    }

    @Autowired
    public void setLoginSuccessHandler(LoginSuccessHandler loginSuccessHandler) {
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Autowired
    public void setLoginFailureHandler(LoginFailureHandler loginFailureHandler) {
        this.loginFailureHandler = loginFailureHandler;
    }

    @Autowired
    public void setAnonymousAuthenticationEntryPoint(AnonymousAuthenticationEntryPoint anonymousAuthenticationEntryPoint) {
        this.anonymousAuthenticationEntryPoint = anonymousAuthenticationEntryPoint;
    }

    @Autowired
    public void setAuthenticationProvider(DefaultAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Autowired
    public void setUserDetailsService(DefaultUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}

