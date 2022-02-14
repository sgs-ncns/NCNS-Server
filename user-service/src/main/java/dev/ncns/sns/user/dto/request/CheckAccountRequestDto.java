package dev.ncns.sns.user.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CheckAccountRequestDto {

    @NotBlank(message = "계정은 공백일 수 없습니다.")
    private final String accountName;

    @JsonCreator
    public CheckAccountRequestDto(String accountName) {
        this.accountName = accountName;
    }

}
