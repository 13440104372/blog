package com.zbw.blog.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * RSA校验异常
 * @author 17587
 */
public class RsaException extends AuthenticationException {
    public RsaException(String msg) {
        super(msg);
    }
}
