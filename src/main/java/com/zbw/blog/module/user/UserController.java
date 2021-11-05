package com.zbw.blog.module.user;

import com.zbw.blog.AppResponse;
import com.zbw.blog.pojo.User;
import com.zbw.blog.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 17587
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private SessionRegistry sessionRegistry;

    @PostMapping("/getUserInfo")
    @PreAuthorize("hasAnyAuthority('add')")
    public AppResponse<User> getUserInfo(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getDetails();
        return AppResponse.onSuccess(loginUser.getUser());
    }

    /**
     * 获取系统在线用户
     */
    @GetMapping("/getOnlineUser")
    public AppResponse<List<Object>> getOnlineUser() {
        List<Object> list = sessionRegistry.getAllPrincipals();
        return AppResponse.onSuccess(list);
    }


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }
}
