package com.zbw.blog.module.open;

import com.zbw.blog.AppResponse;
import com.zbw.blog.utils.RsaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RSA公钥
 * @author 17587
 */
@RequestMapping("/open")
@RestController
public class RsaKeyController {

    private RsaUtil rsaUtil;

    /**
     * 获取RSA公钥
     * @return RSA公钥字符串
     */
    @GetMapping("/getPublicRsaKey")
    public AppResponse<String> getPublicRsaKey(){
        return AppResponse.onSuccess(rsaUtil.getDefaultPublicKey());
    }

    @Autowired
    public void setRsaUtil(RsaUtil rsaUtil) {
        this.rsaUtil = rsaUtil;
    }
}
