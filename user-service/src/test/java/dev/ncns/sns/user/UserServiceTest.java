package dev.ncns.sns.user;

import dev.ncns.sns.user.domain.AuthType;
import dev.ncns.sns.user.domain.Status;
import dev.ncns.sns.user.domain.Users;
import dev.ncns.sns.user.dto.response.UserResponseDto;
import dev.ncns.sns.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest extends UserApplicationTests{
    @Autowired
    private UserService userService;

    String account = "account";
    String nickname = "nick";
    String email = "nick@gmail.com";
    String password = "1234";

    @Test
    void signUp() throws Exception {
        Users user = Users.builder()
                .accountName(account)
                .nickname(nickname)
                .email(email)
                .password(password)
                .status(Status.USER)
                .authType(AuthType.LOCAL)
                .build();
        userService.signUp(user);
    }

    @Test
    void getUserProfile() throws Exception {
        UserResponseDto user = userService.getUserInfo(1L);
        System.out.println(user);
        assertEquals("account",user.getAccountName());
    }

    @Test
    void getAllUserInfo() {
        List<UserResponseDto> userList = userService.getAllUserInfo();
        for (UserResponseDto user:userList) {
            System.out.println(user.getId());
        }
    }
}