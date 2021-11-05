package com.zbw.blog.security;

import com.zbw.blog.pojo.Permission;
import com.zbw.blog.pojo.User;
import lombok.Data;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户信息拓展类
 *
 * @author 17587
 */
@Data
public class LoginUser implements UserDetails, CredentialsContainer {
    private final User user;
    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录时间
     */
    private final LocalDateTime loginTime;

    private final List<Permission> permissions;

    public LoginUser(User user, List<Permission> permissions,LocalDateTime loginTime) {
        this(user, permissions,null, loginTime);
    }


    public LoginUser(User user,List<Permission> permissions, String loginIp, LocalDateTime loginTime) {
        this.user = user;
        this.loginIp = loginIp;
        this.loginTime = loginTime;
        this.permissions = permissions;
    }

    /**
     * 认证完成后，擦除密码
     */
    @Override
    public void eraseCredentials() {
        user.setPassword(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        this.permissions.forEach(permission -> simpleGrantedAuthorities.add(new SimpleGrantedAuthority(permission.getName())));
        return simpleGrantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getAccount();
    }

    /**
     * 账户是否未过期，过期无法验证
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁，锁定的用户无法进行身份验证
     * <p>
     * 密码锁定
     * </p>
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否未过期的用户的凭据(密码)，过期的凭据防止认证
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用户是否被启用。禁用的用户无法进行身份验证。
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
