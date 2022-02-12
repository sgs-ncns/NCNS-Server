package dev.ncns.sns.user.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class CheckEmailRequestDto {

    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    private final String email;

}
