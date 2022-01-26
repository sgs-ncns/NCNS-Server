package dev.ncns.sns.auth.controller;

import dev.ncns.sns.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthControllerTest.class)
public class AuthControllerTest {

    @MockBean
    private AuthService authService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void xxxTest() {

    }

}
