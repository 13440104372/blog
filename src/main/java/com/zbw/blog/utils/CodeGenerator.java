package com.zbw.blog.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author 17587
 */
public class CodeGenerator {


    private static final String SYMBOLS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();

    /**
     * 生成6位随机验证码
     *
     * @return 返回4位验证码
     */
    public static String getCode() {
        char[] nonceChars = new char[6];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }


}
