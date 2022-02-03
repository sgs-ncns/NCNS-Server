package dev.ncns.sns.auth.dto.request;

import dev.ncns.sns.auth.domain.AuthType;
import dev.ncns.sns.auth.dto.validate.AccountLoginValidation;
import dev.ncns.sns.auth.dto.validate.LocalLoginValidation;
import dev.ncns.sns.auth.dto.validate.SocialLoginValidation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@Getter
@RequiredArgsConstructor
public class LoginRequestDto {

    private static final String emailFormat = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$";

    @NotBlank(message = "이메일은 공백일 수 없습니다.", groups = {LocalLoginValidation.class, SocialLoginValidation.class})
    @Null(groups = {AccountLoginValidation.class})
    @Pattern(regexp = emailFormat,
            message = "유효하지 않은 이메일 형식입니다.", groups = {LocalLoginValidation.class, SocialLoginValidation.class})
    private final String email;

    @NotBlank(message = "계정은 공백일 수 없습니다.", groups = AccountLoginValidation.class)
    @Null(groups = {LocalLoginValidation.class, SocialLoginValidation.class})
    private final String accountName;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.", groups = {LocalLoginValidation.class, AccountLoginValidation.class})
    @Null(groups = SocialLoginValidation.class)
    private final String password;

    @NotNull(message = "소셜로그인 타입을 지정하세요.", groups = SocialLoginValidation.class)
    @Null(groups = {LocalLoginValidation.class, AccountLoginValidation.class})
    private final AuthType authType;

}
