package com.zbw.blog.security;

import com.zbw.blog.module.user.UserService;
import com.zbw.blog.pojo.Permission;
import com.zbw.blog.pojo.Role;
import com.zbw.blog.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 认证信息服务
 * @author 17587
 */
@Component
public class DefaultUserDetailsServiceImpl implements UserDetailsService {
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.findUserByAccountOrEmail(s);
        if (user == null) {
            throw new UsernameNotFoundException("登录用户：" + s + " 不存在");
        }
        List<Role> roles = userService.getUserRole(user.getId());
        List<Permission> permissions = userService.getUserPermission(user.getId());
        return new LoginUser(user,roles,permissions, LocalDateTime.now());
    }


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
