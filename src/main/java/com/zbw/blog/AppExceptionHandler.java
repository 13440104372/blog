package com.zbw.blog;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.zbw.blog.AppResponseCode.ACCESS_DENIED;
import static com.zbw.blog.AppResponseCode.NOT_FOUND;

/**
 * 处理DispatcherServlet的异常
 * @author 17587
 */
@ControllerAdvice
@ResponseBody
public class AppExceptionHandler {
    /**
     * 400错误
     */
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public AppResponse<Exception> request400(Exception e) {
        return AppResponse.onError(400, e.getMessage());
    }

    @ExceptionHandler({
            NoHandlerFoundException.class
    })
    public AppResponse<Exception> request404(){
        return AppResponse.onError(NOT_FOUND, "请求地址错误,未找到资源");
    }

    /**
     * 405错误
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public AppResponse<Exception> request405(HttpRequestMethodNotSupportedException e) {
        return AppResponse.onError(405, e.getMessage());
    }

    /**
     * 406错误
     */
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public AppResponse<Exception> request406(HttpMediaTypeNotAcceptableException e) {
        return AppResponse.onError(406, e.getMessage());
    }

    /**
     * 500错误
     */
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public AppResponse<Exception> server500(RuntimeException e) {
        return AppResponse.onError(500, e.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public AppResponse<Exception> accessDenied(AccessDeniedException e){
        return AppResponse.onError(ACCESS_DENIED,e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public AppResponse<Exception> handleAll(Exception e){
        return  AppResponse.onError(-1,e.getMessage());
    }
}
