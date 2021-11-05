package com.zbw.blog.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @author 17587
 */
public class BadEmailCodeException extends AuthenticationException {
    public BadEmailCodeException(String msg) {
        super(msg);
    }
}
