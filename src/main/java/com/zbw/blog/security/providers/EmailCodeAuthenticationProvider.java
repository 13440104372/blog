package com.zbw.blog.security.providers;

import com.zbw.blog.enums.LoginType;
import com.zbw.blog.exceptions.BadEmailCodeException;
import com.zbw.blog.security.DefaultUserDetailsServiceImpl;
import com.zbw.blog.security.EmailCodeAuthenticationToken;
import com.zbw.blog.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author 17587
 */
@Component
public class EmailCodeAuthenticationProvider implements AuthenticationProvider {

    private DefaultUserDetailsServiceImpl userDetailsService;

    private StringRedisTemplate redisTemplate;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String code = (String) authentication.getCredentials();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        ValueOperations<String, String> forValue = redisTemplate.opsForValue();
        String oldCode = forValue.get(email + "_login");
        if (oldCode == null) {
            throw new BadEmailCodeException("验证码不存在！");
        }
        else if (oldCode.equals(code)) {
            redisTemplate.delete(email + "_login");
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            if(userDetails instanceof LoginUser){
                ((LoginUser) userDetails).eraseCredentials();
                ((LoginUser) userDetails).setLoginType(LoginType.EmailCode.getType());
            }
            authenticationToken.setDetails(userDetails);
            return authenticationToken;
        }
        else {
            throw new BadEmailCodeException("验证码错误！");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(EmailCodeAuthenticationToken.class);
    }

    @Autowired
    public void setUserDetailsService(DefaultUserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
