package dev.ncns.sns.auth.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.Cookie;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CookieManagerTest {

    private String name;

    private String value;

    @Spy
    private CookieManager cookieManager;

    @Mock
    private MockHttpServletRequest httpServletRequest;

    @BeforeEach
    private void beforeTest() {
        name = "TestCookie";
        value = "This is test cookie!";
    }

    @Test
    public void cookieTest() {
        Cookie[] cookies = {cookieManager.createCookie(name, value)};

        when(httpServletRequest.getCookies()).thenReturn(cookies);

        Cookie cookie = cookieManager.getCookie(httpServletRequest, name);
        System.out.println("Cookie : " + cookie.getName() + " - " + cookie.getValue());
    }

}
