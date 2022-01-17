package dev.ncns.sns.auth.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@RequiredArgsConstructor
public class LocalLoginRequestDto {

    private static final String emailFormat = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$";

    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    @Pattern(regexp = emailFormat, message = "유효하지 않은 이메일 형식입니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    private final String password;

}
