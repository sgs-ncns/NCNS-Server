package dev.ncns.sns.auth.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
public class CookieManager {

    private static final int COOKIE_VALIDITY = 60 * 60; // 1hours

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
                .orElse(null);
    }

}
