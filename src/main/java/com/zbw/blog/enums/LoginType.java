package com.zbw.blog.enums;

import org.jetbrains.annotations.NotNull;

/**
 * 登录类型
 * @author 17587
 */
public enum LoginType {

    /**
     * 密码登录
     */
    Password("password"),
    /**
     * 邮箱验证码登录
     */
    EmailCode("emailCode");

    private final String type;

    LoginType(@NotNull String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "LoginType{" +
                "type='" + type + '\'' +
                '}';
    }
}
