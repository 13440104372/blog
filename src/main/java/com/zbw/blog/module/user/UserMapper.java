package com.zbw.blog.module.user;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbw.blog.pojo.Permission;
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
}
