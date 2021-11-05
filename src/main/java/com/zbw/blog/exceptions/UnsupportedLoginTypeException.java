package com.zbw.blog.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @author 17587
 */
public class UnsupportedLoginTypeException extends AuthenticationException {
    public UnsupportedLoginTypeException(String msg) {
        super(msg);
    }
}
