package com.zbw.blog.module.role;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zbw.blog.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 17587
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private RoleMapper roleMapper;

    @Override
    public Role findRoleByName(String roleName) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = Wrappers.lambdaQuery();
        roleLambdaQueryWrapper.eq(Role::getName,roleName);
        return roleMapper.selectOne(roleLambdaQueryWrapper);
    }

    @Autowired
    public void setRoleMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }
}
