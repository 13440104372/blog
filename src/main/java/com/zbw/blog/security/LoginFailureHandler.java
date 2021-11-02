package com.zbw.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zbw.blog.AppResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        if (e instanceof AccountExpiredException) {
            write(-1,"账号已过期",response);
        } else if (e instanceof BadCredentialsException) {
            write(-1,"用户名或密码错误",response);
        } else if (e instanceof CredentialsExpiredException) {
            write(-1,"密码已过期",response);
        } else if (e instanceof DisabledException) {
            write(-1,"用户未启用",response);
        } else if (e instanceof LockedException) {
            write(-1,"账号被锁定",response);
        } else if (e instanceof InternalAuthenticationServiceException) {
            write(-1,"用户不存在",response);
        } else {
            write(-1,e.getMessage(),response);
        }
    }

    private void write(int code,String message,HttpServletResponse response){
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.append(new ObjectMapper().writeValueAsString(AppResponse.onError(code,message)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
