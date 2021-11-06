package com.zbw.blog.security.providers;

import com.zbw.blog.enums.LoginType;
import com.zbw.blog.exceptions.RsaException;
import com.zbw.blog.security.DefaultUserDetailsServiceImpl;
import com.zbw.blog.security.LoginUser;
import com.zbw.blog.utils.RsaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 用户密码登录认证提供者<br>
 * 仅支持{@link UsernamePasswordAuthenticationToken}的认证信息
 * @author 17587
 */
@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private DefaultUserDetailsServiceImpl userDetailsService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private RsaUtil rsaUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserDetails userDetails = userDetailsService.loadUserByUsername(account);
        try {
            String decodedPwd = rsaUtil.decrypt(password);
            //对加密密码进行验证
            if (bCryptPasswordEncoder.matches(decodedPwd, userDetails.getPassword())) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                if(userDetails instanceof LoginUser){
                    ((LoginUser) userDetails).eraseCredentials();
                    ((LoginUser) userDetails).setLoginType(LoginType.Password.getType());
                }
                authenticationToken.setDetails(userDetails);
                return authenticationToken;
            } else {
                throw new BadCredentialsException("用户名或密码错误");
            }
        } catch (Exception e) {
            throw new RsaException(e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Autowired
    public void setUserDetailsService(DefaultUserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setBCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    public void setRsaUtil(RsaUtil rsaUtil) {
        this.rsaUtil = rsaUtil;
    }
}
