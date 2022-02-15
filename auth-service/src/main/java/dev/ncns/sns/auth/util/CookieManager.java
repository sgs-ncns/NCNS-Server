package dev.ncns.sns.auth.util;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.NotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
public class CookieManager {

    private static final int COOKIE_VALIDITY = 1000 * 60 * 60 * 24 * 15; // 15days
    private static final int COOKIE_EXPIRATION = 0;

    public Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(COOKIE_VALIDITY);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new NotFoundException(ResponseType.AUTH_NOT_FOUND_COOKIE_KEY));
    }

    public Cookie deleteCookie(Cookie cookie) {
        cookie.setValue(null);
        cookie.setMaxAge(COOKIE_EXPIRATION);
        cookie.setPath("/");
        return cookie;
    }

}
