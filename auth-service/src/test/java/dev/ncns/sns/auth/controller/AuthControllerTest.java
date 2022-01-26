package dev.ncns.sns.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ncns.sns.auth.dto.request.LoginRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String email;

    private String account;

    private String password;

    @BeforeEach
    private void beforeTest() {
        email = "abc@gmail.com";
        account = "ncns123";
        password = "123456";
    }

    @Test
    public void localLoginTest() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, null, password, null);
        mockMvc.perform(post("/api/auth/local")
                        .content(objectMapper.writeValueAsString(loginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andDo(print());
    }

    @Test
    public void accountLoginTest() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto(null, account, password, null);
        mockMvc.perform(post("/api/auth/account")
                        .content(objectMapper.writeValueAsString(loginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andDo(print());
    }

}
