package dev.ncns.sns.user.dto.request;

import dev.ncns.sns.user.domain.AuthType;
import dev.ncns.sns.user.domain.Status;
import dev.ncns.sns.user.domain.Users;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@RequiredArgsConstructor
public class SignUpRequestDto {

    private static final String emailFormat = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$";

    @NotBlank(message = "계정은 공백일 수 없습니다.")
    private final String accountName;

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private final String nickname;

    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    @Pattern(regexp = emailFormat, message = "유효하지 않은 이메일 형식입니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    private final String password;

    @NotNull(message = "가입 유형을 지정하세요.")
    private final AuthType authType;

    public Users toEntity() {
        return Users.builder()
                .accountName(accountName)
                .nickname(nickname)
                .email(email)
                .password(password)
                .status(Status.USER)
                .authType(authType)
                .build();
    }

}
