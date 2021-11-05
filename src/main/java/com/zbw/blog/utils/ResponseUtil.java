package com.zbw.blog.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zbw.blog.AppResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 17587
 */
public class ResponseUtil {
    
    public static void writeError(int code, String message, HttpServletResponse response){
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.append(new ObjectMapper().writeValueAsString(AppResponse.onError(code, message)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static <T> void writeSuccess(T data, HttpServletResponse response){
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.append(new ObjectMapper().writeValueAsString(AppResponse.onSuccess(data)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
