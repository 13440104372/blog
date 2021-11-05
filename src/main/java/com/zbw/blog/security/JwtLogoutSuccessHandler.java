package com.zbw.blog.security;

import com.zbw.blog.utils.JwtProvider;
import com.zbw.blog.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 17587
 */
@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
    private JwtProvider jwtProvider;
    private StringRedisTemplate redisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        if(authentication!=null){
            // 删除redis储存的token
            String userName = (String) authentication.getPrincipal();
            redisTemplate.delete(userName+jwtProvider.getRedisKeySuffix());
            new SecurityContextLogoutHandler().logout(httpServletRequest,httpServletResponse,authentication);
        }
        ResponseUtil.writeSuccess(true,httpServletResponse);
    }

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
