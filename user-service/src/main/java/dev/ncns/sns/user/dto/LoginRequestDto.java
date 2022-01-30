package dev.ncns.sns.user.dto;

import dev.ncns.sns.user.domain.AuthType;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String email;
    private String accountName;
    private String password;
    private AuthType authType;
}