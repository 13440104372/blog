package com.zbw.blog;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class NormalTest {
    @Test
    public void test(){
        System.out.println(new BCryptPasswordEncoder().encode("youknow?"));
    }
}
