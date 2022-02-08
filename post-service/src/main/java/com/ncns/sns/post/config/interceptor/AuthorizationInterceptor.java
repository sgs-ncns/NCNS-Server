package com.ncns.sns.post.config.interceptor;

import dev.ncns.sns.common.annotation.NonAuthorize;
import dev.ncns.sns.common.util.Constants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isNonAuthorize(handler)) {
            return true;
        }
        String userId = request.getHeader(Constants.USER_HEADER_KEY);
        if (!StringUtils.hasText(userId)) {
            return false;
        }
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(userId));
        return true;
    }

    private boolean isNonAuthorize(Object handler) {
        NonAuthorize nonAuthorize = ((HandlerMethod) handler).getMethodAnnotation(NonAuthorize.class);
        return nonAuthorize != null;
    }

    private Authentication getAuthentication(String userId) {
        UserDetails userDetails = User.builder().username(userId).password("").roles("").build();
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
