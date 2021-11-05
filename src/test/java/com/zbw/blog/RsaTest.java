package com.zbw.blog;

import com.zbw.blog.utils.RsaUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RsaTest {

    @Autowired
    private RsaUtil util;

    @Test
    public void Test(){
        try {
            String m = util.encrypt("youknow?");
            System.out.println("加密  "+m);
            System.out.println("解密  "+util.decrypt(m));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
