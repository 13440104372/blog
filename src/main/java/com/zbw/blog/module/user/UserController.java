package com.zbw.blog.module.user;

import com.zbw.blog.AppResponse;
import com.zbw.blog.pojo.User;
import com.zbw.blog.security.LoginUser;
import com.zbw.blog.utils.CodeGenerator;
import com.zbw.blog.utils.MailUtil;
import com.zbw.blog.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author 17587
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    private SessionRegistry sessionRegistry;

    private StringRedisTemplate redisTemplate;

    private MailUtil mailUtil;

    private final SimpleDateFormat sdf = new SimpleDateFormat();

    @PostMapping("/getUserInfo")
    public AppResponse<User> getUserInfo(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getDetails();
        return AppResponse.onSuccess(loginUser.getUser());
    }

    /**
     * 获取系统在线用户
     */
    @GetMapping("/getOnlineUser")
    @PreAuthorize("hasRole('master')")
    public AppResponse<List<Object>> getOnlineUser() {
        List<Object> list = sessionRegistry.getAllPrincipals();
        return AppResponse.onSuccess(list);
    }

    /**
     * 获取登录验证码
     * 每个ip每1分钟只能获取一次
     *
     * @param email 邮箱
     */
    @GetMapping("/getEmailLoginCode")
    public AppResponse<Boolean> getEmailLoginCode(String email, HttpServletRequest request) throws Exception {
        if (!isValidEmail(email)) {
            return AppResponse.onError(-1, "邮箱格式错误");
        }
        String ip = RequestUtil.getIpAddress(request);
        String code = CodeGenerator.getCode();
        Instant now = Instant.now();
        ValueOperations<String, String> forValue = redisTemplate.opsForValue();
        String lastTime = forValue.get(ip + "_lastLoginTime");
        if (lastTime != null) {
            Instant last = Instant.parse(lastTime);
            if (Duration.between(last, now).getSeconds() <= 60L) {
                return AppResponse.onError(-1, "距离上次获取不足一分钟，请勿重复获取");
            }
        }
        forValue.set(email + "_login", code);
        redisTemplate.expire(email + "_login", 5, TimeUnit.MINUTES);
        Map<String, Object> dataMap = new HashMap<>(4);
        dataMap.put("email", email);
        dataMap.put("code", code);
        dataMap.put("createTime", sdf.format(new Date()));
        dataMap.put("time", 5);
        String subject = "用户登录";
        String emailTemplate = "emailTemplateForLogin";
        //发送邮件
        mailUtil.sendTemplateMail(email, subject, emailTemplate, dataMap);
        forValue.set(ip + "_lastLoginTime", now.toString());
        return AppResponse.onSuccess(true);
    }

    private boolean isValidEmail(String email) {
        if ((email != null) && (!email.isEmpty())) {
            return Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
        }
        return false;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }
}
