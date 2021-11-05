package com.zbw.blog.security.filters;

import com.zbw.blog.AppResponseCode;
import com.zbw.blog.enums.LoginType;
import com.zbw.blog.exceptions.RsaException;
import com.zbw.blog.exceptions.UnsupportedLoginTypeException;
import com.zbw.blog.security.EmailCodeAuthenticationToken;
import com.zbw.blog.security.LoginUser;
import com.zbw.blog.utils.JwtProvider;
import com.zbw.blog.utils.RequestUtil;
import com.zbw.blog.utils.ResponseUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static com.zbw.blog.AppResponseCode.*;

/**
 * jwt登录认证过滤器
 *
 * @author 17587
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * @param defaultFilterProcessesUrl 前端请求登录地址
     * @param authenticationManager     认证管理器
     */
    public JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse) throws AuthenticationException {
        String type = httpServletRequest.getParameter("loginType");
        if (type == null || "".equals(type)) {
            throw new UnsupportedLoginTypeException("登录参数loginType不能为空！");
        }
        if(LoginType.Password.getType().equals(type)){
            //获取用户信息
            String account = httpServletRequest.getParameter("account");
            String password = httpServletRequest.getParameter("password");
            //提交给AuthenticationProvider认证
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(account, password));
        }else if(LoginType.EmailCode.getType().equals(type)){
            String email = httpServletRequest.getParameter("email");
            String code = httpServletRequest.getParameter("code");
            return getAuthenticationManager().authenticate(new EmailCodeAuthenticationToken(email, code));
        }else {
            StringBuilder stringBuilder = new StringBuilder();
            for (LoginType value : LoginType.values()) {
                stringBuilder.append(value.getType()).append(',');
            }
            throw new UnsupportedLoginTypeException("登录参数loginType只支持以下值："+ stringBuilder + "错误的值："+type);
        }
    }

    /**
     * 认证成功颁发token，filterChain不进行下一步
     */
    @Override
    public void successfulAuthentication(HttpServletRequest request,
                                         HttpServletResponse response,
                                         FilterChain chain,
                                         Authentication authResult) {
        String userName = (String) authResult.getPrincipal();
        BeanFactory factory = WebApplicationContextUtils
                .getRequiredWebApplicationContext(request.getServletContext());
        JwtProvider jwtProvider = factory.getBean(JwtProvider.class);
        RedisTemplate<String, String> redisTemplate = factory.getBean(StringRedisTemplate.class);
        String key = userName + jwtProvider.getRedisKeySuffix();
        String jwt;
        // 已有颁发过的未过期token
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            jwt = redisTemplate.boundValueOps(key).get();
        } else {
            // 生成token
            jwt = jwtProvider.generateToken(userName);
            // 将token存入redis，使token状态化
            redisTemplate.boundValueOps(key).set(jwt);
            redisTemplate.boundValueOps(key).expire(jwtProvider.getExpireDay(), TimeUnit.DAYS);
        }
        // 将token放入响应头
        response.setHeader(jwtProvider.getHeader(), jwt);
        ResponseUtil.writeSuccess(true, response);
        // 设置登录ip
        if(authResult.getDetails() instanceof LoginUser){
            ((LoginUser) authResult.getDetails()).setLoginIp(RequestUtil.getIpAddress(request));
        }
        SecurityContextHolder.getContext().setAuthentication(authResult);
    }

    /**
     * 认证失败
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        if (e instanceof AccountExpiredException) {
            ResponseUtil.writeError(NEED_LOGIN, "账号已过期", response);
        } else if (e instanceof BadCredentialsException) {
            ResponseUtil.writeError(NEED_LOGIN, "用户名或密码错误", response);
        } else if (e instanceof CredentialsExpiredException) {
            ResponseUtil.writeError(NEED_LOGIN, "密码已过期", response);
        } else if (e instanceof DisabledException) {
            ResponseUtil.writeError(NEED_LOGIN, "用户未启用", response);
        } else if (e instanceof LockedException) {
            ResponseUtil.writeError(NEED_LOGIN, "账号被锁定", response);
        } else if (e instanceof InternalAuthenticationServiceException) {
            ResponseUtil.writeError(NEED_LOGIN, "用户不存在", response);
        } else if (e instanceof RsaException) {
            ResponseUtil.writeError(RSA_VALIDATION, "RAS验证失败", response);
        } else if (e instanceof UnsupportedLoginTypeException) {
            ResponseUtil.writeError(UNSUPPORTED_LOGIN_TYPE, e.getMessage(), response);
        } else {
            ResponseUtil.writeError(NEED_LOGIN, e.getMessage(), response);
        }
    }
}
