package dev.ncns.sns.post.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateUserPostCountDto {

    @JsonProperty
    private final Long userId;

    @JsonProperty
    private final Boolean isUp;

}
