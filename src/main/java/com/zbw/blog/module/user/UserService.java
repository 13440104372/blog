package com.zbw.blog.module.user;


import com.zbw.blog.pojo.Permission;
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
}
