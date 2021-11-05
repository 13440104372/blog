package com.zbw.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zbw.blog.AppResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.zbw.blog.AppResponseCode.NEED_LOGIN;

/**
 * 匿名用户访问需要登录的资源
 * @author 17587
 */
@Component
public class AnonymousAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e){
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer;
        try {
            writer = response.getWriter();
            writer.append(new ObjectMapper().writeValueAsString(AppResponse.onError(NEED_LOGIN,"当前未登录，请登录后再操作")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
