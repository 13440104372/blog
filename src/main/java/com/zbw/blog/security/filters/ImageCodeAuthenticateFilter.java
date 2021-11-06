package com.zbw.blog.security.filters;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zbw.blog.AppResponseCode;
import com.zbw.blog.enums.LoginType;
import com.zbw.blog.module.open.ImageCodeController;
import com.zbw.blog.utils.ResponseUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 密码登录时验证图片验证码
 * @author 17587
 */
public class ImageCodeAuthenticateFilter extends OncePerRequestFilter {
    private final RequestMatcher requestMatcher;

    public ImageCodeAuthenticateFilter(@NotNull String defaultFilterProcessesUrl, @NotNull String method) {
        requestMatcher = new AntPathRequestMatcher(defaultFilterProcessesUrl,method);
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        if (LoginType.Password.getType().equals(request.getParameter("loginType"))
                && requestMatcher.matches(request)) {
            HttpSession session = request.getSession();
            String code = request.getParameter("code");
            String sessionCode = (String) session.getAttribute(ImageCodeController.SESSION_KEY_IMAGE_CODE);
            if (StringUtils.isBlank(code)) {
                ResponseUtil.writeError(AppResponseCode.VALIDATE_CODE, "验证码不能为空！", response);
                return;
            }
            if (sessionCode == null) {
                ResponseUtil.writeError(AppResponseCode.VALIDATE_CODE, "验证码不存在！", response);
                return;
            }
            if (!code.equalsIgnoreCase(sessionCode)) {
                ResponseUtil.writeError(AppResponseCode.VALIDATE_CODE, "验证码不正确！", response);
                return;
            }
            session.removeAttribute(ImageCodeController.SESSION_KEY_IMAGE_CODE);
        }
        filterChain.doFilter(request, response);
    }
}
