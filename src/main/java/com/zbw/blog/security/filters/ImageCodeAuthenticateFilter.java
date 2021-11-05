package com.zbw.blog.security.filters;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zbw.blog.AppResponseCode;
import com.zbw.blog.module.open.ImageCodeController;
import com.zbw.blog.module.user.UserController;
import com.zbw.blog.utils.ResponseUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author 17587
 */
public class ImageCodeAuthenticateFilter extends OncePerRequestFilter {
    private final String defaultFilterProcessesUrl;

    public ImageCodeAuthenticateFilter(@NotNull String defaultFilterProcessesUrl) {
        this.defaultFilterProcessesUrl = defaultFilterProcessesUrl;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (defaultFilterProcessesUrl.equalsIgnoreCase(request.getRequestURI())
                && "post".equalsIgnoreCase(request.getMethod())) {
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
