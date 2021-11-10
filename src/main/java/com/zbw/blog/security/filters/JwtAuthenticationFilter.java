package com.zbw.blog.security.filters;

import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zbw.blog.pojo.User;
import com.zbw.blog.security.DefaultUserDetailsServiceImpl;
import com.zbw.blog.security.LoginUser;
import com.zbw.blog.utils.JwtProvider;
import com.zbw.blog.utils.RequestUtil;
import com.zbw.blog.utils.ResponseUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.zbw.blog.AppResponseCode.NEED_LOGIN;


/**
 * jwt验证过滤器
 *
 * @author 17587
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final List<RequestMatcher> requestMatchers;

    public JwtAuthenticationFilter(@NotNull String... excludeProcessUrls){
        requestMatchers = new ArrayList<>();
        for (String s:excludeProcessUrls){
            requestMatchers.add(new AntPathRequestMatcher(s));
        }
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        boolean doFilter = true;
        for (RequestMatcher requestMatcher : requestMatchers) {
            if(requestMatcher.matches(request)){
                doFilter = false;
                break;
            }
        }
        if(doFilter){
            BeanFactory factory = WebApplicationContextUtils
                    .getRequiredWebApplicationContext(request.getServletContext());
            JwtProvider jwtProvider = factory.getBean(JwtProvider.class);
            String token = request.getHeader(jwtProvider.getHeader());
            if (token == null || token.isEmpty()) {
                ResponseUtil.writeError(NEED_LOGIN, "token不存在", response);
                return;
            }
            try {
                // 验证token
                DecodedJWT decodedJwt = jwtProvider.verifyToken(token);
                // 获取用户名
                String userName = decodedJwt.getClaim(User.TAG).asString();
                if(userName == null){
                    ResponseUtil.writeError(NEED_LOGIN, "用户不存在", response);
                    return;
                }
                // 验证token是否存在
                RedisTemplate<String, String> redisTemplate = factory.getBean(StringRedisTemplate.class);
                String redisToken = redisTemplate.boundValueOps(jwtProvider.getRedisKeyPrefix()+userName).get();
                if (!token.equals(redisToken)) {
                    ResponseUtil.writeError(NEED_LOGIN, "身份信息已过期或不存在，请重新登录", response);
                    return;
                }
                // 未登录
                if (null == SecurityContextHolder.getContext().getAuthentication()) {
                    DefaultUserDetailsServiceImpl userDetailsService = factory.getBean(DefaultUserDetailsServiceImpl.class);
                    // 从数据库查询user
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                    if (userDetails instanceof LoginUser) {
                        // 设置登录ip
                        ((LoginUser) userDetails).setLoginIp(RequestUtil.getIpAddress(request));
                        // 清除密码
                        ((LoginUser) userDetails).eraseCredentials();
                    }
                    authenticationToken.setDetails(userDetails);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (JWTVerificationException e) {
                if (e instanceof AlgorithmMismatchException) {
                    ResponseUtil.writeError(NEED_LOGIN, "加密算法不匹配", response);
                } else if (e instanceof SignatureVerificationException) {
                    ResponseUtil.writeError(NEED_LOGIN, "签名错误", response);
                } else if (e instanceof TokenExpiredException) {
                    ResponseUtil.writeError(NEED_LOGIN, "token已过期", response);
                } else if (e instanceof JWTDecodeException) {
                    ResponseUtil.writeError(NEED_LOGIN, "token验证失败", response);
                } else {
                    ResponseUtil.writeError(NEED_LOGIN, e.getMessage(), response);
                    e.printStackTrace();
                }
                // token验证失败就不继续执行过滤器，直接返回
                return;
            }
        }
        // 继续执行过滤器
        filterChain.doFilter(request, response);

    }
}
