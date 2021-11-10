package com.zbw.blog.module.user;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbw.blog.pojo.Permission;
import com.zbw.blog.pojo.Role;
import com.zbw.blog.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 17587
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 查询用户权限
     * @param userId 用户id
     * @return 用户权限列表
     */
    List<Permission> getUserPermissions(@Param("userId") Long userId);

    /**
     * 注册
     * @param user 用户信息
     * @return 注册成功返回1
     */
    int registerUser(User user);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新成功返回1
     */
    int updateUser(User user);

    /**
     * 获取用户角色
     * @param userId 用户id
     * @return 角色列表
     */
    List<Role> getUserRole(@Param("userId") Long userId);
}
