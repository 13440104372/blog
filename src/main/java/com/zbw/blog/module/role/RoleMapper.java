package com.zbw.blog.module.role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbw.blog.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 17587
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 为用户添加角色
     * @param userId 用户id
     * @param roleId 角色id
     * @return 添加的条数
     */
    int addUserRoleRelation(@Param("userId") Long userId,@Param("roleId") Long roleId);
}
