package com.zbw.blog.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.zbw.blog.module.user.UserMapper;
import com.zbw.blog.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 认证信息服务
 */
@Component
public class DefaultUserDetailsService implements UserDetailsService {
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.eq(User::getAccount,s);
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        if(user == null){
            throw new UsernameNotFoundException("登录用户：" + s + " 不存在");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        return loginUser;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
