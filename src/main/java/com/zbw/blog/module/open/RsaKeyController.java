package com.zbw.blog.module.open;

import com.zbw.blog.AppResponse;
import com.zbw.blog.utils.RsaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 17587
 */
@RequestMapping("/open")
@RestController
public class RsaKeyController {

    private RsaUtil rsaUtil;

    @GetMapping("/getPublicRsaKey")
    public AppResponse<String> getPublicRsaKey(){
        return AppResponse.onSuccess(rsaUtil.getDefaultPublicKey());
    }

    @Autowired
    public void setRsaUtil(RsaUtil rsaUtil) {
        this.rsaUtil = rsaUtil;
    }
}
