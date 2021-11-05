package com.zbw.blog.configuration;

import com.zbw.blog.security.AnonymousAuthenticationEntryPoint;
import com.zbw.blog.security.DefaultAuthenticationProvider;
import com.zbw.blog.security.DefaultExpiredSessionStrategy;
import com.zbw.blog.security.JwtLogoutSuccessHandler;
import com.zbw.blog.security.filters.ImageCodeAuthenticateFilter;
import com.zbw.blog.security.filters.JwtAuthenticationFilter;
import com.zbw.blog.security.filters.JwtLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;


/**
 * SpringSecurity配置
 *
 * @author 17587
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 匿名用户访问无权限资源
     */
    private AnonymousAuthenticationEntryPoint anonymousAuthenticationEntryPoint;

    /**
     * 认证提供者
     */
    private DefaultAuthenticationProvider authenticationProvider;

    /**
     * jwt退出成功处理器
     */
    private JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    /**
     * 会话过期处理
     */
    private DefaultExpiredSessionStrategy expiredSessionStrategy;

    /**
     * 不走过滤器的接口
     */
    private final String[] antMatchers = {"/open/**"};

    /**
     * 不走jwt认证过滤器的接口
     */
    private final String[] jwtExcludeUrls = {"/loginByPwd"};

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 设置默认的加密方式（强hash方式加密）
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.eraseCredentials(true).authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(antMatchers);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                // 未登录请求资源
                .exceptionHandling()
                .authenticationEntryPoint(anonymousAuthenticationEntryPoint)

                .and()
                .authorizeRequests()
                // 拦截所有请求
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(new ImageCodeAuthenticateFilter("/loginByPwd"),LogoutFilter.class)
                .addFilterBefore(new JwtLoginFilter("/loginByPwd", authenticationManager()), LogoutFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(jwtExcludeUrls), LogoutFilter.class)
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(jwtLogoutSuccessHandler)

                .and()
                .sessionManagement()
                // 最大会话数量
                .maximumSessions(1)
                // 超过最大会话数量是否限制登录
                .maxSessionsPreventsLogin(false)
                .expiredSessionStrategy(expiredSessionStrategy)
                .sessionRegistry(sessionRegistry());

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
    public void setJwtLogoutSuccessHandler(JwtLogoutSuccessHandler jwtLogoutSuccessHandler) {
        this.jwtLogoutSuccessHandler = jwtLogoutSuccessHandler;
    }

    @Autowired
    public void setExpiredSessionStrategy(DefaultExpiredSessionStrategy expiredSessionStrategy) {
        this.expiredSessionStrategy = expiredSessionStrategy;
    }
}

