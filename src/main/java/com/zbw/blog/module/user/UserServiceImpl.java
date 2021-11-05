package com.zbw.blog.module.user;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zbw.blog.pojo.Permission;
import com.zbw.blog.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 17587
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    @Override
    public User findUserByAccountOrEmail(String key) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.eq(User::getAccount, key).or().eq(User::getEmail,key);
        return userMapper.selectOne(userLambdaQueryWrapper);
    }

    @Override
    public List<Permission> getUserPermission(Long userId) {
        return userMapper.getUserPermissions(userId);
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


}
