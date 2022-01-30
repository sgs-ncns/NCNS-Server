package dev.ncns.sns.user.dto;

import dev.ncns.sns.user.domain.AuthType;
import dev.ncns.sns.user.domain.Status;
import dev.ncns.sns.user.domain.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SignupRequestDto {
    @NotBlank(message = "NAME_IS_MANDATORY")
    private String accountName;

    @NotBlank(message = "NAME_IS_MANDATORY")
    private String nickname;

    @NotBlank(message = "EMAIL_IS_MANDATORY")
    @Email(message = "NOT_VALID_EMAIL")
    private String email;

    @NotBlank(message = "PASSWORD_IS_MANDATORY")
    private String password;

    public Users toEntity() {
        return Users.builder()
                .accountName(accountName)
                .nickname(nickname)
                .email(email)
                .password(password)
                .status(Status.USER)
                .authType(AuthType.LOCAL)
                .build();
    }
}