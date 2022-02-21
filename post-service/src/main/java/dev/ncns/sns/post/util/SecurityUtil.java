package dev.ncns.sns.post.util;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static Long getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new UnauthorizedException(ResponseType.REQUEST_UNAUTHORIZED);
        }
        return Long.parseLong(authentication.getName());
    }

}
