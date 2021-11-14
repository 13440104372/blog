package com.zbw.blog.module.open;

import com.zbw.blog.AppResponse;
import com.zbw.blog.module.role.RoleService;
import com.zbw.blog.module.user.UserService;
import com.zbw.blog.pojo.Role;
import com.zbw.blog.pojo.User;
import com.zbw.blog.utils.CodeGenerator;
import com.zbw.blog.utils.JwtProvider;
import com.zbw.blog.utils.MailUtil;
import com.zbw.blog.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.zbw.blog.AppResponseCode.NOT_FOUND;

/**
 * 有关user的开放接口
 * @author 17587
 */
@RestController
@RequestMapping("/open/user")
public class OpenedUserController {

    private StringRedisTemplate redisTemplate;
    private MailUtil mailUtil;
    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;
    private RoleService roleService;
    private JwtProvider jwtProvider;
    private final SimpleDateFormat sdf = new SimpleDateFormat();
    private final String defaultRegisterRole = "normal_user";

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

    /**
     * 注册
     *
     * @param user 用户信息
     * @return 注册成功返回TRUE
     */
    @PostMapping("/register")
    public AppResponse<Boolean> register(@RequestBody User user) {
        User local = userService.findUserByAccountOrEmail(user.getAccount());
        if (local != null) {
            return AppResponse.onError(-1, "账号已存在！");
        }
        Role role = roleService.findRoleByName(defaultRegisterRole);
        if (null == role) {
            return AppResponse.onError(-1, "角色未找到！");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return AppResponse.onSuccess(userService.registerAccount(user, role));
    }

    /**
     * 获取激活码
     * 每个ip每1分钟只能获取一次
     *
     * @param email 邮箱
     */
    @GetMapping("/getActivateCode")
    public AppResponse<Boolean> getActivateCode(String email, String account, HttpServletRequest request) throws Exception {
        if (isValidEmail(email)) {
            User user = userService.getUserByEmail(email);
            if (user != null) {
                return AppResponse.onError(-1, "该邮箱已被注册");
            }
            user = userService.getUserByAccount(account);
            if (user == null) {
                return AppResponse.onError(-1, "无效账号");
            } else if (user.getIsEnabled() != 0) {
                return AppResponse.onError(-1, "该账号已被激活，请勿重复激活");
            }
        } else {
            return AppResponse.onError(-1, "邮箱格式错误");
        }
        String ip = RequestUtil.getIpAddress(request);
        String code = CodeGenerator.getCode();
        Instant now = Instant.now();
        ValueOperations<String, String> forValue = redisTemplate.opsForValue();
        String lastTime = forValue.get(ip + "_lastActivateTime");
        if (lastTime != null) {
            Instant last = Instant.parse(lastTime);
            if (Duration.between(last, now).getSeconds() <= 60) {
                return AppResponse.onError(-1, "距离上次获取不足一分钟，请勿重复获取");
            }
        }
        forValue.set(email + "_activate", code);
        forValue.set(email + "_account", account);
        redisTemplate.expire(email + "_activate", 30, TimeUnit.MINUTES);
        redisTemplate.expire(email + "_account", 30, TimeUnit.MINUTES);
        Map<String, Object> dataMap = new HashMap<>(4);
        dataMap.put("email", email);
        dataMap.put("code", code);
        dataMap.put("createTime", sdf.format(new Date()));
        dataMap.put("time", 30);
        String subject = "用户注册";
        String emailTemplate = "emailTemplateForRegister";
        //发送邮件
        mailUtil.sendTemplateMail(email, subject, emailTemplate, dataMap);
        forValue.set(ip + "_lastActivateTime", now.toString());
        return AppResponse.onSuccess(true);
    }

    /**
     * 激活账号（绑定邮箱）
     *
     * @param email 邮箱
     * @param code  激活码
     * @return 激活成功返回true
     */
    @PostMapping("/activate")
    public AppResponse<Boolean> activate(String email, String code, HttpServletResponse response) {
        if (!isValidEmail(email)) {
            return AppResponse.onError(-1, "邮箱格式错误");
        }
        ValueOperations<String, String> forValue = redisTemplate.opsForValue();
        String account = forValue.get(email + "_account");
        String oldCode = forValue.get(email + "_activate");
        if (oldCode == null) {
            return AppResponse.onError(-1, "验证码不存在或者过期！");
        } else if (oldCode.equals(code)) {
            if (userService.activateAccount(account, email)) {
                String token = jwtProvider.generateToken(account);
                redisTemplate.boundValueOps(jwtProvider.getRedisKeyPrefix()+account ).set(token);
                redisTemplate.delete(email + "_account");
                redisTemplate.delete(email + "_activate");
                response.setHeader(jwtProvider.getHeader(), token);
                return AppResponse.onSuccess(true);
            } else {
                return AppResponse.onError(-1, "激活失败！");
            }
        } else {
            return AppResponse.onError(-1, "激活码错误");
        }
    }

    @GetMapping("/findUserById")
    public AppResponse<User> findUserById(@RequestParam("id") Long userId){
        User user = userService.findUserById(userId);
        if(user == null){
            return AppResponse.onError(NOT_FOUND,"用户不存在");
        }
        user.setPassword(null);
        return AppResponse.onSuccess(user);
    }

    private boolean isValidEmail(String email) {
        if ((email != null) && (!email.isEmpty())) {
            return Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
        }
        return false;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }
}
