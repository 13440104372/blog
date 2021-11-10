package com.zbw.blog.module.user;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zbw.blog.module.role.RoleMapper;
import com.zbw.blog.pojo.Permission;
import com.zbw.blog.pojo.Role;
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
    private RoleMapper roleMapper;

    @Override
    public User findUserByAccountOrEmail(String key) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.eq(User::getAccount, key).or().eq(User::getEmail, key);
        return userMapper.selectOne(userLambdaQueryWrapper);
    }

    @Override
    public List<Permission> getUserPermission(Long userId) {
        return userMapper.getUserPermissions(userId);
    }

    @Override
    public List<Role> getUserRole(Long userId) {
        return userMapper.getUserRole(userId);
    }

    @Override
    public boolean registerAccount(User user, Role role) {
        if (userMapper.registerUser(user) > 0) {
            return roleMapper.addUserRoleRelation(user.getId(), role.getId()) > 0;
        }
        return false;
    }

    @Override
    public User getUserByEmail(String email) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.eq(User::getEmail, email);
        return userMapper.selectOne(userLambdaQueryWrapper);
    }

    @Override
    public User getUserByAccount(String account) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.eq(User::getAccount, account);
        return userMapper.selectOne(userLambdaQueryWrapper);
    }

    @Override
    public boolean activateAccount(String account, String email) {
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = Wrappers.lambdaUpdate();
        userLambdaUpdateWrapper
                .eq(User::getAccount, account)
                .set(User::getEmail, email)
                .set(User::getIsEnabled, 1);
        return userMapper.update(null, userLambdaUpdateWrapper) > 0;
    }

    @Override
    public boolean updateAvatar(String path, Long userId) {
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = Wrappers.lambdaUpdate();
        userLambdaUpdateWrapper
                .eq(User::getId, userId)
                .set(User::getAvatar, path);
        return userMapper.update(null, userLambdaUpdateWrapper) > 0;
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.updateUser(user) > 0;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Autowired
    public void setRoleMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }
}
