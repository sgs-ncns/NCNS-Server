package dev.ncns.sns.user.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CheckEmailRequestDto {

    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    private final String email;

    @JsonCreator
    public CheckEmailRequestDto(String email) {
        this.email = email;
    }

}
