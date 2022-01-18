package dev.ncns.sns.auth.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class AccountLoginRequestDto {

    @NotBlank(message = "계정은 공백일 수 없습니다.")
    private final String account;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    private final String password;

}
