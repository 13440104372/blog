package com.zbw.blog.security;

import com.zbw.blog.AppResponseCode;
import com.zbw.blog.utils.ResponseUtil;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

/**
 * @author 17587
 */
@Component
public class DefaultExpiredSessionStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) {
        ResponseUtil.writeError(AppResponseCode.SESSION_EXPIRED,"登录已过期", event.getResponse());
    }
}
