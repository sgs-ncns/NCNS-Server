package dev.ncns.sns.user.config.interceptor;

import dev.ncns.sns.common.annotation.NonAuthorize;
import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.UnauthorizedException;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        /**
         * 인증이 필요하지 않은 요청, @NonAuthorize를 포함하였다면 pass 합니다.
         */
        if (isNonAuthorize(handler)) {
            return true;
        }
        /**
         * Authorized-User Header에 담긴 userId 값을 가져옵니다.
         * Spring SecurityContext에 저장하여 필요시 꺼내 사용할 수 있도록 합니다.
         */
        String userId = request.getHeader(Constants.USER_HEADER_KEY);
        if (!StringUtils.hasText(userId)) {
            throw new UnauthorizedException(ResponseType.REQUEST_UNAUTHORIZED);
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
