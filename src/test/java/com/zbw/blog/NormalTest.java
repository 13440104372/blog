package com.zbw.blog;

import com.zbw.blog.enums.LoginType;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

public class NormalTest {
    @Test
    public void test(){
        System.out.println(Arrays.toString(LoginType.values()));
    }
}
