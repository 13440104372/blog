package com.zbw.blog.module.open;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 图片验证码
 * @author 17587
 */
@RequestMapping("/open")
@RestController
public class ImageCodeController {

    public static final String SESSION_KEY_IMAGE_CODE = "session_key_image_code";

    /**
     * 获取图形验证码
     */
    @GetMapping("/getImageCode")
    public void getImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //设置response响应
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(100, 40, 4, 20);
        //将验证码放到HttpSession里面
        request.getSession().setAttribute(SESSION_KEY_IMAGE_CODE, captcha.getCode());
        //图形验证码写出，可以写出到文件，也可以写出到流
        //输出浏览器
        OutputStream out = response.getOutputStream();
        captcha.write(out);
        out.flush();
        out.close();
    }
}
