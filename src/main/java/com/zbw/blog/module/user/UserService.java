package com.zbw.blog.module.user;


import com.zbw.blog.pojo.Permission;
import com.zbw.blog.pojo.Role;
import com.zbw.blog.pojo.User;

import java.util.List;

/**
 * @author 17587
 */
public interface UserService {

    /**
     * 通过账号查找用户
     * @param key 邮箱或者账号
     * @return 用户
     */
    User findUserByAccountOrEmail(String key);

    /**
     * 查询用户权限
     * @param userId 用户id
     * @return 用户权限
     */
    List<Permission> getUserPermission(Long userId);

    /**
     * 查询用户权限
     * @param userId 用户id
     * @return 用户权限
     */
    List<Role> getUserRole(Long userId);

    /**
     * 注册用户
     * @param user 用户信息
     * @return 注册成功返回true
     */
    boolean registerAccount(User user, Role role);

    /**
     * 通过邮箱找用户
     * @param email 邮箱
     * @return 用户
     */
    User getUserByEmail(String email);

    /**
     * 通过账号找用户
     * @param account 账号
     * @return 用户
     */
    User getUserByAccount(String account);

    /**
     * 激活(绑定邮箱)
     * @param account 账号
     * @param email 邮箱
     * @return 激活成功返回true
     */
    boolean activateAccount(String account, String email);

    /**
     * 更新头像
     * @param path 头像地址
     * @param userId 用户id
     * @return 更新成功返回true
     */
    boolean updateAvatar(String path,Long userId);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新成功返回ture
     */
    boolean updateUser(User user);
}
