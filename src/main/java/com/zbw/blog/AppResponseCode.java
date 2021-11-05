package com.zbw.blog;

/**
 * @author 17587
 */
public class AppResponseCode {

    /**
     * 请求成功
     */
    public static final int SUCCESS_CODE = 200;
    /**
     * 需要登录，没有token或者token验证失败都等
     */
    public static final int NEED_LOGIN = 1001;
    /**
     * 权限不足
     */
    public static final int ACCESS_DENIED = 1002;
    /**
     * 会话过期，如超过session最大限制数
     */
    public static final int SESSION_EXPIRED = 1003;

    /**
     * 验证码校验失败
     */
    public static final int VALIDATE_CODE = 1004;
}
