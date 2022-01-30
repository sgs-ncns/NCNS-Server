package dev.ncns.sns.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginResponseDto {
    @JsonProperty
    private final Long id;
}