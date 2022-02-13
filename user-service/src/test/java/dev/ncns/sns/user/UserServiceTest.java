package dev.ncns.sns.user;

import dev.ncns.sns.user.domain.AuthType;
import dev.ncns.sns.user.dto.request.SignupRequestDto;
import dev.ncns.sns.user.dto.response.UserResponseDto;
import dev.ncns.sns.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest extends UserApplicationTests {
    @Autowired
    private UserService userService;

    String accountName = "king";
    String nickname = "king";
    String email = "king@gmail.com";
    String password = "1234";

    @Test
    void signUp() throws Exception {
        SignupRequestDto request = new SignupRequestDto(accountName, nickname, email, password, AuthType.LOCAL);
        userService.signUp(request);
    }

    @Test
    void getUserProfile() throws Exception {
        UserResponseDto user = userService.getUserInfo(2L);
        System.out.println(user.getAccountName());
        assertEquals("follower", user.getAccountName());
        assertEquals(2, user.getFollowingCount());
    }

}
