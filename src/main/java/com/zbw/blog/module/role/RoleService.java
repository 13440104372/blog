package com.zbw.blog.module.role;

import com.zbw.blog.pojo.Role;

/**
 * @author 17587
 */
public interface RoleService {

    /**
     * 通过角色名查找角色
     * @param roleName 角色名
     * @return 角色
     */
    Role findRoleByName(String roleName);
}
