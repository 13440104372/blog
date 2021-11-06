package com.zbw.blog.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * 邮箱验证码校验异常
 * @author 17587
 */
public class BadEmailCodeException extends AuthenticationException {
    public BadEmailCodeException(String msg) {
        super(msg);
    }
}
