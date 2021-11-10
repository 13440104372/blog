package com.zbw.blog.configuration;

import com.zbw.blog.security.AnonymousAuthenticationEntryPoint;
import com.zbw.blog.security.providers.EmailCodeAuthenticationProvider;
import com.zbw.blog.security.providers.UsernamePasswordAuthenticationProvider;
import com.zbw.blog.security.DefaultExpiredSessionStrategy;
import com.zbw.blog.security.JwtLogoutSuccessHandler;
import com.zbw.blog.security.filters.ImageCodeAuthenticateFilter;
import com.zbw.blog.security.filters.JwtAuthenticationFilter;
import com.zbw.blog.security.filters.JwtLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
     * 用户名加密码登录认证提供者
     */
    private UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

    /**
     * 邮箱加验证码登录认证提供者
     */
    private EmailCodeAuthenticationProvider emailCodeAuthenticationProvider;

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
    private final String[] antMatchers = {
            "/open/**",
            "/user/getEmailLoginCode",
            "/user/register",
            "/user/getActivateCode",
            "/user/activate",
    };

    /**
     * 不走jwt认证过滤器的接口
     */
    private final String[] jwtExcludeUrls = {"/userLogin","/ftp/**"};

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
        auth
                .eraseCredentials(true)
                // 设置认证信息提供者
                .authenticationProvider(usernamePasswordAuthenticationProvider)
                .authenticationProvider(emailCodeAuthenticationProvider);
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
                .antMatchers(jwtExcludeUrls).permitAll()
                // 拦截所有请求
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(new ImageCodeAuthenticateFilter("/userLogin", HttpMethod.POST.toString()),LogoutFilter.class)
                .addFilterBefore(new JwtLoginFilter("/userLogin", authenticationManager()), LogoutFilter.class)
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
    public void setJwtLogoutSuccessHandler(JwtLogoutSuccessHandler jwtLogoutSuccessHandler) {
        this.jwtLogoutSuccessHandler = jwtLogoutSuccessHandler;
    }

    @Autowired
    public void setExpiredSessionStrategy(DefaultExpiredSessionStrategy expiredSessionStrategy) {
        this.expiredSessionStrategy = expiredSessionStrategy;
    }

    @Autowired
    public void setUsernamePasswordAuthenticationProvider(UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider) {
        this.usernamePasswordAuthenticationProvider = usernamePasswordAuthenticationProvider;
    }

    @Autowired
    public void setEmailCodeAuthenticationProvider(EmailCodeAuthenticationProvider emailCodeAuthenticationProvider) {
        this.emailCodeAuthenticationProvider = emailCodeAuthenticationProvider;
    }
}

