package dev.ncns.sns.user.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class CheckAccountRequestDto {

    @NotBlank(message = "계정은 공백일 수 없습니다.")
    private final String accountName;

}
